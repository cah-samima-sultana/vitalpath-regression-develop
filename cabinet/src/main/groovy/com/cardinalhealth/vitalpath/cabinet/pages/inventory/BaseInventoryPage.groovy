package com.cardinalhealth.vitalpath.cabinet.pages.inventory
import com.cardinalhealth.vitalpath.cabinet.modules.components.ApproverAuthenticationModule
import com.cardinalhealth.vitalpath.cabinet.modules.components.ConfirmModule
import com.cardinalhealth.vitalpath.cabinet.modules.components.EditInventoryCardModule
import com.cardinalhealth.vitalpath.cabinet.modules.components.InfoInventoryModule
import com.cardinalhealth.vitalpath.cabinet.pages.BasePage
import com.cardinalhealth.vitalpath.common.modules.Select2Module
import com.cardinalhealth.vitalpath.datasetup.InventorySegment
import com.cardinalhealth.vitalpath.datasetup.ItemConstants
import com.cardinalhealth.vitalpath.utils.InventoryLocation
import com.cardinalhealth.vitalpath.utils.InventorySearchType
import com.cardinalhealth.vitalpath.utils.Item
import geb.navigator.Navigator
import geb.waiting.WaitTimeoutException

class BaseInventoryPage extends BasePage {
    static content = {
        searchByTypeDropDown { module(new Select2Module(selectId: "s2id_searchFilterField")) }
        searchTextInputBox(wait: true) { $("#searchTextField") }

        confirmComponent { module(new ConfirmModule())}
        approverAuthentication { module(new ApproverAuthenticationModule()) }

    }

    def clickAddButton(){
        clickId("addButton")
        waitForAnimationToComplete()
    }

    def clickPatientButton(){
        waitForAnimationToComplete()
        clickId("patientButton")
    }

    def clickAddPatientMedicationButton(){
        clickId("addPatientMedicationButton")
    }

    def clickCycleCountButton(){
        clickId("cycleCountButton")
    }

    def clickTransferButton(){
        clickId("transferButton")
    }

    def clickPutAwayButton(){
        clickId("putAwayButton")
    }

    def clickWasteMultiButton(){
        clickId("wasteMultiButton")
    }

    def clickRestockButton(){
        clickId("restockButton")
    }

    def searchInventory(searchType, searchText) {
        searchByTypeDropDown.selectItem(searchType.title())
        waitForDropDownMaskToGoAway()

        searchTextInputBox.value("")
        searchTextInputBox << searchText
    }

    def clickOverlayText(){
        def overlay = $('#select2-drop-mask');
        waitFor { overlay }
        waitFor { overlay.click() }
        waitForAnimationToComplete()
    }

    def addSiteOwnedDrug(drug, quantity, location){
        clickAddButton()
        addDrug(drug, InventorySegment.SITE_OWNED, quantity, location)
    }

    def addPatientDrug(ItemConstants item, InventorySegment inventorySegment, quantity, Enum inventoryLocation, Boolean isControlledSubstance){
        clickAddPatientMedicationButton()
        addDrug(item, inventorySegment, quantity, inventoryLocation, isControlledSubstance)
    }

    def addPatientDrug(Item item, InventorySegment inventorySegment, quantity, InventoryLocation inventoryLocation,Boolean isControlledSubstance){
        clickAddPatientMedicationButton()
        addDrug(item, inventorySegment, quantity, inventoryLocation, isControlledSubstance)
    }

    def searchForDrug(value){
        adjustment.searchForDrug(value)
        waitForAnimationToComplete()
    }

    def searchForLocation(Enum inventoryLocation){
        adjustment.selectLocation(inventoryLocation)
    }

