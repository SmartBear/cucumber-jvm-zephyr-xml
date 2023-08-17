package io.cucumber.zephyr;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"io.cucumber.zephyr.ZephyrXMLFormatter:target/zephyr.xml"})
public class RunCucumberTest {
}
