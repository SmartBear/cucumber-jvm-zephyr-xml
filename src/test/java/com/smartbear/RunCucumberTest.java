package com.smartbear;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"com.smartbear.ZephyrXMLFormatter:target/zephyr.xml"})
public class RunCucumberTest {
}
