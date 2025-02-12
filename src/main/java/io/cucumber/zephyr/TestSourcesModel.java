package io.cucumber.zephyr;

import io.cucumber.gherkin.GherkinParser;
import io.cucumber.gherkin.GherkinDialect;
import io.cucumber.gherkin.GherkinDialectProvider;
import io.cucumber.messages.types.*;
import io.cucumber.plugin.event.TestSourceRead;

import java.util.*;

import static io.cucumber.messages.types.SourceMediaType.TEXT_X_CUCUMBER_GHERKIN_PLAIN;

final class TestSourcesModel {
    private final Map<String, TestSourceRead> pathToReadEventMap = new HashMap<>();
    private final Map<String, Optional<GherkinDocument>> pathToAstMap = new HashMap<>();

    void addTestSourceReadEvent(String path, TestSourceRead event) {
        pathToReadEventMap.put(path, event);
    }

    Feature getFeature(String path) {
        if (!pathToAstMap.containsKey(path)) {
            parseGherkinSource(path);
        }
        if (pathToAstMap.containsKey(path)) {
            return pathToAstMap.get(path).flatMap(GherkinDocument::getFeature).orElse(null);
        }
        return null;
    }

    String getKeywordFromSource(String uri, int stepLine) {
        Feature feature = getFeature(uri);
        if (feature != null) {
            TestSourceRead event = getTestSourceReadEvent(uri);
            if (event == null || event.getSource() == null) {
                return "";
            }
            String trimmedSourceLine = event.getSource().split("\n")[stepLine - 1].trim();
            GherkinDialect dialect = new GherkinDialectProvider(feature.getLanguage()).getDefaultDialect();
            for (String keyword : dialect.getStepKeywords()) {
                if (trimmedSourceLine.startsWith(keyword)) {
                    return keyword;
                }
            }
        }
        return "";
    }

    private TestSourceRead getTestSourceReadEvent(String uri) {
        if (pathToReadEventMap.containsKey(uri)) {
            return pathToReadEventMap.get(uri);
        }
        return null;
    }

    String getFeatureName(String uri) {
        Feature feature = getFeature(uri);
        if (feature != null) {
            return feature.getName();
        }
        return "";
    }

    private void parseGherkinSource(String path) {
        if (!pathToReadEventMap.containsKey(path)) {
            return;
        }
        String source = pathToReadEventMap.get(path).getSource();

        GherkinParser parser = GherkinParser.builder()
                .includeSource(false)
                .includeGherkinDocument(true)
                .includePickles(false)
                .idGenerator(() -> UUID.randomUUID().toString())
                .build();
        Source gherkinSource = new Source(path, source, TEXT_X_CUCUMBER_GHERKIN_PLAIN);

        List<Envelope> envelopes = parser.parse(Envelope.of(gherkinSource)).toList();

        Optional<GherkinDocument> gherkinDocument = Optional.empty();
        for (Envelope envelope : envelopes) {
            if (envelope.getGherkinDocument().isPresent()) {
                gherkinDocument = envelope.getGherkinDocument();
            }
        }
        pathToAstMap.put(path, gherkinDocument);
        Map<Long, AstNode> nodeMap = new HashMap<>();
        AstNode currentParent = new AstNode(gherkinDocument.flatMap(GherkinDocument::getFeature), null);
        List<FeatureChild> featureChildren = gherkinDocument.flatMap(GherkinDocument::getFeature).map(Feature::getChildren).orElse(List.of());
        for (FeatureChild child : featureChildren) {
            processFeatureDefinition(nodeMap, child, currentParent);
        }
    }

    private void processFeatureDefinition(Map<Long, AstNode> nodeMap, FeatureChild child, AstNode currentParent) {
        if (child.getBackground().isPresent()) {
            processBackgroundDefinition(nodeMap, child.getBackground().get(), currentParent);
        } else if (child.getScenario().isPresent()) {
            processScenarioDefinition(nodeMap, child.getScenario().get(), currentParent);
        } else if (child.getRule().isPresent()) {
            AstNode childNode = new AstNode(child.getRule(), currentParent);
            nodeMap.put(child.getRule().get().getLocation().getLine(), childNode);
            for (RuleChild ruleChild : child.getRule().get().getChildren()) {
                processRuleDefinition(nodeMap, ruleChild, childNode);
            }
        }
    }

    private void processBackgroundDefinition(Map<Long, AstNode> nodeMap, Background background, AstNode currentParent) {
        AstNode childNode = new AstNode(background, currentParent);
        nodeMap.put(background.getLocation().getLine(), childNode);
        for (Step step : background.getSteps()) {
            nodeMap.put(step.getLocation().getLine(), new AstNode(step, childNode));
        }
    }

    private void processScenarioDefinition(Map<Long, AstNode> nodeMap, Scenario child, AstNode currentParent) {
        AstNode childNode = new AstNode(child, currentParent);
        nodeMap.put(child.getLocation().getLine(), childNode);
        for (Step step : child.getSteps()) {
            nodeMap.put(step.getLocation().getLine(), new AstNode(step, childNode));
        }
        if (!child.getExamples().isEmpty()) {
            processScenarioOutlineExamples(nodeMap, child, childNode);
        }
    }

    private void processRuleDefinition(Map<Long, AstNode> nodeMap, RuleChild child, AstNode currentParent) {
        if (child.getBackground().isPresent()) {
            processBackgroundDefinition(nodeMap, child.getBackground().get(), currentParent);
        } else if (child.getScenario().isPresent()) {
            processScenarioDefinition(nodeMap, child.getScenario().get(), currentParent);
        }
    }

    private void processScenarioOutlineExamples(Map<Long, AstNode> nodeMap, Scenario scenarioOutline, AstNode parent) {
        for (Examples examples : scenarioOutline.getExamples()) {
            Optional<TableRow> tableHeader = examples.getTableHeader();
            if (tableHeader.isEmpty()) {
                continue;
            }
            AstNode examplesNode = new AstNode(examples, parent);
            TableRow headerRow = tableHeader.get();
            AstNode headerNode = new AstNode(headerRow, examplesNode);
            nodeMap.put(headerRow.getLocation().getLine(), headerNode);
            for (int i = 0; i < examples.getTableBody().size(); ++i) {
                TableRow examplesRow = examples.getTableBody().get(i);
                Object rowNode = new ExamplesRowWrapperNode(i);
                AstNode expandedScenarioNode = new AstNode(rowNode, examplesNode);
                nodeMap.put(examplesRow.getLocation().getLine(), expandedScenarioNode);
            }
        }
    }

    record ExamplesRowWrapperNode(int bodyRowIndex) { }

    record AstNode(Object node, TestSourcesModel.AstNode parent) { }
}
