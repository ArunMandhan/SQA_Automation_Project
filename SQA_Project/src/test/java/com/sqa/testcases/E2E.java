package com.sqa.testcases;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class E2E {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
    }

    @AfterClass
    public void teardown() {
        driver.quit();
    }

    @Test
    public void testBrowseProductCategories() {
        HomePage homePage = new HomePage(driver);
        homePage.open();

        CategoryPage booksPage = homePage.browseCategory("Books");
        Assert.assertEquals(booksPage.getPageTitle(), "Demo Web Shop. Books", "Incorrect page title for Books category");

        CategoryPage computersPage = homePage.browseCategory("Computers");
        Assert.assertEquals(computersPage.getPageTitle(), "Demo Web Shop. Computers", "Incorrect page title for Computers category");

        CategoryPage electronicsPage = homePage.browseCategory("Electronics");
        Assert.assertEquals(electronicsPage.getPageTitle(), "Demo Web Shop. Electronics", "Incorrect page title for Electronics category");
    }

    @Test
    public void testSortProductsByPriceLowToHigh() {
        HomePage homePage = new HomePage(driver);
        homePage.open();

        CategoryPage booksPage = homePage.browseCategory("Books");
        Assert.assertEquals(booksPage.getPageTitle(), "Demo Web Shop. Books", "Incorrect page title for Books category");

        booksPage.sortProductsByPriceLowToHigh();

        Assert.assertTrue(booksPage.isProductsSortedByPriceLowToHigh(), "Products are not sorted by price: Low to High");
    }

    // Utility method to take a screenshot
    private void takeScreenshot(String screenshotName) {
        // Code to capture screenshot
    }

    // Utility method to generate a report
    private void generateReport() {
        // Code to generate report
    }

    // Utility class for waits, validations, etc.
    private class Utilities {
        // Code for utility methods
    }

    // Home page class
    private class HomePage {
        private WebDriver driver;

        public HomePage(WebDriver driver) {
            this.driver = driver;
        }

        public void open() {
            driver.get("https://demowebshop.tricentis.com/");
            driver.manage().window().maximize();
        }

        public CategoryPage browseCategory(String category) {
            WebElement categoryLink = driver.findElement(By.xpath("//div[@class='listbox']/ul/li/a[contains(text(), '" + category + "')]"));
            categoryLink.click();

            return new CategoryPage(driver);
        }
    }

    // Category page class
    private class CategoryPage {
        private WebDriver driver;

        public CategoryPage(WebDriver driver) {
            this.driver = driver;
        }

        public String getPageTitle() {
            return driver.getTitle();
        }

        public void sortProductsByPriceLowToHigh() {
            WebElement sortSelect = driver.findElement(By.xpath("//select[@id='products-orderby']"));
            Select sortOption = new Select(sortSelect);
            sortOption.selectByVisibleText("Price: Low to High");

            // Wait for the page to reload and products to be sorted
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='product-item']//span[@class='price actual-price']")));
        }

        public boolean isProductsSortedByPriceLowToHigh() {
            // Check the sorting order of the products based on the prices
            WebElement firstProductPrice = driver.findElement(By.xpath("//div[@class='product-item']//span[@class='price actual-price']"));
            WebElement secondProductPrice = driver.findElement(By.xpath("//div[@class='product-item'][2]//span[@class='price actual-price']"));

            double firstPrice = Double.parseDouble(firstProductPrice.getText().substring(1));
            double secondPrice = Double.parseDouble(secondProductPrice.getText().substring(1));

            return firstPrice <= secondPrice;
        }
    }
}
