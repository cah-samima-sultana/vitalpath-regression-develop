package com.cardinalhealth.vitalpath.cabinet.pages.dispense

import com.cardinalhealth.vitalpath.cabinet.modules.components.MedicationOrderLineMaintModule
import com.cardinalhealth.vitalpath.cabinet.modules.components.MedicationOrderPlanModule
import com.cardinalhealth.vitalpath.cabinet.modules.components.SuggestedDispenseModule
import com.cardinalhealth.vitalpath.datasetup.ItemConstants
import com.cardinalhealth.vitalpath.utils.Item
import com.cardinalhealth.vitalpath.datasetup.InventorySegment
import com.cardinalhealth.vitalpath.utils.InventoryLocation

class DispensePlanPage extends BaseDispensePage {

    static url = "#/cabinet/dispense"
    static at = { waitFor { $("id" : "medicationOrderComponent") } }

    static String suggestedDispenseDataId = "[data-id=suggested-dispense]"

    static content = {
        medicationOrderPlan { module(new MedicationOrderPlanModule()) }
        medicationOrderLineMaint(required:false, wait:true) { module(new MedicationOrderLineMaintModule()) }
        labelButton { $("[data-id=label-button]") }
    }

    def isDefaultPlanMode(){
        waitFor(3, {$(suggestedDispenseDataId).size() == 0} )
    }

    def isSuggestedDispenseMode(){
        waitFor(3, {$(suggestedDispenseDataId).size() > 0} )
    }

    def getSuggestedDispenseLineByIndex(int index){
        $(suggestedDispenseDataId).find(".suggested-dispense").eq(index).module(SuggestedDispenseModule)
    }

    def clickLabelButton(){
        waitFor(3, {labelButton.click()})
    }

    def clickPlanPickButton(){
        waitForAnimationToComplete()
        medicationOrderPlan.clickPlanPickButton()
    }

    def clickPickReturnButton() {
        waitForAnimationToComplete()
        medicationOrderPlan.clickPickReturnButton()
    }

    def clickPickWasteButton() {
        waitForAnimationToComplete()
        medicationOrderPlan.clickPickWasteButton()
    }

    def clickHomeButton(){
        waitForAnimationToComplete()
        medicationOrderPlan.clickHomeButton()
    }

    def clickAddLineButton(){
        medicationOrderPlan.clickAddLineButton()
    }

    def closeBottomShelf() {
        clickDataId("tap-to-close")
        waitForAnimationToComplete()
    }

    def enterNewLineDetails(drugFamily, quantity, route){
        medicationOrderPlan.enterNewLineDetails(drugFamily, quantity, route)
    }

    def clickEditLineButton() {
        waitForAnimationToComplete()
        medicationOrderPlan.clickEditLineButton()
    }

    def clickRemoveLineButton(){
        waitForAnimationToComplete()
        medicationOrderPlan.clickRemoveLineButton()
    }

    def clickDeleteClinicalOrderButton(){
        waitForAnimationToComplete()
        medicationOrderPlan.clickDeleteClinicalOrderButton()
    }

    def clickConfirmWithReasonButton(){
        waitForAnimationToComplete()
        medicationOrderPlan.clickConfirmWithReasonButton()
    }

    def clickCancelWithReasonButton(){
        waitForAnimationToComplete()
        medicationOrderPlan.clickCancelWithReasonButton()
    }

    def planItemByLocationNdc(location, ndc, howMany){
        def planItems = medicationOrderPlan.getPlanItems();
        def ndcItem
        for(planItem in planItems){
            if(planItem.isAtLocation(location) && planItem.isNdcItem(ndc)){
                ndcItem = planItem
                break
            }
        }

        for(i in howMany){
            ndcItem.clickPlusButton()
        }

        return true
    }

    def planAllLines() {

        medicationOrderPlan.planLines.each { planLine ->
            planLine.click()

            medicationOrderPlan.planItems.each {
                item -> item.clickPlusButton()
            }
        }
    }

    def planFirstLine(){
        medicationOrderPlan.planPlanItem(1)
    }

    def getPlanItems(){
        medicationOrderPlan.getPlanItems()
    }

    def clickPlanFilter(){
        waitForAnimationToComplete()
        medicationOrderPlan.clickPlanFilter()
    }

    def clickReturnFilter(){
        waitForAnimationToComplete()
        medicationOrderPlan.clickReturnFilter()
    }

    def clickWasteFilter(){
        waitForAnimationToComplete()
        medicationOrderPlan.clickWasteFilter()
    }

