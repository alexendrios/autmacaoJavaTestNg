package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import static utils.Util.esperarPagina;

public class LoginPage {
    WebDriver driver;
    public LoginPage (WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    @FindBy(how = How.XPATH, using = "//*[@class='oxd-topbar-header-breadcrumb']/h6")
    private WebElement homePage;
    @FindBy(how = How.NAME, using = "username")
    private WebElement userName;
    @FindBy(how = How.NAME, using = "password")
    private WebElement passWord;
    @FindBy(how = How.XPATH, using = "//*[@class='oxd-form']/div[3]/button")
    private WebElement btnLogar;
    @FindBy(how = How.XPATH, using = "//*[@class='orangehrm-login-error']/div[1]/div[1]/p")
    private WebElement actualErrorMessage;

    public void navegar(String url){
        driver.get(url);
    }
    public void logar(String user, String password) throws Exception {
        userName.sendKeys(user);
        passWord.sendKeys(password);
        btnLogar.click();
        esperarPagina(3);
    }
    public String getMensagem(){
        return homePage.getText();
    }
    public String getMensagemErro(){
        return actualErrorMessage.getText();
    }
}
