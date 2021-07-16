package com.cardinalhealth.vitalpath.cabinet.modules.components

import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule
import com.cardinalhealth.vitalpath.utils.InventoryLocation


class PlanPickModule extends BaseModule {

    final static String DOOR_CARD_CLASS = "cabinetCard"
    final static String INVENTORY_CARD_CLASS = "planPickItemCard"

    static content = {
        wrapper(wait: true) { dataId("plan-pick") }
        scrollContainer(wait:true) { $(SCROLL_CONTAINER_CLASS) }

        planPickItemScrollContainer(required: false) { wrapper.find(SCROLL_CONTAINER_CLASS) }
        lotEditor { module(new LotEditorModule())}


    }

    def cardByText(textValue, className) {
        waitFor { $(".${className}", text: iContains(textValue) ) }
        $(".${className}", text: iContains(textValue) )
    }

    def clickBackButton() {
        waitFor{ $("#backButton").size() > 0 }
        clickId("backButton")
    }

    def clickHomeButton() {
        clickId("homeButton")
    }

    def clickUpdateButton() {
        clickName("editButton")
    }

    def clickConfirmAllButton() {
        clickId("confirmAllButton")
    }

    def clickSkipPatientButton() {
        clickDataId("skip-button")
    }

    def clickExitPlanPickButton() {
        clickDataId("exit-button")
    }

    def clickReopenButton() {
        clickId("doorButton")
    }

    def findDoorCard(InventoryLocation invLoc){
        findDoorCardByName(invLoc.locationName())
    }

    def findDoorCardByName(textValue){
        waitFor { cardByText(textValue, DOOR_CARD_CLASS) }
    }

    def findInventoryCardByName(textValue){
        waitFor { cardByText(textValue, INVENTORY_CARD_CLASS) }
    }

    def getPlanPickItems(){
        getModules(scrollContainer, ".${PlanPickItemModule.PLAN_PICK_ITEM_CARD_CLASS}", PlanPickItemModule.class)
    }

    def cancelFirstPlanPickItem(){
        def items = getPlanPickItems()
        if(items.size() > 0){
            items.first().clickCancelButton()
        }
    }

    def numberOfPlanPickItems(){
        def items = getPlanPickItems()
        return items.size()
    }

    def confirmFirstPlanPickItem(){
        def items = getPlanPickItems()
        if(items.size() > 0){
            items.first().clickConfirmButton()
        }
    }

    def confirmPlanPickItemByText(textValue){
        def items = getPlanPickItems()
        for(item in items){
            if(item.findPickItemByNdc(textValue).size() > 0){
                item.clickConfirmButton()
            }
        }

    }

    def  verifyPickItemExistByNdc(textValue){
        def items = getPlanPickItems()
        for(item in items){
            if(item.findPickItemByNdc(textValue).size() > 0){
               return true
            }
        }
        return false
    }

    def  verifyPickItemOnHandQuantity(textValue, onHandQty){
        def items = getPlanPickItems()
        for(item in items){
            if(item.findPickItemByNdc(textValue).size() > 0){
                assert(item.findPickItemByHandQuantity(onHandQty).size() > 0)
                return true
            }
        }
        assert false
    }

    def verifyPickItemPlannedQuantity(ndc, plannedQty){
        def items = getPlanPickItems()
        for(item in items){
            if(item.findPickItemByNdc(ndc).size() > 0){
                item.verifyPlannedQuantity(plannedQty).size() > 0
                return true
            }
        }
        assert false
    }


    def  verifyPickItemDispenseDetailLotNumber(ndc, lotNumber){
        def items = getPlanPickItems()
        for(item in items){
            if(item.findPickItemByNdc(ndc).size() > 0){
                assert(item.verifyDispenseDetailLotNumber(lotNumber))
                return true
            }
        }
        assert false
    }

    def  verifyDispenseDetailExpirationDate(ndc, expires){
        def items = getPlanPickItems()
        for(item in items){
            if(item.findPickItemByNdc(ndc).size() > 0){
                assert(item.verifyDispenseDetailExpirationDate(expires))
                return true
            }
        }
        assert false
    }

    def verifyBatchPickHeader(){
        assertTextContainsByDataId("batch-pick-header", "Batch Pick")
    }

    def verifyPatientNameMrnInHeader(value) {
        assertTextContainsByDataId("patient-full-name-mrn", value)
    }

    def skipUntilPatientFound(patientMRN){
        def patientNameMRN = findElement("data-id", "patient-full-name-mrn").text().toString()

        if(patientNameMRN.contains(patientMRN) == false){
            waitForAnimationToComplete()
            clickSkipPatientButton()
            waitFor{ $(".planPickItemCard").size() > 0 }
            skipUntilPatientFound(patientMRN)
        }
    }

}

