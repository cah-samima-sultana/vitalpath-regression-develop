package com.cardinalhealth.vitalpath.common.modules

import geb.Module
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class LoginModule extends Module {

    void loginWith(iUserName, iPassword) {

        WebDriver frameWebDriver = browser.driver.switchTo().frame(browser.driver.findElement(By.id("iFrame")))

        WebElement username = frameWebDriver.findElement(By.id("un"))
        WebElement password = frameWebDriver.findElement(By.id("pw"))
        WebElement loginButton = frameWebDriver.findElement(By.id("loginButton"))

        username.sendKeys(iUserName)
        password.sendKeys(iPassword)
        //This sleep is required because waitFor does not work properly in the iframe scenario
        sleep(500)
        loginButton.click();

        browser.driver.switchTo().defaultContent();
    }
}