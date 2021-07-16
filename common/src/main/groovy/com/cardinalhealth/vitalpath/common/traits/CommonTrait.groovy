package com.cardinalhealth.vitalpath.common.traits

import geb.navigator.Navigator
import org.openqa.selenium.By

trait CommonTrait {

    def doClick(navigatorItem) {
        waitFor { navigatorItem.displayed }
        waitFor { navigatorItem.click() }
    }

    def clickAttribute(type, identifier){
        def Navigator nav = waitFor {  $("${type}" : identifier) }
        try {
            nav.click()
        } catch (Exception e) {
            if (e.message.contains("is not clickable at point")) {
                def tapToClose = $("data-id": "tap-to-close")
                tapToClose.click()
                waitFor { $('data-id': 'tap-to-close') == [] }
                nav.click()
            } else {
                throw e
            }
        }
    }

    def clickId(identifier){
        clickAttribute("id", identifier)
    }

    def clickDataId(identifier){
        clickAttribute("data-id", identifier)
    }

    def clickClass(identifier){
        clickAttribute("class", identifier)
    }

    def clickName(identifier){
        clickAttribute("name", identifier)
    }

    def enterTextByAttribute(type, identifier, value){
        def Navigator nav = waitFor { $("${type}": identifier) }
        nav << value
    }

    def enterTextByName(identifier, value){
        enterTextByAttribute("name", identifier,  value)
    }

    def enterTextById(identifier, value){
        enterTextByAttribute("id", identifier, value)
    }

    def enterTextByClass(identifier, value){
        enterTextByAttribute("class", identifier, value)
    }

    def isDataIdDisplayed(dataId) {
        def Navigator nav = waitFor {  $("data-id" : dataId) }
        nav.displayed
    }

    def elementExists(selector) {
        def nav = convertSelectorToNavigator(selector)
        nav != null && !nav.empty
    }

    def convertSelectorToNavigator(selector) {
        def nav = selector
        if (!nav instanceof Navigator) {
            nav = $(nav)
        }

        nav
    }

    def dataId(attr) {
        "[data-id='${attr}']"
    }

    def className(attr) {
        ".${attr}"
    }

    def getModule(Navigator container, String className, Class moduleClass){
        def element = container.find(By.className(className))

        assert element : "No ${className} found in ${container.toString()}"
        element.module(moduleClass)
    }

    def getModules(Navigator navigator, String selector, Class moduleClass){
        def results = []
        navigator.find(selector).each { item->
            results.add(item.module(moduleClass))
        }

        results
    }

    def isNonEmptyNavigator(Navigator nav){
        geb.navigator.NonEmptyNavigator.isInstance(nav)
    }

    def isEmptyNavigator(Navigator nav){
        geb.navigator.EmptyNavigator.isInstance(nav)
    }


}
