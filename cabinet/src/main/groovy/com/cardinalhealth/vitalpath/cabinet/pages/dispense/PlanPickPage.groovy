package com.cardinalhealth.vitalpath.cabinet.pages.dispense

import com.cardinalhealth.vitalpath.cabinet.modules.components.ApproverAuthenticationModule
import com.cardinalhealth.vitalpath.cabinet.modules.components.InventoryAdjustmentModule
import com.cardinalhealth.vitalpath.cabinet.modules.components.LotEditorModule
import com.cardinalhealth.vitalpath.cabinet.modules.components.PlanPickModule
import com.cardinalhealth.vitalpath.cabinet.modules.components.TreatmentDaysModule
import com.cardinalhealth.vitalpath.utils.InventoryLocation

class PlanPickPage extends BaseDispensePage {

    static url = "#/cabinet/dispense"
    static at = { waitFor { $("data-id" : "plan-pick") } }

    static content = {

        planPick { module(new PlanPickModule()) }
        lotEditor { module(new LotEditorModule())}
        wasteAdjustment { module(new InventoryAdjustmentModule())}
        approverAuthentication { module(new ApproverAuthenticationModule()) }
        treatmentDays(required: false) { module(new TreatmentDaysModule())}
    }

    def waitForCards(){
        waitFor{ $("[data-id='batchPickButton']").hasClass("isDisabled") == false }
    }

    def clickBackButton(){
        planPick.clickBackButton()
    }

    def clickUpdateButton(){
        planPick.clickUpdateButton()
    }

    def clickHomeButton(){
        planPick.clickHomeButton()
        waitForAnimationToComplete()
    }

    def clickConfirmAllButton(){
        waitForAnimationToComplete()
        planPick.clickConfirmAllButton()
        waitForAnimationToComplete()
    }

    def clickApproverCancelButton(){
        waitForAnimationToComplete()
        approverAuthentication.clickCancelButton()
        waitForAnimationToComplete()
    }

    def isApproverAuthenticationShown(){
        waitForAnimationToComplete()
        approverAuthentication.isShown()
        waitForAnimationToComplete()
    }

    def enterApproval(id, pwd){
        waitForAnimationToComplete()
        approverAuthentication.enterApproval(id, pwd)
        waitForAnimationToComplete()
        return true
    }

    def findDoorCardByName(name){
        planPick.findDoorCardByName(name)
    }

    def clickDoorCard(InventoryLocation invLoc){
        planPick.findDoorCard(invLoc).click()
    }

    def clickDoorCardByName(name){
        planPick.findDoorCardByName(name).click()
    }

    def clickSkipPatientButton(){
        waitForAnimationToComplete()
        planPick.clickSkipPatientButton()
    }

    def skipUntilPatientFound(mrn){
        planPick.skipUntilPatientFound(mrn)
        return true
    }

    def clickExitPlanPickButton(){
        waitForAnimationToComplete()
        planPick.clickExitPlanPickButton()
    }

    def clickReopenButton(){
        waitForAnimationToComplete()
        planPick.clickReopenButton()
    }

    def cancelFirstPlanPickItem(){
        planPick.cancelFirstPlanPickItem()
    }

    def confirmFirstPlanPickItem(){
        planPick.confirmFirstPlanPickItem()
    }

    def confirmPlanPickItemByNdc(text){
        planPick.confirmPlanPickItemByText(text)
        waitForAnimationToComplete()
        return true
    }

    def findInventoryCardByName(name){
        planPick.findInventoryCardByName(name)
    }

    def verifyBatchPickHeader(){
        waitForAnimationToComplete()
        planPick.verifyBatchPickHeader()
    }

    def verifyPatientNameMrnInHeader(value){
        waitForAnimationToComplete()
        planPick.verifyPatientNameMrnInHeader(value)
    }

    def verifyPickItemExistByNdc(value){
        waitForAnimationToComplete()
        planPick.verifyPickItemExistByNdc(value)
    }

    def verifyPickItemOnHandQuantity(textValue, onHandQantity){
        waitForAnimationToComplete()
        planPick.verifyPickItemOnHandQuantity(textValue, onHandQantity)
    }

    def verifyPickItemDispenseDetailLotNumber(ndc, String lotNumber){
        waitForAnimationToComplete()
        planPick.verifyPickItemDispenseDetailLotNumber(ndc, lotNumber)
    }

    def verifyDispenseDetailExpirationDate(ndc, expires){
        waitForAnimationToComplete()
        planPick.verifyDispenseDetailExpirationDate(ndc, expires)
    }

    def verifyPickItemPlannedQuantity(ndc, plannedQty){
        planPick.verifyPickItemPlannedQuantity(ndc, plannedQty)
    }

    def verifyNumberOfPlanPickItems(count){
        waitFor{planPick.numberOfPlanPickItems() == count}
    }

    def enterSingleLotNumber(value){
        def items = planPick.getPlanPickItems()
        if(items.size() > 0){
            items.first().clickLotNumberButton()
            waitForAnimationToComplete()
            def valid = lotEditor.enterSingleLotNumber(value)
            if(valid == true){
                lotEditor.clickSaveAndConfirmButton()
            }
        }

        return true
    }

    def saveWasteAdjustment(){
        waitForAnimationToComplete()
        wasteAdjustment.clickSaveButton()
        waitForAnimationToComplete()
    }

    def clickUpdateInventoryPlusButton(){
        waitForAnimationToComplete()
        wasteAdjustment.clickPlusButton()
    }

    def selectUpdateInventoryReasonCode(reason){
        waitForAnimationToComplete()
        wasteAdjustment.selectReasonCode(reason)
    }

    def inputInventoryLotNumber(String lotNumber){
        waitForAnimationToComplete()
        wasteAdjustment.inputLotNumber(lotNumber)
    }

    def inputInventoryExpires(String expires){
        waitForAnimationToComplete()
        wasteAdjustment.inputExpirationDate(expires)
    }

}
