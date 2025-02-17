package com.smartbear;

public class Utils {
    public static String getUniqueTestNameForScenarioExample(String testCaseName, int exampleNumber) {
        return testCaseName + (testCaseName.contains(" ") ? " " : "_") + exampleNumber;
    }
}
