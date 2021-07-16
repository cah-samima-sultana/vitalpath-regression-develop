package com.cardinalhealth.vitalpath.cabinet.pages.inventory

import com.cardinalhealth.vitalpath.cabinet.modules.components.ApproverAuthenticationModule
import com.cardinalhealth.vitalpath.cabinet.modules.components.InventoryAdjustmentModule
import com.cardinalhealth.vitalpath.cabinet.modules.components.InventoryInfoModule
import com.cardinalhealth.vitalpath.datasetup.InventorySegment
import com.cardinalhealth.vitalpath.datasetup.ItemConstants
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants

class InventoryAddPage extends BaseInventoryPage {

    static at = { title == "IMS | Cabinet – Inventory – Index" }

    static content = {
        adjustment { module(new InventoryAdjustmentModule()) }
        approverAuthentication{ module(new ApproverAuthenticationModule()) }
        inventorySearch { $(".headerBar").find("#searchTextField") }

        scrollContainer { $(".scroll-container") }
        cards { scrollContainer.find(".scrollBoxCard") }

    }

    def searchForInventory(String searchText){
        inventorySearch << searchText
    }


    def findInventoryInfo(String ndc, String invLocation, String invSegment, String patientNameMRN){
        def matchingCard = null

        for(int i=0; i<cards.size(); i++){
            def card = cards[i]
            def inventoryInfo = card.module(InventoryInfoModule.class)

            def locationMatches = inventoryInfo.matchLocationName(invLocation)
            def ndcMatches = inventoryInfo.matchNDC(ndc)
            def invSegMatches = inventoryInfo.matchInventorySegmentType(invSegment)
            def patientMatches = inventoryInfo.matchPatient(patientNameMRN)

            if(locationMatches && ndcMatches && invSegMatches && patientMatches){
                matchingCard = inventoryInfo
                break
            }
        }

        matchingCard
    }

    def findInventoryCard(String ndc, String invLocation, String invSegment, String patientNameMRN){
        def matchingCard = null

        for(int i=0; i<cards.size(); i++){
            def card = cards[i]
            def inventoryInfo = card.module(InventoryInfoModule.class)

            def locationMatches = inventoryInfo.matchLocationName(invLocation)
            def ndcMatches = inventoryInfo.matchNDC(ndc)
            def invSegMatches = inventoryInfo.matchInventorySegmentType(invSegment)
            def patientMatches = inventoryInfo.matchPatient(patientNameMRN)

            if(locationMatches && ndcMatches && invSegMatches && patientMatches){
                matchingCard = card
                break
            }
        }

        matchingCard
    }

    def testSingleDose(){
        waitFor(3, {$("[data-id=inventoryEditAdjustmentComponent]")})

        adjustment.searchForDrug(ItemConstants.GEMZAR_200mg_00409_0183_01.getNdc())
        adjustment.selectDrugType(InventorySegment.SITE_OWNED)
        adjustment.selectLocation(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2)
        adjustment.assertTextContainsByName(adjustment.quantityLabel, "item")
        //adjustment.assertTextContainsByDataId(adjustment.ndcDataId, "00409-0183-01")
        //adjustment.assertTextContainsByDataId(adjustment.taiDataId, "200 MG")
        adjustment.assertTextContainsByDataId(adjustment.onHandQtyDataId, "0 MG")
        adjustment.assertTextContainsByDataId(adjustment.updatedQtyDataId, "0 MG")
        adjustment.assertInputValueByName(adjustment.quantityInputName, "0")
        adjustment.assertTextContainsByDataId(adjustment.onHandQtyIconDataId, "0")
        adjustment.assertTextContainsByDataId(adjustment.updatedQtyIconDataId, "0")

        adjustment.clickPlusButton()
        adjustment.assertTextContainsByDataId(adjustment.onHandQtyDataId, "0 MG")
        adjustment.assertTextContainsByDataId(adjustment.updatedQtyDataId, "200 MG")
        adjustment.assertTextContainsByDataId(adjustment.onHandQtyIconDataId, "0")
        adjustment.assertTextContainsByDataId(adjustment.updatedQtyIconDataId, "1")

        adjustment.clickPlusButton()
        adjustment.assertTextContainsByDataId(adjustment.onHandQtyDataId, "0 MG")
        adjustment.assertTextContainsByDataId(adjustment.updatedQtyDataId, "400 MG")
        adjustment.assertTextContainsByDataId(adjustment.onHandQtyIconDataId, "0")
        adjustment.assertTextContainsByDataId(adjustment.updatedQtyIconDataId, "2")

        adjustment.clickMinusButton()
        adjustment.assertTextContainsByDataId(adjustment.onHandQtyDataId, "0 MG")
        adjustment.assertTextContainsByDataId(adjustment.updatedQtyDataId, "200 MG")
        adjustment.assertTextContainsByDataId(adjustment.onHandQtyIconDataId, "0")
        adjustment.assertTextContainsByDataId(adjustment.updatedQtyIconDataId, "1")

        adjustment.clickMinusButton()
        adjustment.assertTextContainsByDataId(adjustment.onHandQtyDataId, "0 MG")
        adjustment.assertTextContainsByDataId(adjustment.updatedQtyDataId, "0 MG")
        adjustment.assertTextContainsByDataId(adjustment.onHandQtyIconDataId, "0")
        adjustment.assertTextContainsByDataId(adjustment.updatedQtyIconDataId, "0")

        adjustment.clickPlusButton()
        adjustment.assertTextContainsByDataId(adjustment.onHandQtyDataId, "0 MG")
        adjustment.assertTextContainsByDataId(adjustment.updatedQtyDataId, "200 MG")
        adjustment.assertTextContainsByDataId(adjustment.onHandQtyIconDataId, "0")
        adjustment.assertTextContainsByDataId(adjustment.updatedQtyIconDataId, "1")

        adjustment.clickCancelButton()
    }

    def testNonItemDrug(drug){
        waitForAnimationToComplete()


        searchForDrug(drug.id)
        adjustment.selectDrugType(InventorySegment.SITE_OWNED)
        selectLocation(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2)
        selectReasonCode('Restock Cardinal')
        clickPlusButton()
        clickSaveButton()

    }

    def searchForDrug(value){
        adjustment.searchForDrug(value)
    }

    def verifyFormText(value){
        adjustment.assertTextContainsByName(adjustment.quantityLabel, value)
    }

    def verifyNdc(value){
        adjustment.assertTextContainsByDataId(adjustment.itemDataId, value)
    }

    def clickPlusButton(){
        adjustment.clickPlusButton()
    }

    def clickSaveButton(){
        adjustment.clickSaveButton()
    }

    def selectLocation(location){
        adjustment.selectLocation(location)
    }

    def selectReasonCode(value){
        adjustment.selectReasonCode(value)
    }

    def verifyDrugItemSelectedName(value){
        adjustment.assertTextContainsByDataId(adjustment.drugItemSelectedName, value)
    }

    def verifyInventorySegmentType(String type){
        adjustment.verifyInventorySegmentType(type)
    }

    def verifyInventorySegmentTypeIsDisabled(){
        adjustment.verifyInventorySegmentTypeIsDisabled()
    }
}
