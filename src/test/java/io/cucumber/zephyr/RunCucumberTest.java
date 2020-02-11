package io.cucumber.zephyr;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"io.cucumber.zephyr.ZephyrXMLFormatter:target/zephyr.xml"})
public class RunCucumberTest {
}
