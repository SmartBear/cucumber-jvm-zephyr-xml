package com.smartbear;

import io.cucumber.core.exception.CucumberException;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static java.util.Locale.ROOT;

public final class ZephyrXMLFormatter implements EventListener {
    private final Writer out;
    private final Document doc;
    private final Element rootElement;

    private TestCase testCase;
    private Element root;

    private final EventHandler<TestSourceRead> sourceReadHandler = this::handleTestSourceRead;
    private final EventHandler<TestCaseStarted> caseStartedHandler = this::handleTestCaseStarted;
    private final EventHandler<TestStepFinished> stepFinishedHandler = this::handleTestStepFinished;
    private final EventHandler<TestCaseFinished> caseFinishedHandler = this::handleTestCaseFinished;
    private final EventHandler<TestRunFinished> runFinishedHandler = event -> finishReport();

    public ZephyrXMLFormatter(URL out) throws IOException {
        this.out = new OutputStreamWriter(new URLOutputStream(out));
        TestCase.currentFeatureFile = null;
        TestCase.previousTestCaseName = "";
        TestCase.exampleNumber = 1;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            rootElement = doc.createElement("testsuite");
            doc.appendChild(rootElement);
        } catch (ParserConfigurationException e) {
            throw new CucumberException("Error while processing unit report", e);
        }
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestSourceRead.class, sourceReadHandler);
        publisher.registerHandlerFor(TestCaseStarted.class, caseStartedHandler);
        publisher.registerHandlerFor(TestCaseFinished.class, caseFinishedHandler);
        publisher.registerHandlerFor(TestStepFinished.class, stepFinishedHandler);
        publisher.registerHandlerFor(TestRunFinished.class, runFinishedHandler);
    }

    private void handleTestSourceRead(TestSourceRead event) {
        TestCase.testSources.addTestSourceReadEvent(String.valueOf(event.getUri()), event);
    }

    private void handleTestCaseStarted(TestCaseStarted event) {
        if (TestCase.currentFeatureFile == null || !TestCase.currentFeatureFile.equals(event.getTestCase().getUri())) {
            TestCase.currentFeatureFile = String.valueOf(event.getTestCase().getUri());
            TestCase.previousTestCaseName = "";
            TestCase.exampleNumber = 1;
        }
        testCase = new TestCase(event.getTestCase());
        root = testCase.createElement(doc);
        testCase.writeElement(root);
        rootElement.appendChild(root);

        increaseAttributeValue(rootElement, "tests");
    }

    private void handleTestStepFinished(TestStepFinished event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            testCase.steps.add((PickleStepTestStep) event.getTestStep());
            testCase.results.add(event.getResult());
        }
    }

    private void handleTestCaseFinished(TestCaseFinished event) {

        List<String> tags = event.getTestCase().getTags();
        List<String> requirementIds = tags
                .stream()
                .filter(tagName -> tagName.startsWith("@JIRA_"))
                .map(tagName -> tagName.replaceAll("^@JIRA_", "AltID_"))
                .collect(Collectors.toList());
        
        List<String> customTags = tags
                .stream()
                .filter(tagName -> !tagName.startsWith("@JIRA_"))
                .map(tagName -> tagName.replaceAll("^@", ""))
                .collect(Collectors.toList());

        testCase.addListToElement(doc, root, "requirements", "requirement", requirementIds);
        testCase.addListToElement(doc, root, "tags", "tag", customTags);

        if (testCase.steps.isEmpty()) {
            testCase.handleEmptyTestCase(doc, root, event.getResult());
        } else {
            testCase.addTestCaseElement(doc, root, event.getResult());
        }
    }

    private void finishReport() {
        try {
            // set up a transformer
            rootElement.setAttribute("name", getClass().getName());
            rootElement.setAttribute("failures", String.valueOf(rootElement.getElementsByTagName("failure").getLength()));
            rootElement.setAttribute("skipped", String.valueOf(rootElement.getElementsByTagName("skipped").getLength()));
            rootElement.setAttribute("time", sumTimes(rootElement.getElementsByTagName("testcase")));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer trans = transformerFactory.newTransformer();
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            StreamResult result = new StreamResult(out);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            closeQuietly(out);
        } catch (TransformerException e) {
            throw new CucumberException("Error while transforming.", e);
        }
    }

    private String sumTimes(NodeList testCaseNodes) {
        double totalDurationSecondsForAllTimes = 0.0d;
        for (int i = 0; i < testCaseNodes.getLength(); i++) {
            try {
                double testCaseTime =
                        Double.parseDouble(testCaseNodes.item(i).getAttributes().getNamedItem("time").getNodeValue());
                totalDurationSecondsForAllTimes += testCaseTime;
            } catch (NumberFormatException | NullPointerException e) {
                throw new CucumberException(e);
            }
        }
        DecimalFormat nfmt = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
        nfmt.applyPattern("0.######");
        return nfmt.format(totalDurationSecondsForAllTimes);
    }

    private void increaseAttributeValue(Element element, String attribute) {
        int value = 0;
        if (element.hasAttribute(attribute)) {
            value = Integer.parseInt(element.getAttribute(attribute));
        }
        element.setAttribute(attribute, String.valueOf(++value));
    }

    private static class TestCase {
        private static final DecimalFormat NUMBER_FORMAT = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
        private static final TestSourcesModel testSources = new TestSourcesModel();

        static {
            NUMBER_FORMAT.applyPattern("0.######");
        }

        private static String currentFeatureFile;
        private static String previousTestCaseName;
        private static int exampleNumber;

        private final List<PickleStepTestStep> steps = new ArrayList<>();
        private final List<Result> results = new ArrayList<>();
        private final io.cucumber.plugin.event.TestCase testCase;

        private TestCase(io.cucumber.plugin.event.TestCase testCase) {
            this.testCase = testCase;
        }

        private Element createElement(Document doc) {
            return doc.createElement("testcase");
        }

        private void writeElement(Element tc) {
            tc.setAttribute("classname", testSources.getFeatureName(currentFeatureFile));
            tc.setAttribute("name", calculateElementName(testCase));
        }

        private String calculateElementName(io.cucumber.plugin.event.TestCase testCase) {
            String testCaseName = testCase.getName();
            if (testCaseName.equals(previousTestCaseName)) {
                return Utils.getUniqueTestNameForScenarioExample(testCaseName, ++exampleNumber);
            } else {
                previousTestCaseName = testCase.getName();
                exampleNumber = 1;
                return testCaseName;
            }
        }

        public void addTestCaseElement(Document doc, Element tc, Result result) {
            tc.setAttribute("time", calculateTotalDurationString(result));

            StringBuilder sb = new StringBuilder();

            addStepAndResultListing(sb);
            Element child;
            if (result.getStatus().is(Status.FAILED)) {
                addStackTrace(sb, result);
                child = createElementWithMessage(doc, sb, "failure", result.getError().getMessage());
            } else if (result.getStatus().is(Status.AMBIGUOUS)) {
                addStackTrace(sb, result);
                child = createElementWithMessage(doc, sb, "failure", result.getError().getMessage());
            } else if (result.getStatus().is(Status.PENDING) || result.getStatus().is(Status.UNDEFINED)) {
                child = createElementWithMessage(doc, sb, "failure", "The scenario has pending or undefined step(s)");
            } else if (result.getStatus().is(Status.SKIPPED) && result.getError() != null) {
                addStackTrace(sb, result);
                child = createElementWithMessage(doc, sb, "skipped", result.getError().getMessage());
            } else {
                child = createElement(doc, sb, "system-out");
            }

            tc.appendChild(child);
        }

        public void handleEmptyTestCase(Document doc, Element tc, Result result) {
            tc.setAttribute("time", calculateTotalDurationString(result));

            String resultType = "failure" ;
            Element child = createElementWithMessage(doc, new StringBuilder(), resultType, "The scenario has no steps");

            tc.appendChild(child);
        }
        
        public void addListToElement(Document doc, Element tc, String parentElementName, String childElementName, List<String> values) {
            Element parentElement = doc.createElement(parentElementName);
            tc.appendChild(parentElement);

            values.forEach(value -> {
                Element childElement = doc.createElement(childElementName);
                parentElement.appendChild(childElement);
                childElement.setTextContent(value);
            });
        }
        
        private String calculateTotalDurationString(Result result) {
            // time here is in nanoSeconds
            return NUMBER_FORMAT.format((double)result.getDuration().getNano()/1000000000);
        }

        private void addStepAndResultListing(StringBuilder sb) {
            for (int i = 0; i < steps.size(); i++) {
                int length = sb.length();
                String resultStatus = "not executed";
                if (i < results.size()) {
                    resultStatus = results.get(i).getStatus().name().toLowerCase(ROOT);
                }
                sb.append(getKeywordFromSource(steps.get(i).getStep().getLine())).append(steps.get(i).getStep().getText());
                do {
                    sb.append(".");
                } while (sb.length() - length < 76);
                sb.append(resultStatus);
                sb.append("\n");
            }
        }

        private String getKeywordFromSource(int stepLine) {
            return testSources.getKeywordFromSource(currentFeatureFile, stepLine);
        }

        private void addStackTrace(StringBuilder sb, Result failed) {
            sb.append("\nStackTrace:\n");
            StringWriter sw = new StringWriter();
            failed.getError().printStackTrace(new PrintWriter(sw));
            sb.append(sw);
        }

        private Element createElementWithMessage(Document doc, StringBuilder sb, String elementType, String message) {
            Element child = createElement(doc, sb, elementType);
            child.setAttribute("message", message);
            return child;
        }

        private Element createElement(Document doc, StringBuilder sb, String elementType) {
            Element child = doc.createElement(elementType);
            // the createCDATASection method seems to convert "\n" to "\r\n" on Windows, in case
            // data originally contains "\r\n" line separators the result becomes "\r\r\n", which
            // are displayed as double line breaks.
            String systemLineSeparator = System.lineSeparator();
            child.appendChild(doc.createCDATASection(sb.toString().replace(systemLineSeparator, "\n")));
            return child;
        }

    }

    private static void closeQuietly(Closeable out) {
        try {
            out.close();
        } catch (IOException ignored) {
            // go gentle into that good night
        }
    }
}
