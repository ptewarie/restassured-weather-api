package Steps;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(features = {"src/test/java/Features/"} , plugin = {"json:target/cucumber.json", "html:target/site/cucumber-pretty"},  glue = "Steps", strict = true)
public class TestRunner extends AbstractTestNGCucumberTests {

}