    def addDrug(ItemConstants item, InventorySegment inventorySegment, quantity, Enum inventoryLocation, Boolean isControlledSubstance){
        waitForAnimationToComplete()
        adjustment.searchForDrug(item.getNdc())
        adjustment.selectDrugType(inventorySegment)

        waitForAnimationToComplete()
        waitForAnimationToComplete()

        adjustment.selectLocation(inventoryLocation)

        adjustment.selectReasonCode("Restock Cardinal")
        waitForAnimationToComplete()
        if(isControlledSubstance){
            approverAuthentication.enterApproval(LOGIN_ID_APPROVER, LOGIN_PWD_APPROVER)
            waitForAnimationToComplete()
        }

        adjustment.enterQuantity(quantity)
        adjustment.clickSaveButton()
    }

    def addDrug(Item item, InventorySegment inventorySegment, quantity, InventoryLocation inventoryLocation, Boolean isControlledSubstance){
        waitForAnimationToComplete()
        adjustment.searchForDrug(item.drugId())
        adjustment.selectDrugType(inventorySegment)
        adjustment.selectLocation(inventoryLocation)

        waitForAnimationToComplete()
        if(isControlledSubstance){
            approverAuthentication.enterApproval(LOGIN_ID_APPROVER, LOGIN_PWD_APPROVER)
            waitForAnimationToComplete()
        }

        adjustment.enterQuantity(quantity)
        adjustment.clickSaveButton()
    }


    def verifyInventoryCard(Item item, InventorySegment inventorySegment, InventoryLocation inventoryLocation, quantity, String iconClass){
        searchInventory(InventorySearchType.NDC, item.drugId())

        def InfoInventoryModule infoInv = getInventoryEditCard(item, inventoryLocation, inventorySegment).infoInventory()

        infoInv.verifyInventoryLocationName(inventoryLocation.locationName())
        infoInv.verifyNdc(item.drugId())
        infoInv.verifyItemName(item.drugName())
        infoInv.verifyInventoryIcon(item.iconClass())
        infoInv.verifyInventorySegment(inventorySegment.title())
        return true
    }

    def addVerifyInventory(Item item, InventorySegment inventorySegment, InventoryLocation inventoryLocation, quantity, Boolean isControlledSubstance) {
        addDrug(item, inventorySegment, quantity, inventoryLocation, isControlledSubstance)
        verifyInventoryCard(item, inventorySegment, inventoryLocation, quantity, item.iconClass())
    }
    def addVerifyDeleteInventory(Item item, InventorySegment inventorySegment, InventoryLocation inventoryLocation, quantity, Boolean isControlledSubstance){
        addVerifyInventory(item, inventorySegment, inventoryLocation, quantity, isControlledSubstance)
        removeExistingInventory(item, inventorySegment, inventoryLocation, isControlledSubstance)
    }

    def validateAdjustment(Item item, String onHandQty, String updatedQty, String inputQty, String onHandContainerQty, String updatedContainerQty){
        adjustment.assertTextContainsByDataId(adjustment.ndcDataId, item.drugId())
        adjustment.assertTextContainsByDataId(adjustment.taiDataId, "${item.tai()} ${item.uom()}")
        adjustment.assertTextContainsByDataId(adjustment.onHandQtyDataId, "${onHandQty} ${item.uom()}")
        adjustment.assertTextContainsByDataId(adjustment.updatedQtyDataId, "${updatedQty} ${item.uom()}")
        adjustment.assertInputValueByName(adjustment.quantityInputName, "${inputQty}")
        adjustment.assertTextContainsByDataId(adjustment.onHandQtyIconDataId, "${onHandContainerQty}")
        adjustment.assertTextContainsByDataId(adjustment.updatedQtyIconDataId, "${updatedContainerQty}")

    }

    def clickAdjustmentCancelButton(){
        adjustment.clickCancelButton()
        waitForAnimationToComplete()
    }

    def clickAdjustmentSaveButton(){
        adjustment.clickSaveButton()
        waitForAnimationToComplete()
    }

    def clickAdjustmentPlusButton(){
        adjustment.clickPlusButton()
    }

    def clickAdjustmentMinusButton(){
        adjustment.clickMinusButton()
    }

    def clickAdjustmentOverlay(){
        clickDataId("adjustmentOffCanvas")
        waitForAnimationToComplete(2500)
    }

