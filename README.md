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

**Warning**: This plugin has not been tested against later versions of Cucumber-JVM
and is **unlikely** to be compatible with versions `5.x` and `6.x`.

Until this is fixed, users of this plugin will **not** be able to use recent versions
of Cucumber-JVM.

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

`@` tags in the feature file which are not recognised as requirements (see above), will be inserted into the XML results file as tags to be attached to the Testcase in Zephyr.

For example:

```gherkin
@JIRA_XYZ-1 @JIRA_XYZ-1 @smoke-test
Feature: something
```

This will collect the first two tags as requirements and the last `@` tag will be a tag for the zephyr tescase. 

Here is an example of a single tag in the XML file:
```xml
<tags>                                    ---//  tags: parent element
<tag>Feature1</tag>             ---// tag : child element 
</tags>  
```

Here is an example of a multiple tags in the XML file:
```xml
<tags>
<tag>BVT1</tag>
<tag>BVT2</tag>
</tags>
```

In conjection with having this data in the XML file, we need to ensure the custom parser is able to read the tag. 

Sample line which we should use in the template to add tags 
```xml
\"tag\":\"${group_concat(testsuite.testcase.tags,tag,' ')}\"}
```
(we've already done this for the Cucumber parser in Vortex, but not the other OOTB parsers or any that you may have customised). 

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

## Maintenance

This plugin is maintained by the SmartBear Zephyr team.

## Release process

Update `CHANGELOG.md` to reflect the changes since the previous release.

Contact somebody fro the Cucumber Open core team to get access to secrets.

    export SONATYPE_PASSWORD=...
    export GPG_SIGNING_KEY_PASSPHRASE=...
    mvn clean deploy -Psign-source-javadoc --settings settings.xml -DskipTests=true
    # Find X.Y.Z in pom.xml
    git tag vX.Y.Z
    git push --tags
