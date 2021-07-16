package com.cardinalhealth.vitalpath.common.modules

import com.cardinalhealth.vitalpath.utils.SelectorOptions
import org.openqa.selenium.Keys

class SelectorModule extends CommonBaseModule  {


    def selector = ""

    static content = {
        dropDown(required:true, wait: true) { selector }

        dropDownTrigger(required:true, wait: true) { dropDown.find(".ember-power-select-trigger")}

        dropDownItem(wait: true) { itemValue ->
            $('.ember-power-select-option', text: iContains(itemValue) )
            sleep(500)
        }

        dropDownContent(required: false, wait: 1) { $(".ember-basic-dropdown-content") }
        searchBox(required: false) { dropDownContent.find('.ember-power-select-search-input') }
        dropDownResults(required: false) { dropDownContent.find(".ember-power-select-options") }
        dropDownFirstItem(required: false) { dropDownResults.firstElement() }
    }

    def open(){
        if(dropDownResults.size() == 0 && dropDownResults.isDisplayed() == false){
            dropDownTrigger.click()
        }

        waitFor { dropDownResults.size() > 0 }
    }

    def isOpen(){
        waitFor(3, {dropDownResults.size() > 0})
    }

    def openAndSelect(value){
        open()
        select(value)
    }

    def select(value){
        def dropdownItems = dropDownItem(value)
        waitFor { dropdownItems.size() > 0 }
        def item = dropdownItems.first()
        waitFor(3, { item.click() })
    }

    def selectFirst() {
        open()
        dropDownFirstItem.click()
        waitForDropDownMaskToGoAway()
    }

    def selectItem(itemValue) {
        open()
        select(itemValue)
    }

    def search(value){
        waitFor { searchBox }

        searchBox << value

        waitFor { dropDownItem(value) != null }
    }

    def searchAndSelect(value) {
        search(value)
        sleep(500)
        select(value)
    }

    def openSearchAndSelect(value) {
        open()
        searchAndSelect(value)
    }

    def searchAndSelectWithEnter(value){
        open()
        search(value)

        searchBox << Keys.ENTER
    }

    def search(SelectorOptions options){
        if(options.open){
            open()
        }

        search(options.searchValue)

        if(options.selectItem) {
            if (options.actionKey) {
                searchBox << options.actionKey.key()
            } else {
                select(options.searchValue)
            }
        }
    }

    def selectedValue() {
        dropDown.text()
    }

    def selectedValueEquals(value) {
        selectedValue() == value
    }

    def getOptions() {
        open()
        dropDownResults.children()
    }

    def optionsContains(value){
        def options = getOptions()
        for(def option : options){
            if(option.text().trim() == value){
                return true
            }
        }
        return false
    }

    def isDisabled() {
        dropDownTrigger.attr("aria-disabled") == "true"
    }

    def isEnabled() {
        !isDisabled()
    }

    def shiftTab(){

    }


}