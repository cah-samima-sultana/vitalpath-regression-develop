package com.cardinalhealth.vitalpath.cabinet.specs.dispense

import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispenseHomePage
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispensePlanPage
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.PlanPickPage
import com.cardinalhealth.vitalpath.cabinet.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.InventorySegment
import com.cardinalhealth.vitalpath.datasetup.ItemConstants
import com.cardinalhealth.vitalpath.datasetup.PhysicianConstants
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.datasetup.SettingsConstant
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting
import spock.lang.IgnoreRest
import spock.lang.Shared

class Dispense  extends BaseSpec {

    def patient
    def clinicalOrder
    def orderLine

    def setup(){
        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(), Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.GEMZAR_200mg_00002_7501_01.getNdc(), ItemConstants.GEMZAR_200mg_00002_7501_01.getItemId(), 15000, null, null)

        patient = patientService.makePatient()
        clinicalOrder = medicationOrderService.makeMedicationOrder(patient, PhysicianConstants.physician1, System.currentTimeMillis(), Session.loggedInSiteId)
        orderLine = medicationOrderService.addMedicationLine(ItemConstants.GEMZAR_200mg_00002_7501_01.drugFamilyId, 1, 10000, 'IV', clinicalOrder.id)
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)])
    def 'I can dispense a medication order'(){
        given: 'I have a medication order line'
            goToDispensePage()
            clickRefreshButton()

        when: 'Select medication order for dispense'
            selectFirstCardByMrn(patient.accountNumber)

        then: 'I am on the dispense planning page'
            at DispensePlanPage

        and: 'I plan line for pick'
            planItemByLocationNdc(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.displayPath(), ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 1)

        and: 'I start to pick the line'
            clickPlanPickButton()

        then: 'I am at the plan pick page'
            at PlanPickPage

        and: 'I confirm the pick'
            confirmPlanPickItemByNdc(ItemConstants.GEMZAR_200mg_00002_7501_01.ndc)

        then: 'I am at the dispense planning page'
            at DispensePlanPage

        and: 'the line is complete'
            isLineComplete(1) == true

        and: 'the dispensed line values are correct'
            verifyLineQuantities(1, 100, 200, 0, 100, 'MG')
    }


    def dispenseLine(){
        dispenseService.dispenseItem(Session.loggedInSiteId, orderLine.id, InventorySegment.SITE_OWNED.id(), PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(),  ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 20000)
        return true
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
           siteSettings=[@Setting(name = SettingsConstant.COMPLETED_FILTER, value = SettingsConstant.TRUE),
                         @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)])
    def 'I can return a medication order'(){
        given: 'I have a medication order line'
            to DispenseHomePage
            dispenseLine()
            clickRefreshButton()

        and: 'Select medication order for dispense'
            selectFirstCardByMrn(patient.accountNumber)

        and: 'I am on the dispense planning page'
            at DispensePlanPage

        when: 'I click the return filter'
            clickReturnFilter()

        and: 'I plan the first item'
            planItemByLocationNdc(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.displayPath(), ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 1)

        and: 'I start to pick the line'
            clickPickReturnButton()

        then: 'I am at the plan pick page'
            at PlanPickPage

        and: 'I confirm the return'
            confirmPlanPickItemByNdc(ItemConstants.GEMZAR_200mg_00002_7501_01.ndc)

        and: 'I am at the planning page'
            at DispensePlanPage

        and: 'the dispensed line values are correct'
            verifyLineQuantities(1, 100, 0, 0, 0, 'MG')

        and: 'the line is not started'
            isLineNotStarted(1) == true
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.COMPLETED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)])
    def 'I can waste a medication order'(){
        given: 'I have a medication order line'
            to DispenseHomePage
            dispenseLine()
            clickRefreshButton()

        and: 'Select medication order for dispense'
             selectFirstCardByMrn(patient.accountNumber)

        and: 'I am on the dispense planning page'
            at DispensePlanPage

        when: 'I click the waste filter'
            clickWasteFilter()

        and: 'I plan the first item'
            planItemByLocationNdc(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.displayPath(), ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 1)

        and: 'I start to waste the line'
            clickPickWasteButton()

        and: 'I am at the plan pick page'
            at PlanPickPage

        and: 'I confirm the waste'
            confirmPlanPickItemByNdc(ItemConstants.GEMZAR_200mg_00002_7501_01.ndc)

        and: 'I save the credit and waste'
            saveWasteAdjustment()

        then: 'I am at the planning page'
            at DispensePlanPage

        and: 'the dispensed line values are correct'
            verifyLineQuantities(1, 100, 0, 0, 0, 'MG')

        and: 'the line is not started'
            isLineNotStarted(1) == true
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)])
    def 'I can edit inventory from pick screen'(){
        given: 'I have a medication order line'
        goToDispensePage()
        clickRefreshButton()

        when: 'Select medication order for dispense'
        selectFirstCardByMrn(patient.accountNumber)

        then: 'I am on the dispense planning page'
        at DispensePlanPage

        and: 'I plan line for pick'
        planItemByLocationNdc(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.displayPath(), ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 1)

        and: 'I start to pick the line'
        clickPlanPickButton()

        then: 'I am at the plan pick page'
        at PlanPickPage

        and: "I click on the update button"
        waitFor(3, {clickUpdateButton()})

        then: 'I update the quantity'
        clickUpdateInventoryPlusButton()
        selectUpdateInventoryReasonCode("Broken")

        and: 'I save the update'
        saveWasteAdjustment()

        and: 'the dispensed line values are correct'
        verifyPickItemOnHandQuantity(ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 76)

        cleanup: "Revert the quantity updated"
        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(), Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.GEMZAR_200mg_00002_7501_01.getNdc(), ItemConstants.GEMZAR_200mg_00002_7501_01.getItemId(), 15000, null, null)
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)])
    def 'I can add lot and expires to inventory and is displayed as dispensed lot'(){
        given: 'I have a medication order line'
        goToDispensePage()
        clickRefreshButton()

        when: 'Select medication order for dispense'
        selectFirstCardByMrn(patient.accountNumber)

        then: 'I am on the dispense planning page'
        at DispensePlanPage

        and: 'I plan line for pick'
        planItemByLocationNdc(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.displayPath(), ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 1)
        sleep(2000)
        and: 'I start to pick the line'
        clickPlanPickButton()

        then: 'I am at the plan pick page'
        at PlanPickPage

        and: "I click on the update button"
        waitFor(3, {clickUpdateButton()})

        then: 'I update the lot number and expires'
        inputInventoryLotNumber("12345")
        inputInventoryExpires("2020/11/05")
        selectUpdateInventoryReasonCode("Broken")

        and: 'I save the update'
        saveWasteAdjustment()

        then: 'I am at the plan pick page'
        at PlanPickPage
        sleep(2000)

        and: 'I see the lotnumber and expiration date'
        verifyPickItemDispenseDetailLotNumber(ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, "12345")
        verifyDispenseDetailExpirationDate(ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, "2020-11-05")

        and: 'the dispensed line values are correct'
        //verifyPickItemOnHandQuantity(ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 76)

        cleanup: "Revert the lotnumber and expires that was updated"
        def invLocationId = inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), Session.site1Id)
        sleep(1000)
        def inventory = inventoryService.getInventory(InventorySegment.SITE_OWNED.id(), ItemConstants.GEMZAR_200mg_00002_7501_01.getNdc(), invLocationId, ItemConstants.GEMZAR_200mg_00002_7501_01.getItemId(), null)
    }

