package utils;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.testng.Assert.assertTrue;


public class Util {
    private static final Logger LOGGER = LoggerFactory.getLogger(AllureUtils.class);
    private static WebDriver driver;
    public static void renameVideoFile(File oldFile, File newFile) {
        try {
            if (oldFile.renameTo(newFile)) {
                LOGGER.info("Video file renamed successfully to: {}", newFile);
            } else {
                LOGGER.error("Failed to rename video file: {}", oldFile);
            }
        } catch (Exception e) {
            LOGGER.error("Error renaming video file: ", e);
        }
    }
    public static WebDriver getDriver() {
        if (driver == null) {
            try {
                PageLoadStrategy pageLoadStrategy = PageLoadStrategy.NORMAL;
                ChromeOptions options = getChromeOptions(pageLoadStrategy);
                ChromeDriverService driverService = ChromeDriverService.createDefaultService();
                driver = new ChromeDriver(driverService, options);
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
            } catch (Exception e) {
                LOGGER.error("Error in driver creation: ", e);
                throw new RuntimeException(e);
            }
        }
        return driver;
    }
    public static void killDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
    public static void esperarPagina(int tempo) throws Exception {
        for (int i = 0; i < tempo; i++) {
            Thread.sleep(1000);
        }
    }
    public static void aguardarVisibilidade(Boolean visibilidade, Integer tempoEspera, String valorXpath)
            throws Exception {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(tempoEspera));

