package ru.erminson.ec.impl;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("All Test Suite")
@SelectClasses({AllUtilsTests.class, AllServiceTests.class, AllRepositoryTests.class})
public class AllTests {
}