//    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
//            siteSettings=[@Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)])
//    def 'I can remove lot of the inventory and the bar with dispense details is not shown'(){
//        given: 'I have a medication order line'
//        def invLocationId = inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), Session.site1Id)
//        def inventory = inventoryService.getInventory(InventorySegment.SITE_OWNED.id(), ItemConstants.GEMZAR_200mg_00002_7501_01.getNdc(), invLocationId, ItemConstants.GEMZAR_200mg_00002_7501_01.getItemId(), null)
//        inventoryAdjustmentService.updateInventory(inventory, "5678", null)
//        goToDispensePage()
//        clickRefreshButton()
//
//        when: 'Select medication order for dispense'
//        selectFirstCardByMrn(patient.accountNumber)
//
//        then: 'I am on the dispense planning page'
//        at DispensePlanPage
//
//        and: 'I plan line for pick'
//        planItemByLocationNdc(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.displayPath(), ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 1)
//
//        and: 'I start to pick the line'
//        clickPlanPickButton()
//
//        then: 'I am at the plan pick page'
//        at PlanPickPage
//
//        and: "I click on the update button"
//        waitFor(3, {clickUpdateButton()})
//
//        then: 'I update the lot number and expires'
//        inputInventoryLotNumber('')
//        selectUpdateInventoryReasonCode("Broken")
//
//        and: 'I save the update'
//        saveWasteAdjustment()
//
//        then: 'I am at the plan pick page'
//        at PlanPickPage
//
//        and: 'I see the lotnumber and expiration date'
//        verifyPickItemDispenseDetailLotNumber(ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, "12345")
//        verifyDispenseDetailExpirationDate(ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, "2020-11-05")
//
//        and: 'the dispensed line values are correct'
//        //verifyPickItemOnHandQuantity(ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 76)
//    }
}
