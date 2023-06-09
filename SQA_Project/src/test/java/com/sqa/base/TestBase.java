package com.sqa.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestBase {
    public static WebDriver driver;
    public static Properties properties;
    public static ThreadLocal<WebDriver> tdriver = new ThreadLocal<WebDriver>();

    public TestBase() throws IOException {
        properties = new Properties();
        FileInputStream inputStream = new FileInputStream(System.getProperty("user.dir") + "/config.properties");
        properties.load(inputStream);
    }

    public static WebDriver initialization() {
        String browserName = properties.getProperty("browser");
        if (browserName.equals("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--incognito");
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if (browserName.equals("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        }
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.get(properties.getProperty("url"));
        return driver;
//        tdriver.set(driver);
//        return getDriver();
    }

    public static synchronized WebDriver getDriver() {
        return tdriver.get();
    }

}
