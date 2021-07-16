package com.cardinalhealth.vitalpath.cabinet.modules.components
import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule
import com.cardinalhealth.vitalpath.common.modules.SelectorModule
import com.cardinalhealth.vitalpath.datasetup.InventorySegment
import com.cardinalhealth.vitalpath.utils.InventoryLocation
import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement

class InventoryAdjustmentModule extends BaseModule {

    def itemDataId = "adj-comp-prompt-for-product"

    def quantityLabel = "quantityLabel"
    def onHandQtyDataId = "adj-comp-on-hand-qty"
    def updatedQtyDataId = "adj-comp-updated-qty"
    def quantityInputName = "qtyWidget"
    def onHandQtyIconDataId = "adj-comp-on-hand-qtyIcon"
    def updatedQtyIconDataId = "adj-comp-updated-qtyIcon"

    def cancelButtonDataId = "adj-comp-cancel-button"
    def saveButtonDataId = "adj-comp-save-button"

    def minusButtonDataId = "minus-button"
    def plusButtonDataId = "plus-button"
    def drugItemSelectedName = "drug-item-selected-name"

    @Override
    String attr(String name) {
        return super.attr(name)
    }

    static content = {

        drugDropDown(wait:true) { module(new SelectorModule( selector: $("data-id": "adj-comp-prompt-for-product"))) }
        drugTypeDropDown(wait:true) { module(new SelectorModule( selector: $("data-id": "adj-comp-prompt-for-inv-segment"))) }
        invLocationDropDown(wait:true) { module(new SelectorModule( selector: $("data-id": "adj-comp-prompt-for-inv-location"))) }
        reasonCodeDropDown(wait:true) { module(new SelectorModule( selector: $("data-id": "adj-comp-prompt-for-reason-select"))) }

        expirationDateLabel(wait:true) { $('[data-id="exp-date-label"]') }
        expirationDateInput(wait:true) { $('[data-id="adj-comp-prompt-for-expiration"]').find("input") }
        lotNumberInput(wait:true) { $('[data-id="adj-comp-prompt-for-lot-number"]').find("input") }

        plusButton(wait:true) { $("data-id" : plusButtonDataId) }
        minusButton(wait:true) { $("data-id" : minusButtonDataId) }

        saveButton(wait:true) { $("data-id" : saveButtonDataId) }
        cancelButton(wait:true) { $("data-id" : cancelButtonDataId) }

        quantityInputBox(wait:true) { $(".quantityInput") }
        notesBox(wait:true) { $("[data-id=adjustment-notes]") }
    }


    def searchForDrug(value){
        drugDropDown.openSearchAndSelect(value)
    }

    def selectDrugType(InventorySegment inventorySegment) {
        drugTypeDropDown.openSearchAndSelect(inventorySegment.title())
        waitFor{ drugTypeDropDown.dropDownResults.displayed == false }
    }

    def openReasonDropdown(){
        reasonCodeDropDown.open()
    }

    def selectReasonCode(value) {
        reasonCodeDropDown.openSearchAndSelect(value)
        waitFor{ reasonCodeDropDown.dropDownResults.displayed == false }
    }

    def selectLocation(InventoryLocation location) {
        invLocationDropDown.openSearchAndSelect(location.locationName())
    }

    def selectLocation(Enum inventoryLocation) {
        invLocationDropDown.openSearchAndSelect(inventoryLocation.displayPath())
    }

    def inputLotNumber(String lotNumber){
        waitFor{lotNumberInput.isDisplayed()}
        waitFor{lotNumberInput.hasClass("isDisabled") == false}

        lotNumberInput.value(lotNumber)
    }

    def inputExpirationDate(String expDate){
        waitFor{expirationDateInput.isDisplayed()}
        waitFor{expirationDateInput.hasClass("isDisabled") == false}
        expirationDateInput.value(expDate)

        // The calendar will be open if we don't click outside of it, blocking other elements
        expirationDateLabel.click()
    }

    def clickCancelButton(){
        clickDataId(cancelButtonDataId)
        waitForAnimationToComplete()
    }

    def clickSaveButton(){
        waitFor{saveButton.hasClass("isDisabled") == false}
        //clickDataId(saveButtonDataId)
        saveButton.click()
        waitForAnimationToComplete()
    }

    def clickPlusButton(){
        clickDataId(plusButtonDataId)
    }

    def clickMinusButton(){
        clickDataId(minusButtonDataId)
    }

    def enterQuantity(Integer quantity){
        enterTextByClass("quantityInput", quantity.toString())
    }

    def clickTab(String className){
        WebElement webElement = browser.driver.switchTo().activeElement()
        webElement.sendKeys(Keys.TAB);
        true
    }

    def clickShiftTab(String className){
        WebElement webElement = browser.driver.switchTo().activeElement()
        webElement.sendKeys(Keys.SHIFT,Keys.TAB);
        true
    }

    def verifyQtyIsFocused(){
        quantityInputBox.isFocused()
    }

    def verifyNotesIsFocused(){
        notesBox.isFocused()
    }

    def verifyExpirationIsFocused(){
        expirationDateInput.isFocused()
    }

    def verifyLotIsFocused(){
        lotNumberInput.isFocused()
    }

    def verifyReasonCodeIsFocused(){
        reasonCodeDropDown.isOpen()
    }

    def verifyReason(String reasonCode){
        reasonCodeDropDown.isOpen()
        isNonEmptyNavigator($(".ember-power-select-option").find{ it.text().contains(reasonCode)})
    }

    def verifyInventorySegmentType(String type){
        drugTypeDropDown.selectedValueEquals(type)
    }

    def verifyInventorySegmentTypeIsDisabled(String type){
        drugTypeDropDown.isDisabled()
    }

}
