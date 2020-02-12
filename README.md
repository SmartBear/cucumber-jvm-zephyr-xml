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
  <requirement>AltID_XYZ-1</requirement>
  <requirement>AltID_XYZ-2</requirement>
</requirements>
```

The plugin will replace `@JIRA_` with `AltID_` in the generated XML.
  
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

## Compatibility

This plugin will only work with Cucumber-JVM 4.2.X. If you require support for version 5.x,
please open an issue.

## Release process

Update `CHANGELOG.md` to reflect the changes since the previous release.

Find the two environment variables in 1Password

    export SONATYPE_PASSWORD=...
    export GPG_SIGNING_KEY_PASSPHRASE=...
	mvn clean deploy -Psign-source-javadoc --settings settings.xml -DskipTests=true
	# Find X.Y.Z in pom.xml
	git tag vX.Y.Z
	git push --tags