    def Navigator getEditInventoryCard(ItemConstants item, inventoryLocation, InventorySegment inventorySegment){
        return waitFor(3) { $(".${EditInventoryCardModule.MODULE_CLASS}").filter(text: iContains(item.ndc)).filter(text: iContains(inventoryLocation)).filter(text: iContains(inventorySegment.title())) }
    }

    def Navigator getEditInventoryCard(ItemConstants item, InventorySegment inventorySegment){
        return waitFor(3) { $(".${EditInventoryCardModule.MODULE_CLASS}").filter(text: iContains(item.ndc)).filter(text: iContains(inventorySegment.title())) }
    }

    def EditInventoryCardModule getInventoryEditCard(ItemConstants item, inventoryLocation, InventorySegment inventorySegment){
        def Navigator card;
        if(inventoryLocation == null){
            card = getEditInventoryCard(item, inventorySegment);
        }else{
            card = getEditInventoryCard(item, inventoryLocation, inventorySegment)
        }
        card.module(EditInventoryCardModule.class)
    }

    def editInventory(ItemConstants item, inventoryLocation, InventorySegment inventorySegment){
        getInventoryEditCard(item, inventoryLocation, inventorySegment).clickEditButton()
    }

    def editInventoryByItemAndSegment(ItemConstants item, InventorySegment inventorySegment){
        editInventory(item, null, inventorySegment)
    }

    def removeInventoryIfExists(Item item, InventorySegment inventorySegment, InventoryLocation inventoryLocation, Boolean isControlledSubstance) {
        try{
            removeExistingInventory(item, inventorySegment, inventoryLocation, isControlledSubstance)
        } catch(WaitTimeoutException e){
            return true
        }
    }

    def searchAndDeleteInventory(Item item, InventorySegment inventorySegment, InventoryLocation inventoryLocation) {
        searchInventory(InventorySearchType.ALL, item.drugId())

        deleteInventory(item, inventorySegment, inventoryLocation)

    }

    def deleteInventory(Item item, InventorySegment inventorySegment, InventoryLocation inventoryLocation) {
        waitForAnimationToComplete()

        def EditInventoryCardModule card = getInventoryEditCard(item, inventoryLocation, inventorySegment)

        card.clickDeleteButton()

        waitForAnimationToComplete()
    }

    def removeExistingInventory(Item item, InventorySegment inventorySegment, InventoryLocation inventoryLocation, Boolean isControlledSubstance) {
        searchAndDeleteInventory(item, inventorySegment, inventoryLocation)

        if(isControlledSubstance){
            approverAuthentication.enterApproval(LOGIN_ID_APPROVER, LOGIN_PWD_APPROVER)
        }

        confirmDelete()
        searchTextInputBox.value("")
        return true;
    }

    def confirmDelete(){
        confirmComponent.clickConfirmButton()
    }

    def cancelDelete(){
        confirmComponent.clickCancelButton()
    }

    def clickDeleteOverlay(){
        clickDataId("removalOffCanvas")
        waitForAnimationToComplete()
        waitForAnimationToComplete()
    }

    def tabClick(){
        waitForAnimationToComplete()
        adjustment.clickTab()
    }

    def shiftTabClick(){
        waitForAnimationToComplete()
        adjustment.clickShiftTab()
    }


    def verifySearchByOrder(String firstOption, String lastOption){
        searchByTypeDropDown.fetchFirstElementText() == firstOption
        searchByTypeDropDown.fetchLastElementText() == lastOption
    }

    def verifyNotesIsFocused(){
        adjustment.verifyNotesIsFocused()
    }

    def verifyQtyIsFocused(){
        adjustment.verifyQtyIsFocused()
    }

    def verifyReasonCodeIsFocused(){
        adjustment.verifyReasonCodeIsFocused()
    }

    def verifyExpirationIsFocused(){
        adjustment.verifyExpirationIsFocused()
    }

    def verifyLotIsFocused(){
        adjustment.verifyLotIsFocused()
    }

}