        if (visibilidade = true) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(valorXpath)));
        } else {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(valorXpath)));
        }

    }
    public static void limparCache() {
        try {
            Robot r = new Robot();
            r.keyPress(KeyEvent.VK_CONTROL);
            r.keyPress(KeyEvent.VK_SHIFT);
            r.keyPress(KeyEvent.VK_DELETE);
            r.keyRelease(KeyEvent.VK_CONTROL);
            r.keyRelease(KeyEvent.VK_SHIFT);
            r.keyRelease(KeyEvent.VK_DELETE);
            Thread.sleep(1000);
            r.keyPress(KeyEvent.VK_ENTER);
            r.keyRelease(KeyEvent.VK_ENTER);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void paginaContemTexto(String text) throws Exception {
        esperarPagina(2);
        boolean existe = getDriver().getPageSource().contains(text);
        if (!existe) {
            System.out.println("Não encontrei a String: " + text);
        }
        assertTrue(existe);

    }
    public static boolean textoExiste(String texto) {
        return getDriver().getPageSource().contains(texto);

    }
    public static void capturarScreenshot() {
        File imagem = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        try {
            Allure.addAttachment("Screenshot", Files.newInputStream(Paths.get(imagem.getPath())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String gerarNumerosAleatorios(int qtdDigitos) {
        String numero = Double.toString(Math.random());
        return numero.substring(3, qtdDigitos + 3);
    }
    public static void aceitarAlertaJavascript() {
        Alert alt = getDriver().switchTo().alert();
        alt.accept();
    }
    public static void cancelarAlertaJavascript() {
        Alert alt = getDriver().switchTo().alert();
        alt.dismiss();
    }
    public static void movimentarScroll(String pixels) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("window.parent.scrollTo(0," + pixels + ");", "0");
    }
    public static boolean elementoExiste(String elemento) {
        boolean existe = false;
        try {
            existe = getDriver().findElement(By.xpath(elemento)).isEnabled();
        } catch (NoSuchElementException e) {
            existe = false;
        }
        return existe;
    }
    public static void moverMouse() throws Exception {
        Robot robot = new Robot();
        robot.mouseMove(+400, +100);
        robot.mouseMove(-400, -100);
    }
    public static void atualizarPagina() throws Exception {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_F5);
        robot.keyRelease(KeyEvent.VK_F5);
        Thread.sleep(10000);
    }
    public static void moverMousePara(String xPath) throws Exception {
        Actions acao = new Actions(getDriver());
        acao.moveToElement(getDriver().findElement(By.xpath(xPath))).build().perform();
    }
    public static WebElement findElement(String xPath) {
        WebDriverWait wait = new WebDriverWait(getDriver(),Duration.ofSeconds(20));
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
        } catch (org.openqa.selenium.TimeoutException e) {
            // Handle timeout exception
            System.out.println("Timeout waiting for element with xpath: " + xPath);
            throw e;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.out.println("Element not found with xpath: " + xPath);
            throw e;
        }
    }
    public static List<WebElement> findElements(String xPath) {
        return getDriver().findElements(By.xpath(xPath));
    }
    public static void dobleClick(String xPath) {
        (new Actions(getDriver())).doubleClick(findElement(xPath)).build().perform();
    }
    public static void scrollByElement(WebElement elemento) throws Exception {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].scrollIntoView();", elemento);
    }
    public static void addEnvironmentAllure() {
        ArrayList<String> infomationAplication;
        LOGGER.info("Realizando as configurações da Identificação do Projeto");
        try {

            infomationAplication = new ArrayList<>();
            infomationAplication.add(Config.getProperty("APLICATION"));
            infomationAplication.add(Config.getProperty("VERSION"));
            infomationAplication.add(Config.getProperty("ENVIRONMENT"));
            Capabilities cap = ((RemoteWebDriver) getDriver()).getCapabilities();
            File env = new File("./target/allure-results/environment.properties");
            BufferedWriter as = new BufferedWriter(new FileWriter(env));
            as.write("BROWSER = "+cap.getBrowserName().toUpperCase()+" - Version: "+ cap.getBrowserVersion());
            as.newLine();
            as.write("APLICATION = "+infomationAplication.get(0).toUpperCase() + " - VERSION: " +infomationAplication.get(1));
            as.newLine();
            as.write("ENVIRONMENT = " + infomationAplication.get(2).toUpperCase());
            as.newLine();
            as.write("OPERATIONAL SYSTEM:  " + System.getProperty("os.name").toUpperCase()+" - USER: " +System.getProperty("user.name").toUpperCase());
            as.close();
            LOGGER.info("Informações Configuradas");
        } catch (Exception e) {
            LOGGER.error("Erro ao Configurar as Informaações do Projeto: ", e);
            throw new RuntimeException(e);
        }

    }
    public static void focoEmOutraJanela() {
        for (String winHandle : getDriver().getWindowHandles()) {
            getDriver().switchTo().window(winHandle);
        }
    }
    public static void janelaPrincipal() {
        String mainWindow = getDriver().getWindowHandle();
        getDriver().switchTo().window(mainWindow);

    }
    public static String getUrl() {
        return getDriver().getCurrentUrl();
    }
    public static void fecharJanelaAtual() {
        getDriver().close();
    }
    public static int quantidadeDejanelas() {
        return getDriver().getWindowHandles().size();
    }
    public static void escritorArquivos(String arquivo, String conteudo) throws IOException, IOException {

        // Cria arquivo
        File file = new File(arquivo);

        // Prepara para escrever no arquivo
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        try {

            // Se o arquivo nao existir, ele gera
            if (!file.exists()) {
                file.createNewFile();
            }

            // Escreve e fecha arquivo
            bw.write(conteudo);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // fecha o arquivo
        bw.close();
    }
    public static boolean verificarArquivo(String path){
        File file = new File(path);
        if (file.exists()){
            return true;
        }else {
            return false;
        }
    }
    public static String listarArquivo(String path){
        File file = new File(path);
        File afile[] = file.listFiles();
        String retorno = "";
        for (int i = 0; i < afile.length; i++) {
            if(afile[i].toString().endsWith("xlsx")){
                retorno = afile[i].toString();
            }
        }
        return retorno;
    }
    public static void deleteFile(String file){
        File af = new File(file);
        af.delete();
    }
    public static boolean elementoExisteCss(String elemento) {
        boolean existe = false;
        try {
            existe = getDriver().findElement(By.cssSelector(elemento)).isEnabled();
        } catch (NoSuchElementException e) {
            existe = false;
        }
        return existe;
    }
    private static ChromeOptions getChromeOptions(PageLoadStrategy pageLoadStrategy) {
        HashMap<String, Object> chromePreferences = new HashMap<>();
        chromePreferences.put("profile.default_content_settings.popups", 0);
        chromePreferences.put("download.prompt_for_download", "false");
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(pageLoadStrategy);
        boolean isHeadless = Boolean.parseBoolean(Config.getProperty("headless"));
        if (isHeadless) {
            options.addArguments("--headless");
        }
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--no-sandbox");
        options.addArguments("disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("window-size=1920,1080");
        options.setExperimentalOption("prefs", chromePreferences);
        return options;
    }
}
