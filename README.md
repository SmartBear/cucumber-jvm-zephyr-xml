# cucumber-jvm-zephyr-xml

This Cucumber-JVM plugin generates JUnit XML with some modifications
to support Zephyr.

For example:

```gherkin
@XYZ-1 @XYZ-1 @smoke-test
Feature: something
```

This will output the following snippet in the generated XML

```xml
<requirements>
  <requirement>ALTID_XYZ-1</requirement>
  <requirement>ALTID_XYZ-2</requirement>
</requirements>
```

The plugin needs to know what tags to include/exclude from the output.

TODO - some ideas:
- Include tags matching a particular regexp, e.g. `/[A-Z]+-\d+/`
  - How to specify this regexp?
  
## Usage:

Add the following to your pom.xml:

```xml
TODO
```

Add the following to your JUnit class:

```java
@RunWith(Cucumber.class)
@Cucumber.Options(plugin = "com.smartbear.zephyr.cucumber.ZephyrXmlFormatter")
```

That's it!