    def isLineComplete(Integer position){
        medicationOrderPlan.isLineComplete(position)
    }

    def isLineNotStarted(Integer position){
        medicationOrderPlan.isLineNotStarted(position)
    }

    def isLinePlanned(Integer position){
        medicationOrderPlan.isLinePlanned(position)
    }

    def editDoseOnSelectedLine(dose){
        medicationOrderPlan.editDoseOnSelectedLine(dose)
    }

    def verifyLineDose(Integer position, dose){
        waitForAnimationToComplete()
        medicationOrderPlan.verifyLineDose(position, dose)
    }

    def verifyLineDispensedQty(Integer position, qty){
        waitForAnimationToComplete()
        medicationOrderPlan.verifyLineDispensedQty(position, qty)
    }

    def verifyLinePlannedQty(Integer position, qty){
        waitForAnimationToComplete()
        medicationOrderPlan.verifyLinePlannedQty(position, qty)
    }

    def verifyLineEstimatedWasteQty(Integer position, qty){
        waitForAnimationToComplete()
        medicationOrderPlan.verifyLineEstimatedWasteQty(position, qty)
    }

    def verifyLineQuantities(Integer position, Integer dose, Integer dispensed, Integer planned, Integer estimatedWaste, String uom ){

        medicationOrderPlan.verifyLineDose(position, "${dose} ${uom}")
        medicationOrderPlan.verifyLineDispensedQty(position, "${dispensed} ${uom}")
        medicationOrderPlan.verifyLinePlannedQty(position, "${planned} ${uom}")
        medicationOrderPlan.verifyLineEstimatedWasteQty(position, "${estimatedWaste} ${uom}")
    }

    def verifyPlanLineIsSelected(Integer position){
        medicationOrderPlan.verifyPlanLineIsSelected(position)
    }

    def verifyPlanLineCount(Integer count){
        medicationOrderPlan.verifyPlanLineCount(count)
    }

    def verifyPlanItemCount(Integer count){
        medicationOrderPlan.verifyPlanItemCount(count)
    }

    def selectPlanLine(Integer position){
        medicationOrderPlan.selectPlanLine(position)
    }

    def verifyPlanItem(Integer position,  Item item, InventoryLocation inventoryLocation, InventorySegment inventorySegment){

        medicationOrderPlan.verifyPlanItemItemName(position, "${item.itemName()}")
        medicationOrderPlan.verifyPlanItemInventoryLocationName(position, "${inventoryLocation.locationName()}")
        medicationOrderPlan.verifyPlanItemNdc(position, "${item.drugId()}")
        medicationOrderPlan.verifyPlanItemInventorySegment(position, "${inventorySegment.title()}")

    }

    def verifyPlanItem(position, ItemConstants item, String inventoryLocation, InventorySegment inventorySegment){
        medicationOrderPlan.verifyPlanItemItemName(position, item.getName())
        medicationOrderPlan.verifyPlanItemInventoryLocationName(position, inventoryLocation)
        medicationOrderPlan.verifyPlanItemNdc(position, item.getNdc())
        medicationOrderPlan.verifyPlanItemInventorySegment(position, inventorySegment.title())

    }

    def verifyOrderLineContainerUpScrollIsDisabled(){
        medicationOrderPlan.verifyOrderLineContainerUpScrollIsDisabled()
    }

    def verifyOrderLineContainerDownScrollIsDisabled(){
        medicationOrderPlan.verifyOrderLineContainerDownScrollIsDisabled()
    }

    def verifyOrderLineContainerUpScrollIsEnabled(){
        medicationOrderPlan.verifyOrderLineContainerUpScrollIsEnabled()
    }

    def verifyOrderLineContainerDownScrollIsEnabled(){
        medicationOrderPlan.verifyOrderLineContainerDownScrollIsEnabled()
    }

    def clickOrderLineContainerDownScroll(){
        medicationOrderPlan.clickOrderLineContainerDownScroll()
    }

    def enterQuantityOnEditDoseAndSave(int quantity){
        waitFor({medicationOrderLineMaint.displayed})
        medicationOrderLineMaint.enterQuantity(quantity)
    }

    def saveAndCloseEditDose(){
        waitForAnimationToComplete()
        medicationOrderLineMaint.saveLineAndClose()
    }

    def verifyEditDisabled(){
        return $("[data-id=edit-clinical-order-line-button]").hasClass("isDisabled")
    }

    def verifyEditEnabled(){
        return !$("[data-id=edit-clinical-order-line-button]").hasClass("isDisabled")
    }

}
