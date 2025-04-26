package ru.stepup;

import io.restassured.RestAssured;
import java.io.InputStream;
import java.util.Properties;

public abstract class BaseTest {
    static {
        Properties props = new Properties();
        try (InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("application.properties")) {
            props.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось загрузить application.properties", e);
        }
        RestAssured.baseURI = props.getProperty("base.uri");
    }
}
