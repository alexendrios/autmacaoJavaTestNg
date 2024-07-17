package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AllureUtils;
import utils.Config;
import utils.VideoRecorderUtil;
import java.io.File;
import java.io.IOException;
import static utils.AllureUtils.attachImage;
import static utils.AllureUtils.attachVideo;
import static utils.Util.*;


public class Hooks {

    VideoRecorderUtil videoRecorderUtil = new VideoRecorderUtil();
    private static final Logger LOGGER = LoggerFactory.getLogger(AllureUtils.class);
    private String nameScenario;
    private final boolean isHeadless = Boolean.parseBoolean(Config.getProperty("headless"));

    @Before
    public void setUp(Scenario scenario) throws Exception {
        try {
            LOGGER.info("Executando as Configurações do SETUP");
            getDriver();
            LOGGER.info("Execução do Cenário: {}\n...\n", scenario.getName()+"-"+scenario.getId());
            if (!isHeadless) {
                nameScenario = scenario.getName();
                videoRecorderUtil.startRecording(nameScenario);
                nameScenario = "./target/videos/"+scenario.getName()+".avi";
            }
        }catch (IOException e){
            LOGGER.error("Erro ao executar as Configurações do SETUP: ", e);
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown(Scenario scenario) throws Exception {
        try {
            if (!isHeadless) {
                videoRecorderUtil.stopRecording();
                File oldFile = new File(nameScenario);
                File newfile = new File("./target/videos/"+scenario.getName() + "-" + scenario.getStatus() +"-"+scenario.getId()+".avi");
                renameVideoFile(oldFile,newfile);
                attachVideo(scenario);
            }
            attachImage(scenario);
            addEnvironmentAllure();
        } finally {
            killDriver();
        }
    }
}
