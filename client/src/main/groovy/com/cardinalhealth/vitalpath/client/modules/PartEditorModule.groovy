package com.cardinalhealth.vitalpath.client.modules

import com.cardinalhealth.vitalpath.common.modules.Select2Module
import com.cardinalhealth.vitalpath.common.modules.SelectorModule

class PartEditorModule extends BaseModule {

    static content = {
        wrapper(wait: true) { $(".part-modal") }

        filterDropdown(required: true, wait: true) { module(new Select2Module(selectId: "s2id_searchBoxFilterField")) }
        filterSearch(required: true, wait: true) { $("#searchBoxTextField") }

        headerBar { $(".selectable-grid .headerBar") }
        columnRows {  $("[data-id='column-row']") }

        distributorSelector { module(new SelectorModule(selector: $('data-id': 'vendor-selector'))) }
        partNumberInput { $('data-id': 'modal-part-number').find("input") }
        priceInput {$('data-id': 'modal-order-cost').find("input")}

        closeButton { $('data-id': 'close-button' )}

        backButton { $('data-id': 'modal-back') }
        saveButton { $('data-id': 'modal-save') }
        continueButton { $('data-id': 'continue')}
    }

    def selectFilterNdc(){
        filterDropdown.openAndSelect("NDC")
    }

    def search(text){
        filterSearch << text
    }

    def verifyColumnTitles(){
        headerBar.find(".column-title").getAt(0).text() == "NDC"
        headerBar.find(".column-title").getAt(1).text() == "DRUG NAME"
        headerBar.find(".column-title").getAt(2).text() == "FORM"
    }

    def verifySort(index, sort){
        headerBar.find(".column-header").getAt(index).find(".column-indicator .sort").hasClass(sort) == true
    }

    def verifyRowCount(rowCount){
       columnRows.size() == rowCount
    }

    def inputPartNumber(partNumber){
        partNumberInput << partNumber
    }

    def inputPrice(price){
        priceInput << price
    }

    def clickBack(){
        backButton.click()
    }

    def clickSave(){
        saveButton.click()
    }

    def clickContinue(){
        continueButton.click()
    }

    def clickClose(){
        closeButton.click()
    }

    def verifyContinueIsEnabled(){
        waitFor { continueButton.hasClass('isDisabled') == false }
    }

    def isInvalidPartNumber(){
        $(".fuse-exclamation-circle").size() == 1
        $("[data-id='invalid-part-text']").size() == 1
        saveButton.hasClass("isDisabled") == true
    }
}