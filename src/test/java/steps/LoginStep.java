package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.LoginPage;
import utils.Util;

public class LoginStep {

    public LoginPage page = new LoginPage(Util.getDriver());

    @Given("User is on HRMLogin page {string}")
    public void loginTest(String url) {
        page.navegar(url);
    }

    @When("User enters username as {string} and password as {string}")
    public void goToHomePage(String userName, String passWord) throws Exception {
        page.logar(userName, passWord);
    }

    @Then("User should be able to login successfully and new page open")
    public void verifyLogin() {

      Assert.assertEquals(page.getMensagem(),"Dashboard");
    }

    @Then("User should be able to see error message {string}")
    public void verifyErrorMessage(String expectedErrorMessage) {

        Assert.assertEquals(page.getMensagemErro(), expectedErrorMessage);
    }
}
