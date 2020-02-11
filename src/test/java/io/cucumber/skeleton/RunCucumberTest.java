package io.cucumber.skeleton;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"com.zephyr.cucumber.ZephyrXMLFormatter:target/zephyr.xml"})
public class RunCucumberTest {
}
