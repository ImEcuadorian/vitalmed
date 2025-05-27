package io.github.imecuadorian.vitalmed;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("io.github.imecuadorian.vitalmed.features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "io.github.imecuadorian.vitalmed.steps")
public class RunCucumberTest {}