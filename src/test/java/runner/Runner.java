package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        tags = "",
        features = {"src/test/resources/features"},
        glue = {"steps"},
        plugin = {
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "pretty",
                "json:target/cucumber.json",
                "summary"
        }
)
public class Runner extends AbstractTestNGCucumberTests {
}
