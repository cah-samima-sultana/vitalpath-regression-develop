package com.cardinalhealth.vitalpath.cabinet.modules.components

import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule
import com.cardinalhealth.vitalpath.common.modules.PlanItemModule

class MedicationOrderPlanModule extends BaseModule {


    static content = {

        wrapper(wait: true) { $("id": "medicationOrderComponent") }

        orderLineContainer(required: false) { $("data-id": "order-line-container") }
        orderLinesUpScroll(required: false) { orderLineContainer.find(".scrollBar.isUp") }
        orderLinesDownScroll(required: false) { orderLineContainer.find(".scrollBar.isDown") }

        planItemContainer(required: false) { $("data-id": "plan-item-container") }

        planItemScrollContainer(required: false) { planItemContainer.find(SCROLL_CONTAINER_CLASS) }

        confirmWithReason { module(new ConfirmWithReasonModule()) }

        medicationOrderLineMaint { module(new MedicationOrderLineMaintModule()) }

        emrFixModule(required: false) { planItemContainer.find("[data-id=clinical-order-line-source-maint]").module(EMRFixModule.class) }
    }

    def clickAddLineButton() {
        clickDataId('add-button')
    }

    def clickEditLineButton() {
        clickDataId('edit-clinical-order-line-button')
        waitForAnimationToComplete()
    }

    def clickRemoveLineButton() {
        clickDataId('remove-line-button')
    }

    def clickDeleteClinicalOrderButton() {
        clickDataId('delete-clinical-order-button')
    }

    def clickPlanPickButton() {
        clickDataId('pick-button')
    }

    def clickPickReturnButton() {
        clickDataId('return-button')
    }

    def clickPickWasteButton() {
        clickDataId('waste-button')
    }

    def clickReturnFilter() {
        clickDataId('returnfilter')
    }

    def clickWasteFilter() {
        clickDataId('wastefilter')
    }

    def clickPlanFilter() {
        clickDataId("planfilter")
    }


    def clickHomeButton() {
        clickDataId('home-button')
    }

    def clickCloneButton() {
        clickDataId('clone-clinical-order-button')
    }

    def clickLabelButton() {
        clickDataId('label-button')
    }

    def clickDispenseReceiptButton() {
        clickDataId('receipt-button')
    }

    def clickConfirmWithReasonButton(){
        confirmWithReason.clickConfirmButton()
    }

    def clickCancelWithReasonButton(){
        confirmWithReason.clickCancelButton()
    }

    def ArrayList<DispenseOrderLineCardModule> getPlanLines(){
        getModules(orderLineContainer, ".${DispenseOrderLineCardModule.DISPENSE_ORDER_LINE_CARD_CLASS}", DispenseOrderLineCardModule.class)
    }

    def selectPlanLine(Integer position){
        def planLine = getPlanLine(position)
        try {
            planLine.click()
        } catch (Exception e) {
            if (e.message.contains("is not clickable at point")) {
                def tapToClose = $("data-id": "tap-to-close")
                tapToClose.click()
                waitFor { $('data-id': 'tap-to-close') == [] }
                planLine.click()
            } else {
                throw e
            }
        }
    }

    def Integer getPlanLineCount(){
        getPlanLines().size()
    }

    def verifyPlanLineCount(Integer count){
        def Integer plCount = getPlanLineCount()
        assert plCount.equals(count) : "Plan Line Count ${plCount} does not equal the expected count of ${count}"
        true
    }

    def DispenseOrderLineCardModule getPlanLine(Integer position){
        def line = null
        def items = getPlanLines()
        if(items.size() > 0){
            line = items[position - 1]
        }

        return line
    }

    def enterNewLineDetails(drugFamily, quantity, route){
        medicationOrderLineMaint.addDrugFamilyLine(drugFamily, quantity, route)
        medicationOrderLineMaint.clickAddLineDoneButton()
    }

