package utils;

import io.cucumber.java.Scenario;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static utils.Util.getDriver;

public class AllureUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllureUtils.class);

    @Attachment(value = "Video", type = "video/mp4")
    public static void attachVideo(Scenario scenario) {
        try {
            String filePath = "./target/videos/"+scenario.getName() + "-" + scenario.getStatus() +"-"+scenario.getId()+".avi";
            File video = new File(filePath);
            FileInputStream fis = new FileInputStream(video);
            fis.close();
            LOGGER.info("Salvando Evidência em video formato '.avi' referente ao cenário: {}", scenario.getName() + "-" + scenario.getStatus() +"-"+scenario.getId());
        } catch (IOException e) {
            LOGGER.error("Erro ao executar a gravação da evidência em vídeo: ", e);
            throw new RuntimeException(e);
        }
    }

    public  static void attachImage(Scenario scenario){
        try {
            String screenshotName = scenario.getName();
            final byte[] screenshot = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "img/png", screenshotName);

            File imagem = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(imagem,
                    (new File("./target/screnshot", scenario.getName() + " - " + scenario.getStatus()+"-" +scenario.getId()+ ".jpg")));
            LOGGER.info("Salvando Evidência em imagem formato: '.jpg' referente ao cenário: {}", scenario.getName() + " - " + scenario.getStatus() +"-"+scenario.getId());
        }catch (IOException e) {
            LOGGER.error("Erro ao coletar a evidência em imagem: ", e);
            throw new RuntimeException(e);
        }
    }
}
