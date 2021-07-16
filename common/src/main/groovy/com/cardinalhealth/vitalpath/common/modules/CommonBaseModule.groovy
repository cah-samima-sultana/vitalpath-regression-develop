package com.cardinalhealth.vitalpath.common.modules

import com.cardinalhealth.vitalpath.common.traits.CommonAnimationTrait
import com.cardinalhealth.vitalpath.common.traits.CommonTrait
import geb.Module
import geb.navigator.Navigator
import org.openqa.selenium.By

class CommonBaseModule extends Module implements CommonAnimationTrait, CommonTrait {

    static String SCROLL_CONTAINER_CLASS = ".scrollContainer"

    def assertTextContainsByDataId(dataId, expectedValue) {
        assertTextContainsByAttribute("data-id", dataId, expectedValue)
    }

    def assertTextContainsByName(elementName, expectedValue) {
        assertTextContainsByAttribute("name", elementName, expectedValue)
    }

    def assertTextValueById(elementId, expectedValue) {
        assertTextContainsByAttribute("id", elementId, expectedValue)
    }

    def assertTextContainsByAttribute(attr, attrValue, expectedValue) {
        def actualElement = findElement(attr, attrValue)
        def actualValue = actualElement.text().toString().toLowerCase()
        assert actualValue.contains(expectedValue.toLowerCase()) : "Could not assert [$attr=$attrValue] contains [$expectedValue].  Actual value: [$actualValue]"
        true
    }

    def assertInputValueByDataId(dataId, expectedValue) {
        assertInputValueByAttribute("data-id", dataId, expectedValue)
    }

    def assertInputValueByName(elementName, expectedValue) {
        assertInputValueByAttribute("name", elementName, expectedValue)
    }

    def assertInputValueByAttribute(attr, attrValue, expectedValue) {
        def actualElement = findElement(attr, attrValue)
        def actualValue = actualElement.value()
        assert (actualValue == expectedValue) : "Could not assert [$attr=$attrValue]. Expected value [$expectedValue] does not equal actual value [$actualValue]"
        true
    }

    def assertHrefById(elementId, attribute, expectedValue){
        def actualElement = findElement('id', elementId)
        def actualValue = actualElement.attr(attribute)
        assert actualValue.endsWith(expectedValue) : "Could not assert [$elementId] - [$attribute]. Expected value [$expectedValue] does not equal actual value [$actualValue]"
        return true
    }

    def findElement(attr, attrValue){
        def actualElement = $("[$attr=$attrValue]")
        return actualElement
    }

    def isNonEmptyNavigator(Navigator nav){
        geb.navigator.NonEmptyNavigator.isInstance(nav)
    }

    def isNonEmptyNavigatorByClassName(String className){
        isNonEmptyNavigator(browser.$(By.className(className)))
    }

    def isEmptyNavigatorByClassName(String className){
        isEmptyNavigator(browser.$(By.className(className)))
    }


}