    def editDoseOnSelectedLine(dose){
        waitForAnimationToComplete()
        medicationOrderLineMaint.enterQuantity(dose)
        medicationOrderLineMaint.saveLineAndClose()
    }


    def verifyLineDose(Integer position, value){
        def line = getPlanLine(position)
        if(line != null){
            return line.verifyPrescribedQty(value)
        }

        return false
    }

    def verifyLineDispensedQty(Integer position, value){
        def line = getPlanLine(position)
        if(line != null){
            return line.verifyDispensedQty(value)
        }

        return false
    }

    def verifyLinePlannedQty(Integer position, value){
        def line = getPlanLine(position)
        if(line != null){
            return line.verifyPlannedQty(value)
        }

        return false

    }

    def verifyLineEstimatedWasteQty(Integer position, value){
        def line = getPlanLine(position)
        if(line != null){
            return line.verifyEstimatedWasteQty(value)
        }

        return false
    }

    def isLineComplete(Integer position){
        def line = getPlanLine(position)
        if(line != null){
            return line.isComplete()
        }

        return false
    }

    def isLineNotStarted(Integer position){
        def line = getPlanLine(position)
        if(line != null){
            return line.isNotStarted()
        }

        return false
    }

    def isLinePlanned(Integer position){
        def line = getPlanLine(position)
        if(line != null){
            return line.isPlanned()
        }

        return false
    }

    def verifyPlanLineIsSelected(Integer position){
        def line = getPlanLine(position)
        if(line != null){
            return line.isSelected()
        }

        return false
    }

    def ArrayList<PlanItemModule> getPlanItems(){
        getModules(planItemScrollContainer, ".${PlanItemModule.PLAN_ITEM_CARD_CLASS}", PlanItemModule.class)
    }


    def Integer getPlanItemCount(){
        getPlanItems().size()
    }

    def verifyPlanItemCount(Integer count){
        def Integer piCount = getPlanItemCount()
        assert piCount.equals(count) : "Plan Line Count ${piCount} does not equal the expected count of ${count}"
        true
    }

    def PlanItemModule getPlanItem(Integer position){
        def line = null
        def items = getPlanItems()
        if(items.size() > 0){
            line = items[position - 1]
        }

        return line
    }

    def planPlanItem(Integer itemPosition){
        getPlanItem(itemPosition).clickPlusButton()
    }

    def verifyPlanItemItemName(Integer position, value){
        def planItem = getPlanItem(position)
        if(planItem != null){
            return planItem.verifyItemName(value)
        }

        return false
    }

    def verifyPlanItemInventoryLocationName(Integer position, value){
        def planItem = getPlanItem(position)
        if(planItem != null){
            return planItem.verifyInventoryLocationName(value)
        }

        return false
    }

    def verifyPlanItemNdc(Integer position, value){
        def planItem = getPlanItem(position)
        if(planItem != null){
            return planItem.verifyNdc(value)
        }

        return false
    }

    def verifyPlanItemInventorySegment(Integer position, value){
        def planItem = getPlanItem(position)
        if(planItem != null){
            return planItem.verifyInventorySegment(value)
        }

        return false
    }


    def verifyOrderLineContainerUpScrollIsDisabled(){
       orderLinesUpScroll.hasClass("isDisabled")
    }

    def verifyOrderLineContainerUpScrollIsEnabled(){
        orderLinesUpScroll.hasClass("isDisabled") == false
    }

    def verifyOrderLineContainerDownScrollIsDisabled(){
       orderLinesDownScroll.hasClass("isDisabled")
    }
    
    def verifyOrderLineContainerDownScrollIsEnabled(){
        orderLinesDownScroll.hasClass("isDisabled") == false
    }

    def clickOrderLineContainerDownScroll(){
        orderLinesDownScroll.click()
    }

    def getEMRFixModule(){
        planItemContainer.module(EMRFixModule.class)
    }
}
