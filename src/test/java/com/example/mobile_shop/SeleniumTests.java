package com.example.mobile_shop;

import com.example.mobile_shop.entities.Phone;
import com.example.mobile_shop.repositories.PhoneRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SeleniumTests {
    @Autowired
    private PhoneRepository phonesRepository;

    @LocalServerPort
    private int localPort;

    private String serverUrl;
    private WebDriver webDriver;

    @BeforeAll
    public static void init(){
        WebDriverManager.chromedriver().setup();

    }

    @BeforeEach
    public void initServerUrl(){
        phonesRepository.deleteAll();
        this.serverUrl = "http://localhost:" + localPort;
        this.webDriver = new ChromeDriver();
        this.webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

    }

    @AfterEach
    public void teardown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @Test
    public void testWhenFilterPhonesAndAllPhones() throws InterruptedException {
        List<Phone> phones = Arrays.asList(new Phone("Galaxy A101","Samsung",113.99),
                                            new Phone("Galaxy S20","Samsung",699.99),
                                            new Phone("Poco X3 NFC","Xiaomi",275.76),
                                            new Phone("IPhone 12 Pro Max","Apple",1399.60));
        phonesRepository.saveAll(phones);

        String affordablePriceValue = "500";
        WebDriverWait wait = new WebDriverWait(webDriver,30,1000);
        webDriver.get(serverUrl);

        By phonesList = By.id("phonesList");
        wait.until(presenceOfElementLocated(phonesList));
        List<WebElement> webElementPhonesList = webDriver.findElements(phonesList);
        int initialSize = webElementPhonesList.size();

        Thread.sleep(5000);

        By affordablePriceInput = By.id("affordablePriceInput");
        wait.until(presenceOfElementLocated(affordablePriceInput));
        webDriver.findElement(affordablePriceInput).sendKeys(affordablePriceValue);

        Thread.sleep(5000);

        By listFilterButton = By.id("listFilterButton");
        wait.until(elementToBeClickable(listFilterButton));
        webDriver.findElement(listFilterButton).click();

        Thread.sleep(5000);

        By listAllButton = By.id("listAllButton");
        wait.until(elementToBeClickable(listAllButton));
        webDriver.findElement(listAllButton).click();

        Thread.sleep(5000);
        webElementPhonesList = webDriver.findElements(phonesList);

        String phoneName1 = webDriver.findElement(By.id("phoneName" + 0)).getText();
        String brand1 = webDriver.findElement(By.id("brand" + 0)).getText();
        String price1 = webDriver.findElement(By.id("price" + 0)).getText();

        String phoneName2 = webDriver.findElement(By.id("phoneName" + 2)).getText();
        String brand2 = webDriver.findElement(By.id("brand" + 2)).getText();
        String price2 = webDriver.findElement(By.id("price" + 2)).getText();

        assertEquals("", webDriver.findElement(By.id("priceWarning")).getText());
        assertEquals(initialSize, webElementPhonesList.size());
        assertEquals(phones.get(0).getPhoneName(),phoneName1);
        assertEquals(phones.get(0).getBrand(),brand1);
        assertEquals(String.valueOf(phones.get(0).getPrice()),price1);
        assertEquals(phones.get(2).getPhoneName(),phoneName2);
        assertEquals(phones.get(2).getBrand(),brand2);
        assertEquals("275.76",price2);

    }

    @Test
    public void testWhenFilterPhonesHappyFlow() throws InterruptedException {
        List<Phone> phones = Arrays.asList(new Phone("Galaxy A101","Samsung",113.99),
                                            new Phone("Galaxy S20","Samsung",699.99),
                                            new Phone("Poco X3 NFC","Xiaomi",275.76),
                                            new Phone("IPhone 12 Pro Max","Apple",1399.60));
        phonesRepository.saveAll(phones);

        String affordablePriceValue = "450.00";
        WebDriverWait wait = new WebDriverWait(webDriver,30,1000);
        webDriver.get(serverUrl);

        By phonesList = By.id("phonesList");
        wait.until(presenceOfElementLocated(phonesList));
        List<WebElement> webElementPhonesList = webDriver.findElements(phonesList);
        int initialSize = webElementPhonesList.size();

        Thread.sleep(5000);

        By affordablePriceInput = By.id("affordablePriceInput");
        wait.until(presenceOfElementLocated(affordablePriceInput));
        webDriver.findElement(affordablePriceInput).sendKeys(affordablePriceValue);

        Thread.sleep(5000);

        By listFilterButton = By.id("listFilterButton");
        wait.until(elementToBeClickable(listFilterButton));
        webDriver.findElement(listFilterButton).click();

        Thread.sleep(5000);
        webElementPhonesList = webDriver.findElements(phonesList);

        String phoneName1 = webDriver.findElement(By.id("phoneName" + 0)).getText();
        String brand1 = webDriver.findElement(By.id("brand" + 0)).getText();
        String price1 = webDriver.findElement(By.id("price" + 0)).getText();

        String phoneName2 = webDriver.findElement(By.id("phoneName" + 1)).getText();
        String brand2 = webDriver.findElement(By.id("brand" + 1)).getText();
        String price2 = webDriver.findElement(By.id("price" + 1)).getText();


        assertEquals("", webDriver.findElement(By.id("priceWarning")).getText());
        assertEquals(initialSize - 2, webElementPhonesList.size());
        assertEquals(phones.get(0).getPhoneName(),phoneName1);
        assertEquals(phones.get(0).getBrand(),brand1);
        assertEquals(String.valueOf(phones.get(0).getPrice()),price1);
        assertEquals(phones.get(2).getPhoneName(),phoneName2);
        assertEquals(phones.get(2).getBrand(),brand2);
        assertEquals("275.76",price2);

    }

    @Test
    public void testWhenFilterPhonesWithEmptyPrice() throws InterruptedException {
        List<Phone> phones = Arrays.asList(new Phone("Galaxy A101","Samsung",113.99),
                                            new Phone("Galaxy S20","Samsung",699.99),
                                            new Phone("Poco X3 NFC","Xiaomi",275.76),
                                            new Phone("IPhone 12 Pro Max","Apple",1399.60));
        phonesRepository.saveAll(phones);

        String warningMessage = "Please introduce a price!";

        String affordablePriceValue = "";
        WebDriverWait wait = new WebDriverWait(webDriver,30,1000);
        webDriver.get(serverUrl);

        Thread.sleep(5000);

        By affordablePriceInput = By.id("affordablePriceInput");
        wait.until(presenceOfElementLocated(affordablePriceInput));
        webDriver.findElement(affordablePriceInput).sendKeys(affordablePriceValue);

        Thread.sleep(5000);

        By listFilterButton = By.id("listFilterButton");
        wait.until(elementToBeClickable(listFilterButton));
        webDriver.findElement(listFilterButton).click();

        assertEquals(warningMessage, webDriver.findElement(By.id("priceWarning")).getText());

    }

    @Test
    public void testWhenFilterPhonesWithNegativePrice() throws InterruptedException {
        List<Phone> phones = Arrays.asList(new Phone("Galaxy A101","Samsung",113.99),
                                            new Phone("Galaxy S20","Samsung",699.99),
                                            new Phone("Poco X3 NFC","Xiaomi",275.76),
                                            new Phone("IPhone 12 Pro Max","Apple",1399.60));
        phonesRepository.saveAll(phones);

        String warningMessage = "Please introduce a price higher than 0!";

        String affordablePriceValue = "-750";
        WebDriverWait wait = new WebDriverWait(webDriver,30,1000);
        webDriver.get(serverUrl);

        Thread.sleep(5000);

        By affordablePriceInput = By.id("affordablePriceInput");
        wait.until(presenceOfElementLocated(affordablePriceInput));
        webDriver.findElement(affordablePriceInput).sendKeys(affordablePriceValue);

        Thread.sleep(5000);

        By listFilterButton = By.id("listFilterButton");
        wait.until(elementToBeClickable(listFilterButton));
        webDriver.findElement(listFilterButton).click();

        assertEquals(warningMessage, webDriver.findElement(By.id("priceWarning")).getText());

    }

    @Test
    public void testWhenFilterPhonesWithStringPrice() throws InterruptedException {
        List<Phone> phones = Arrays.asList(new Phone("Galaxy A101","Samsung",113.99),
                                            new Phone("Galaxy S20","Samsung",699.99),
                                            new Phone("Poco X3 NFC","Xiaomi",275.76),
                                            new Phone("IPhone 12 Pro Max","Apple",1399.60));
        phonesRepository.saveAll(phones);

        String warningMessage = "Please introduce a valid price!";

        String affordablePriceValue = "price";
        WebDriverWait wait = new WebDriverWait(webDriver,30,1000);
        webDriver.get(serverUrl);

        Thread.sleep(5000);

        By affordablePriceInput = By.id("affordablePriceInput");
        wait.until(presenceOfElementLocated(affordablePriceInput));
        webDriver.findElement(affordablePriceInput).sendKeys(affordablePriceValue);

        Thread.sleep(5000);

        By listFilterButton = By.id("listFilterButton");
        wait.until(elementToBeClickable(listFilterButton));
        webDriver.findElement(listFilterButton).click();

        assertEquals(warningMessage, webDriver.findElement(By.id("priceWarning")).getText());

    }

    @Test
    public void testWhenAddPhoneWithHappyFlow() throws InterruptedException {
        List<Phone> phones = Arrays.asList(new Phone("Galaxy A101","Samsung",113.99),
                                            new Phone("Galaxy S20","Samsung",699.99),
                                            new Phone("Poco X3 NFC","Xiaomi",275.76),
                                            new Phone("IPhone 12 Pro Max","Apple",1399.60));
        phonesRepository.saveAll(phones);

        String phoneNameValue = "IPhone X";
        String brandValue = "Apple";
        String priceValue = "400.45";

        WebDriverWait wait = new WebDriverWait(webDriver,30,1000);
        webDriver.get(serverUrl);

        By phonesList = By.id("phonesList");
        wait.until(presenceOfElementLocated(phonesList));
        List<WebElement> webElementPhonesList = webDriver.findElements(phonesList);
        int initialSize = webElementPhonesList.size();

        Thread.sleep(5000);

        By newPhoneHref = By.id("newPhoneHref");
        wait.until(elementToBeClickable(newPhoneHref ));
        webDriver.findElement(newPhoneHref).click();

        Thread.sleep(5000);

        By phoneNameInput = By.id("phoneNameInput");
        wait.until(presenceOfElementLocated(phoneNameInput));
        webDriver.findElement(phoneNameInput).sendKeys(phoneNameValue);

        Thread.sleep(5000);

        By brandInput = By.id("brandInput");
        wait.until(presenceOfElementLocated(brandInput));
        webDriver.findElement(brandInput).sendKeys(brandValue);

        Thread.sleep(5000);

        By priceInput = By.id("priceInput");
        wait.until(presenceOfElementLocated(priceInput));
        webDriver.findElement(priceInput).sendKeys(priceValue);

        Thread.sleep(5000);

        By submitButton = By.id("submitButton");
        wait.until(elementToBeClickable(submitButton));
        webDriver.findElement(submitButton).click();
        webElementPhonesList = webDriver.findElements(phonesList);

        Thread.sleep(5000);

        String addedPhoneName = webDriver.findElement(By.id("phoneName" + (webElementPhonesList.size() - 1))).getText();
        String addedBrand = webDriver.findElement(By.id("brand" + (webElementPhonesList.size() - 1))).getText();
        String addedPrice = webDriver.findElement(By.id("price" + (webElementPhonesList.size() - 1))).getText();



        assertEquals(serverUrl + "/", webDriver.getCurrentUrl());
        assertEquals(initialSize+1,webElementPhonesList.size());
        assertEquals(phoneNameValue,addedPhoneName);
        assertEquals(brandValue,addedBrand);
        assertEquals(priceValue,addedPrice);


    }

    @Test
    public void testWhenAddPhoneWithEmptyInputs() throws InterruptedException {
        List<Phone> phones = Arrays.asList(new Phone("Galaxy A101","Samsung",113.99),
                                            new Phone("Galaxy S20","Samsung",699.99),
                                            new Phone("Poco X3 NFC","Xiaomi",275.76),
                                            new Phone("IPhone 12 Pro Max","Apple",1399.60));
        phonesRepository.saveAll(phones);

        String warningMessagePhoneName = "Please introduce a phone name!";
        String warningMessageBrand = "Please introduce a brand!";
        String warningMessagePrice = "Please introduce a price!";

        WebDriverWait wait = new WebDriverWait(webDriver,30,1000);
        webDriver.get(serverUrl);

        Thread.sleep(5000);

        By newPhoneHref = By.id("newPhoneHref");
        wait.until(elementToBeClickable(newPhoneHref ));
        webDriver.findElement(newPhoneHref).click();

        Thread.sleep(5000);

        By submitButton = By.id("submitButton");
        wait.until(elementToBeClickable(submitButton));
        webDriver.findElement(submitButton).click();

        assertEquals(warningMessagePhoneName, webDriver.findElement(By.id("phoneNameWarning")).getText());
        assertEquals(warningMessageBrand, webDriver.findElement(By.id("brandWarning")).getText());
        assertEquals(warningMessagePrice, webDriver.findElement(By.id("priceWarning")).getText());

    }

    @Test
    public void testWhenAddPhoneWithNegativePrice() throws InterruptedException {
        List<Phone> phones = Arrays.asList(new Phone("Galaxy A101","Samsung",113.99),
                                            new Phone("Galaxy S20","Samsung",699.99),
                                            new Phone("Poco X3 NFC","Xiaomi",275.76),
                                            new Phone("IPhone 12 Pro Max","Apple",1399.60));
        phonesRepository.saveAll(phones);

        String warningMessagePrice = "Please introduce a price higher than 0!";
        String priceValue = "-900.5";

        WebDriverWait wait = new WebDriverWait(webDriver,30,1000);
        webDriver.get(serverUrl);

        Thread.sleep(5000);

        By newPhoneHref = By.id("newPhoneHref");
        wait.until(elementToBeClickable(newPhoneHref ));
        webDriver.findElement(newPhoneHref).click();

        Thread.sleep(5000);

        By priceInput = By.id("priceInput");
        wait.until(presenceOfElementLocated(priceInput));
        webDriver.findElement(priceInput).sendKeys(priceValue);

        Thread.sleep(5000);

        By submitButton = By.id("submitButton");
        wait.until(elementToBeClickable(submitButton));
        webDriver.findElement(submitButton).click();

        assertEquals(warningMessagePrice, webDriver.findElement(By.id("priceWarning")).getText());

    }

    @Test
    public void testWhenAddPhoneWithStringPrice() throws InterruptedException {
        List<Phone> phones = Arrays.asList(new Phone("Galaxy A101","Samsung",113.99),
                                            new Phone("Galaxy S20","Samsung",699.99),
                                            new Phone("Poco X3 NFC","Xiaomi",275.76),
                                            new Phone("IPhone 12 Pro Max","Apple",1399.60));
        phonesRepository.saveAll(phones);

        String warningMessagePrice = "Please introduce a valid price!";
        String priceValue = "price";

        WebDriverWait wait = new WebDriverWait(webDriver,30,1000);
        webDriver.get(serverUrl);

        Thread.sleep(5000);

        By newPhoneHref = By.id("newPhoneHref");
        wait.until(elementToBeClickable(newPhoneHref ));
        webDriver.findElement(newPhoneHref).click();

        Thread.sleep(5000);

        By priceInput = By.id("priceInput");
        wait.until(presenceOfElementLocated(priceInput));
        webDriver.findElement(priceInput).sendKeys(priceValue);

        Thread.sleep(5000);

        By submitButton = By.id("submitButton");
        wait.until(elementToBeClickable(submitButton));
        webDriver.findElement(submitButton).click();

        assertEquals(warningMessagePrice, webDriver.findElement(By.id("priceWarning")).getText());

    }

    @Test
    public void testWhenRemoveAPhone() throws InterruptedException {
        List<Phone> phones = Arrays.asList(new Phone("Galaxy A101","Samsung",113.99),
                                            new Phone("Galaxy S20","Samsung",699.99),
                                            new Phone("Poco X3 NFC","Xiaomi",275.76),
                                            new Phone("IPhone 12 Pro Max","Apple",1399.60));
        phonesRepository.saveAll(phones);

        WebDriverWait wait = new WebDriverWait(webDriver,30,1000);
        webDriver.get(serverUrl);

        Thread.sleep(5000);

        By deleteList = By.id("phonesList");
        wait.until(presenceOfElementLocated(deleteList));
        List<WebElement> webElementDeleteList = webDriver.findElements(deleteList);
        int initialSize = webElementDeleteList.size();

        By deleteButton = By.id("2");
        wait.until(elementToBeClickable(deleteButton));
        WebElement deletePhoneWebElement = webElementDeleteList.get(2);
        WebElement deleteButtonWebElement = webDriver.findElement(deleteButton);
        deleteButtonWebElement.click();

        Thread.sleep(5000);
        webElementDeleteList = webDriver.findElements(deleteList);

        assertFalse(webElementDeleteList.contains(deletePhoneWebElement));
        assertEquals(initialSize-1, webElementDeleteList.size());

    }

}
