package com.cardinalhealth.vitalpath.cabinet.specs.dispense

import com.cardinalhealth.vitalpath.EnvironmentProperties
import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.cabinet.modules.components.DispenseOrderLineCardModule
import com.cardinalhealth.vitalpath.cabinet.modules.components.EMRFixModule
import com.cardinalhealth.vitalpath.cabinet.modules.components.SuggestedDispenseModule
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispenseAddMedicationOrderPage
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispenseHomePage
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispensePlanPage
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.PlanPickPage
import com.cardinalhealth.vitalpath.cabinet.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.*
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.LabelPage



class SuggestedDispenseSpec extends BaseSpec {

    def patient
    def clinicalOrder
    def clinicalOrderExternalId
    def createdMedicationOrder

    def setup(){
        setOnHandInventory()

        patient = patientService.makePatient()
        clinicalOrderExternalId = UUID.randomUUID().toString()
        clinicalOrder = medicationOrderService.makeExternalMedicationOrder(patient, PhysicianConstants.physician1, clinicalOrderExternalId, System.currentTimeMillis(), Session.loggedInSiteId);

        createMedicationOrderLine(clinicalOrder.id, 1, ItemConstants.GEMZAR_200mg_00002_7501_01.drugFamilyId, 50000)
        createMedicationOrderLine(clinicalOrder.id, 2, ItemConstants.ALOXI_200mcg_62856_0797_01.drugFamilyId, 60000)
    }

