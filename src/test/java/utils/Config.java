package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();
    private static final Logger LOGGER = LoggerFactory.getLogger(AllureUtils.class);

    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Desculpe, não foi possível encontrar config.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            LOGGER.error("Erro ao Carregar as configurações do Config.properties: ", e);
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
