package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    TestLexer.class,
    TestParser.class,
    TestSemantic.class,
    TestExtraFeatures.class,
    TestCodeGeneration.class
})
public class TestSuite {

}
