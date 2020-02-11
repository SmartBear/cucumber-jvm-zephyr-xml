# cucumber-jvm-zephyr-xml

This Cucumber-JVM plugin generates JUnit XML with modifications to support Zephyr.

Any tags in the feature file starting with `@JIRA_` will be outputted to the XML.

For example:

```gherkin
@JIRA_XYZ-1 @JIRA_XYZ-1 @smoke-test
Feature: something
```

This will output the following snippet in the generated XML

```xml
<requirements>
  <requirement>ALTID_XYZ-1</requirement>
  <requirement>ALTID_XYZ-2</requirement>
</requirements>
```

The plugin will replace `@JIRA_` with `ALTID_` in the generated XML.
  
## Usage:

Add the dependency to your pom.xml:

```xml
<dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>zephyr-xml-formatter</artifactId>
    <version>4.2.0</version>
</dependency>
```

Add the following to your JUnit class:

```java
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"io.cucumber.zephyr.ZephyrXMLFormatter:target/zephyr.xml"})
```

That's it!

## Release process

Find the two environment variables in 1Password

    export SONATYPE_PASSWORD=...
    export GPG_SIGNING_KEY_PASSPHRASE=...
	mvn deploy -Psign-source-javadoc --settings settings.xml -DskipTests=true

