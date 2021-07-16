package com.cardinalhealth.vitalpath.common.modules

import org.openqa.selenium.Keys

class Select2Module extends CommonBaseModule  {

    static content = {
        dropDown(required:false, wait: 1) {
            if(dataId) {
                return $("div", "data-id" : dataId)
            }

            return $("div", id: selectId)
        }
        searchBox(wait: 1, required: false) { $('#select2-drop .select2-input') }
        dropDownResults(required:false, wait: 1) { $(".select2-results li")}
        dropDownFirstItem(wait:true) { dropDownResults.firstElement() }
        dropDownItem(wait: true) { itemValue ->
            $('.select2-result-label', text: iContains(itemValue) )
        }
    }

    def selectId = ""
    def dataId = ""

    def open(){
        if(dropDown.displayed && dropDownResults.displayed == false){
            dropDown.click()
        }
        waitFor { dropDownResults.size() > 0 }
    }

    def isOpen(){
        if(dropDown.displayed && dropDownResults.size() > 0){
            true
        }
        false
    }
    def close(){
        if(searchBox.displayed){
            waitFor { searchBox }
            searchBox << Keys.ESCAPE
        }

        return true;
    }

    def openAndSelect(value){
        open()
        select(value)
    }

    def select(value){
        waitFor { dropDownItem(value) }
        dropDownItem(value).first().click()
        waitForDropDownMaskToGoAway()
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
    }

    def searchAndSelect(value) {
        search(value)
        select(value)
    }

    def openSearchAndSelect(value) {
        open()
        search(value)
        select(value)
    }

    def fetchFirstElementText(){
        if(isOpen() == false){
            open()
        }
        dropDownResults.firstElement().getText()
    }

    def fetchLastElementText(){
        if(isOpen() == false){
            open()
        }
        dropDownResults.lastElement().getText()
    }
}