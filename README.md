[![Java CI with Maven](https://github.com/SmartBear/cucumber-jvm-zephyr-xml/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/SmartBear/cucumber-jvm-zephyr-xml/actions?query=workflow%3A%22Java+CI+with+Maven%22)

# cucumber-jvm-zephyr-xml

This Cucumber-JVM plugin generates JUnit XML with proprietary modifications to support Zephyr.

**Warning**: This plugin outputs additional XML elements that are **incompatible**
with widely used XML schemas that validate the generated XML:

* [jenkins-junit.xsd](https://github.com/junit-team/junit5/blob/main/platform-tests/src/test/resources/jenkins-junit.xsd)
* [various other schemas](https://stackoverflow.com/questions/442556/spec-for-junit-xml-output)

Adding these extra XML elements in the official `JUnitXmlFormatter` would cause
validation errors for all users using one of the validation schemas above.

For this reason this plugin is a fork of the `JUnitXmlFormatter`. It has been forked from [Cucumber-JVM 4.2.6](https://github.com/cucumber/cucumber-jvm/blob/main/CHANGELOG.md#426-2019-03-06).

## Custom `<requirements>` element

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

## Using `<tags>` element

`@` tags in the feature file which are not recognised as requirements (see above), will be inserted into the XML results file as tag nodes.

For example:

```gherkin
@JIRA_XYZ-1 @JIRA_XYZ-1 @smoke-test
Feature: something
```

This will collect the first two tags as requirements and the last `@` tag will be a tag for the zephyr tescase.

Here is an example of a single tag in the XML file:
```xml
<tags>                          ---//  tags: parent element
<tag>Feature1</tag>             ---// tag : child element
</tags>  
```

Here is an example of multiple tags in the XML file:
```xml
<tags>
<tag>BVT1</tag>
<tag>BVT2</tag>
</tags>
```

## Usage:

Add the dependency to your pom.xml:

```xml
<dependency>
    <groupId>com.smartbear</groupId>
    <artifactId>zephyr-xml-formatter</artifactId>
    <version>7.21.1.1</version>
</dependency>
```

Add the following to your JUnit class:

```java
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"com.smartbear.ZephyrXMLFormatter:target/zephyr.xml"})
```

## Maintenance

This plugin is maintained by the SmartBear Zephyr team.

## Release process

* Update the version number in `pom.xml`.
* Update `CHANGELOG.md` to reflect the changes since the previous release.
* Commit your files.
* Tag the `master` branch with the version number (e.g. `vX.Y.Z`).
* Create the release branch in the form `release/vX.Y.Z` to automatically publish.

### Manual Publish

Contact somebody from the Cucumber Open core team to get access to secrets.

```bash
make docker-run-with-secrets
make release
```

This should tag the git repository and upload artefacts to Maven Central.
