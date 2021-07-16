package com.cardinalhealth.vitalpath.cabinet.modules

import geb.Module
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class CabinetLoginModule extends Module {

    void loginWith(iUserName, iPassword) {

        WebDriver frameWebDriver = browser.driver.switchTo().frame(0)

        WebElement username = frameWebDriver.findElement(By.id("un"))
        WebElement password = frameWebDriver.findElement(By.id("pw"))
        WebElement loginButton = frameWebDriver.findElement(By.id("loginButton"))

        username.sendKeys(iUserName)
        password.sendKeys(iPassword)
        //This sleep is required because waitFor does not work properly in the iframe scenario
        sleep(250)
        loginButton.click();

        browser.driver.switchTo().defaultContent();
    }
}