    def setOnHandInventory() {
        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(), Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.GEMZAR_200mg_00002_7501_01.getNdc(), ItemConstants.GEMZAR_200mg_00002_7501_01.getItemId(), 15000, null, null)
        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(), Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(), ItemConstants.ALOXI_200mcg_62856_0797_01.getNdc(), ItemConstants.ALOXI_200mcg_62856_0797_01.getItemId(), 15000, null, null)
    }

    def createMedicationOrder(patient, physician) {
        createdMedicationOrder = medicationOrderService.makeMedicationOrder(patient, physician, System.currentTimeMillis(), Session.loggedInSiteId)
    }

    def createMedicationOrderLine(orderId, lineNumber, drugFamilyId, quantity) {
        medicationOrderService.addMedicationLine(drugFamilyId, lineNumber, quantity, 'IV', orderId)
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.SUGGESTED_DISPENSE, value = SettingsConstant.TRUE)
            ])
    def "Edit Suggested Dispense"() {
        given: "I have a clinical order with 2 lines"
        goToDispensePage()
        clickRefreshButton()

        when: "I select the clinical order"
        selectFirstCardByMrn(patient.accountNumber)

        then: "I am on the dispense planning page"
        at DispensePlanPage

        and: "I am in suggested dispense mode"
        isSuggestedDispenseMode()

        when: "I select to edit a line"
        SuggestedDispenseModule suggestedDispenseLine = getSuggestedDispenseLineByIndex(1)
        suggestedDispenseLine.clickEdit()

        then: "The mode switches to the default plan mode"
        isDefaultPlanMode()

        and: "The line I clicked 'Edit' on is unplanned"
        waitFor(3, {$(".dispenseOrderLineCard").size() > 0})
        def lines = $(".dispenseOrderLineCard")
        def planLine1 = lines.eq(1)
        planLine1.find("[data-id=planned-quantity]").text().trim() == "0 MCG"

        and: "The other line(s) are still planned"
        def planLine0 = lines.eq(0)
        planLine0.find("[data-id=planned-quantity]").text().trim() == "500 MG"
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.SUGGESTED_DISPENSE, value = SettingsConstant.TRUE)
            ])
    def "Skip Suggested Dispense"() {
        given: "I have a clinical order with 2 lines"
        goToDispensePage()
        clickRefreshButton()

        when: "I select the clinical order"
        selectFirstCardByMrn(patient.accountNumber)

        then: "I am on the dispense planning page"
        at DispensePlanPage

        and: "I am in suggested dispense mode"
        isSuggestedDispenseMode()

        when: "I select to skip a line"
        SuggestedDispenseModule suggestedDispenseLine0 = getSuggestedDispenseLineByIndex(0)
        suggestedDispenseLine0.clickSkip()

        and: "I choose to edit another"
        SuggestedDispenseModule suggestedDispenseLine1 = getSuggestedDispenseLineByIndex(1)
        suggestedDispenseLine1.clickEdit()

        then: "The mode switches to the default plan mode"
        isDefaultPlanMode()

        and: "The line I clicked 'Edit' on is unplanned"
        waitFor(3, {$(".dispenseOrderLineCard").size() > 0})
        def lines = $(".dispenseOrderLineCard")
        def planLine1 = lines.eq(1)
        planLine1.find("[data-id=planned-quantity]").text().trim() == "0 MCG"

        and: "The line I clicked 'Skip' on is unplanned"
        def planLine0 = lines.eq(0)
        planLine0.find("[data-id=planned-quantity]").text().trim() == "0 MG"
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.SUGGESTED_DISPENSE, value = SettingsConstant.TRUE)
            ])
    def "EMR Line - Created with Error"() {
        given: "I have a clinical order with an EMR line that needs to be fixed"
        medicationOrderService.addMedicationOrderLineSource(true, clinicalOrder.id, clinicalOrderExternalId, Session.site1Id, 1000);
        goToDispensePage()
        clickRefreshButton()

        when: "I select the clinical order"
        selectFirstCardByMrn(patient.accountNumber)

        then: "I am on the dispense planning page"
        at DispensePlanPage

        and: "I am in default plan mode"
        isDefaultPlanMode()

        then: "I select the Needs Attention line"
        selectPlanLine(2)

        and: "I fix the Needs Attention line"
        medicationOrderPlan.emrFixModule.selectDrugFamily(DrugFamily.GEMZAR.drugFamilyName())
        medicationOrderPlan.emrFixModule.inputQuantity("200")
        medicationOrderPlan.emrFixModule.clickFixButton()

        then: "I am taken to the suggested dispense module"
        isSuggestedDispenseMode()
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.SUGGESTED_DISPENSE, value = SettingsConstant.TRUE)
            ])
    def "Adding order goes to suggested dispense"() {
        given: "I am on the dispense home screen"
        goToDispensePage()

        when: "I click to add an order"
        clickAddButton()

        then: "I am on the add medication order screen"
        at DispenseAddMedicationOrderPage

        and: "I enter the patient and physician names"
        addMedicationOrder.searchAndSelectPatientByLastName(patient)
        addMedicationOrder.searchPhysician(PhysicianConstants.physician1)

        and: "I click to add a line"
        def addLineButton = $("[data-id=add-clinical-order-line-button]")
        waitFor(3, {addLineButton.hasClass("isDisabled") == false})
        addLineButton.click()

        and: "I enter the drug order information"
        addMedicationOrder.medicationOrderLineMaint.addDrugFamilyLine(DrugFamily.GEMZAR.drugFamilyName(), "20000", null)

        and: "I click 'Add Line and Done'"
        def saveAndDoneButton = $("[data-id=save-add-done-clinical-order-line-button]")
        waitFor(3, {saveAndDoneButton.hasClass("isDisabled") == false})
        saveAndDoneButton.click()

        then: "I am on the suggested dispense screen"
        at DispensePlanPage
        isSuggestedDispenseMode()
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.SUGGESTED_DISPENSE, value = SettingsConstant.TRUE)
            ])
    def "Go to label screen and going back stays in Suggested Dispense mode"() {
        given: "I have a clinical order with 2 lines"
        goToDispensePage()
        clickRefreshButton()

        when: "I select the clinical order"
        selectFirstCardByMrn(patient.accountNumber)

        then: "I am on the dispense planning page"
        at DispensePlanPage

        and: "I click on the label button"
        clickLabelButton()

        then: "I am on the label screen"
        at LabelPage

        and: "I click the back button"
        waitFor(3, {backButton.click()})

        then: "I am back on the dispense screen"
        at DispensePlanPage

        and: "I am still in suggested dispense mode"
        isSuggestedDispenseMode()

    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.SUGGESTED_DISPENSE, value = SettingsConstant.TRUE)
            ])
    def "Picking order and going back returns to Suggested Dispense mode"() {
        given: "I have a clinical order with 2 lines"
        goToDispensePage()
        clickRefreshButton()

        when: "I select the clinical order"
        selectFirstCardByMrn(patient.accountNumber)

        then: "I am on the dispense planning page"
        at DispensePlanPage

        and: "I click on the Pick Button on the sidebar"
        clickPlanPickButton()

        then: "I am on the pick screen"
        at PlanPickPage

        and: "I click on the back button"
        waitFor(3, {clickBackButton()})

        then: "I am back on the dispense screen"
        at DispensePlanPage

        and: "I am still in suggested dispense mode"
        isSuggestedDispenseMode()
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.SUGGESTED_DISPENSE, value = SettingsConstant.TRUE)
            ])
    def "Picking an order returns you to dispense home screen"() {
        given: "I have a clinical order with 2 lines"
        goToDispensePage()
        clickRefreshButton()

        when: "I select the clinical order"
        selectFirstCardByMrn(patient.accountNumber)

        then: "I am on the dispense planning page"
        at DispensePlanPage

        and: "I click to pick the order"
        clickPlanPickButton()

        then: "I am on the pick screen"
        at PlanPickPage

        and: "I confirm all dispenses"
        clickConfirmAllButton()

        then: "I am taken back to the dispense home screen"
        at DispenseHomePage
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.SUGGESTED_DISPENSE, value = SettingsConstant.TRUE)
            ])
    def "Edit and pick from suggested dispense"() {
        given: "I have a clinical order with 2 lines"
        goToDispensePage()
        clickRefreshButton()

        when: "I select the clinical order"
        selectFirstCardByMrn(patient.accountNumber)

        then: "I am on the dispense planning page"
        at DispensePlanPage

        and: "I click on the edit button of the first order order"
        SuggestedDispenseModule suggestedDispenseLine0 = getSuggestedDispenseLineByIndex(0)
        suggestedDispenseLine0.clickEdit()

        then: "I am on the default planning screen"
        at DispensePlanPage

        and: "I am in default plan mode"
        isDefaultPlanMode()

        and: "I plan all of the orders"
        planAllLines()

        and: "I click on the pick button"
        clickPlanPickButton()

        then: "I am on the pick screen"
        at PlanPickPage

        and: "I confirm all dispenses"
        clickConfirmAllButton()

        then: "I am taken back to the dispense home screen"
        at DispenseHomePage

    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.SUGGESTED_DISPENSE, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.COMPLETED_FILTER, value = SettingsConstant.TRUE)
            ])
    def "Switching from plan to return or waste changes modes"() {
        given: "I have a clinical order with 2 lines"
        goToDispensePage()
        clickRefreshButton()

        when: "I select the clinical order"
        selectFirstCardByMrn(patient.accountNumber)

        then: "I am on the dispense planning page"
        at DispensePlanPage

        and: "I am still in suggested dispense mode"
        isSuggestedDispenseMode()

        and: "I click to pick the order"
        clickPlanPickButton()

        then: "I am on the pick screen"
        at PlanPickPage

        and: "I confirm all dispenses"
        clickConfirmAllButton()

        then: "I am taken back to the dispense home screen"
        at DispenseHomePage

        then: "I click on the order I just dispensed"
        selectFirstCardByPatient(patient)

        then: "I am on the dispense planning page"
        at DispensePlanPage

        and: "I am still in suggested dispense mode"
        isSuggestedDispenseMode()

        and: "I click on the return filter button"
        clickReturnFilter()

        and: "I am in default mode"
        isDefaultPlanMode()

        then: "I click on the waste button"
        clickWasteFilter()

        and: "I am still in default mode"
        isDefaultPlanMode()

        then: "I click on the plan button"
        clickPlanFilter()

        and: "I am still in suggested dispense mode"
        isSuggestedDispenseMode()

    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.SUGGESTED_DISPENSE, value = SettingsConstant.TRUE)
            ])
    def "Edit Dose in Suggested Dispense"() {
        given: "I have a clinical order with 2 lines"
        goToDispensePage()
        clickRefreshButton()

        when: "I select the clinical order"
        selectFirstCardByMrn(patient.accountNumber)

        then: "I am on the dispense planning page"
        at DispensePlanPage

        and: "I am in suggested dispense mode"
        isSuggestedDispenseMode()

        when: "I very the planned items and select to edit the dose"
        SuggestedDispenseModule suggestedDispenseLine = getSuggestedDispenseLineByIndex(1)
        suggestedDispenseLine.verifyPlanItemVialCount(0,"3")
        suggestedDispenseLine.verifyNdc(0,"62856-0797-01")
        suggestedDispenseLine.clickEditDose()

        then: "Enter a quantity"
        enterQuantityOnEditDoseAndSave(1000)
        saveAndCloseEditDose()

        and: "The line I clicked 'Edit dose' have the updated dose"
        suggestedDispenseLine.verifyDose("1000")

        and: "The line I clicked 'Edit dose' have the updated planned lines"
        suggestedDispenseLine.verifyPlanItemVialCount(0,"4")
        suggestedDispenseLine.verifyNdc(0,"62856-0797-01")

        and: "The other line(s) dose does not change"
        SuggestedDispenseModule otherSuggestedDispenseLine = getSuggestedDispenseLineByIndex(0)
        otherSuggestedDispenseLine.verifyDose("500")
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.SUGGESTED_DISPENSE, value = SettingsConstant.TRUE)
            ])
    def "Picking order and updating the inventory"() {
        given: "I have a clinical order with 2 lines"
        goToDispensePage()
        clickRefreshButton()

        when: "I select the clinical order"
        selectFirstCardByMrn(patient.accountNumber)

        then: "I am on the dispense planning page"
        at DispensePlanPage

        and: "I click on the Pick Button on the sidebar"
        clickPlanPickButton()

        then: "I am on the pick screen"
        at PlanPickPage
        confirmFirstPlanPickItem()

        and: "I click on the update button"
        waitFor(3, {clickUpdateButton()})
    }


    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.SUGGESTED_DISPENSE, value = SettingsConstant.TRUE)
            ])
    def "Choose to use an existing order from the history screen goes to suggested dispense"() {
        given: "I am on the dispense home screen"
        goToDispensePage()
        clickRefreshButton()

        when: "I click to add an order"
        clickAddButton()

        then: "I am on the add medication order screen"
        at DispenseAddMedicationOrderPage

        and: "I enter the patient and physician names"
        addMedicationOrder.searchAndSelectPatientByMrn(patient)

        and: "I select the first medication order"
        historyMedicationOrder.selectHistoryCard(clinicalOrder.id)

        and: "I choose to use existing order"
        historyMedicationOrder.clickUseExistingOrder()

        then: "I am on the suggested dispense screen"
        at DispensePlanPage
        isSuggestedDispenseMode()
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.SUGGESTED_DISPENSE, value = SettingsConstant.TRUE)
            ])
    def "Choose to create a new order from the history screen goes to suggested dispense on adding a line"() {
        given: "I am on the dispense home screen"
        goToDispensePage()
        clickRefreshButton()

        when: "I click to add an order"
        clickAddButton()

        then: "I am on the add medication order screen"
        at DispenseAddMedicationOrderPage

        and: "I enter the patient and physician names"
        addMedicationOrder.searchAndSelectPatientByMrn(patient)

        and: "I choose to use existing order"
        historyMedicationOrder.clickNewOrder()

        and: "I click to add a line"
        def addLineButton = $("[data-id=add-clinical-order-line-button]")
        waitFor(3,{addLineButton.hasClass("isDisabled") == false})
        addLineButton.click()

        and: "I enter the drug order information"
        addMedicationOrder.medicationOrderLineMaint.addDrugFamilyLine(DrugFamily.GEMZAR.drugFamilyName(), "20000", null)

        and: "I click 'Add Line and Done'"
        def saveAndDoneButton = $("[data-id=save-add-done-clinical-order-line-button]")
        waitFor({saveAndDoneButton.hasClass("isDisabled") == false})
        saveAndDoneButton.click()

        then: "I am on the suggested dispense screen"
        at DispensePlanPage
        isSuggestedDispenseMode()
    }

    def cleanupSpec(){
        settingsService.turnSuggestedDispenseOffAllLocations(EnvironmentProperties.instance.tenantId())
    }
}

