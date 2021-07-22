package ru.mai;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Logging {

    final static Logger LOGGER = LoggerFactory.getLogger(Logging.class);

    /**
     * переменная для проверки того, была ли выполнена настройка логгера
     */

    static boolean config = false;

    /**
     * записать в лог сообщение о предупреждении
     */

    public static void writeWarn(String data, Object e) {
        if (!config) {
            configurate();
            config = true;
        }
        LOGGER.warn(data, e);
    }

    /**
     * записать в лог сообщение об ошибке
     */

    public static void writeError(Exception e) {
        if (!config) {
            configurate();
            config = true;
        }
        LOGGER.error(String.valueOf(e));
    }

    /**
     * настроить свойства логгера
     */

    private static void configurate() {
        Properties props = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("log4j.properties");
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        try {
            props.load(inputStream);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        PropertyConfigurator.configure(props);
    }
}