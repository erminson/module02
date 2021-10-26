package ru.erminson.ec.impl;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Repository Test Suite")
@SelectPackages("ru.erminson.ec.impl.repository")
public class AllRepositoryTests {
}
