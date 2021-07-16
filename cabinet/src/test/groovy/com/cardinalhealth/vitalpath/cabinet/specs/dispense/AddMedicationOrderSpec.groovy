package com.cardinalhealth.vitalpath.cabinet.specs.dispense
import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispenseAddMedicationOrderPage
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispenseHomePage
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispensePlanPage
import com.cardinalhealth.vitalpath.cabinet.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.*
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting

class AddMedicationOrderSpec extends BaseSpec {

    def patient

    def setup(){
        patient = patientService.makePatient()
        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(), Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.GEMZAR_200mg_00002_7501_01.getNdc(), ItemConstants.GEMZAR_200mg_00002_7501_01.getItemId(), 15000, null, null)

    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.CLINICAL_ORDER_LINE_PROMPT, value = SettingsConstant.ClinicalOrderLinePrompt.DRUG_FAMILY_ONLY)])
    def "I can add and delete a medication order"() {
        given: "I am on the dispense home page"
            goToDispensePage()

        when: "I add a medication order"
            waitForAnimationToComplete()

            clickAddButton()

        then: "I am at the add medication order page"
            at DispenseAddMedicationOrderPage

        then: "I search for a patient"
            searchAndSelectPatientByMrn(patient)

        and: "I search for a physician"
            searchPhysician(PhysicianConstants.physician1)

        and: "I add a new line"
            addNewDrugFamilyLine('Gemzar', '200', null)

        and: "I finish adding lines"
            clickAddLineDoneButton()

        then: "I am on the planning page"
            at DispensePlanPage

        and: "the line is not started"
            isLineNotStarted(1) == true

        and: "line prescribed, dispensed and estimated wast is displayed correctly"
            verifyLineQuantities(1, 200, 0, 0, 0, 'MG')

        and: "I remove the current line"
            clickRemoveLineButton()

        and: "I confirm the remove"
            clickConfirmWithReasonButton()

        then: "I remove the clinical order"
            clickDeleteClinicalOrderButton()

        and: "I confirm the removal of the clinical order"
            clickConfirmWithReasonButton()

        then: "I am at the dispense home page"
            at DispenseHomePage

        and: "I do not see the order"
            cardByMrnNotExist(patient.accountNumber)
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A)
    def "I can create a medication order by cloning a previous order"() {
        def medicationOrderId = null

        given: "I have medication order for the previous day"
            (medicationOrderId = createMedicationOrderForPreviousDay(patient, PhysicianConstants.physician1)) != null

        and: "I am on the dispense home page"
            refreshPage()
            goToDispensePage()

        when: "I add a medication order"
            clickAddButton()

        and: "I am at the add medication order page"
            at DispenseAddMedicationOrderPage

        and: "I enter a patient name"
            searchAndSelectPatientByMrn(patient)

        and: "I search for a physician"
            searchPhysician(PhysicianConstants.physician1)

        and: "I click the clone button"
            clickCloneButton()

        and: "I click on previous day order"
            cloneMedicationOrder.clickMedicationOrderToClone(medicationOrderId)

        and: "I click on clone button"
            cloneMedicationOrder.clickCloneMedicationOrderButton()

        and: "I save the medication order"
            clickSaveButton()

        then: "I can plan the lines"
            at DispensePlanPage

        and: "Line status is not started"
            isLineNotStarted(1) == true
        and:"Go back to dispense page"
            clickHomeButton()
    }

    def clinicalOrder
    def orderLine

    def setupOrder(){
        clinicalOrder = medicationOrderService.makeMedicationOrder(patient, PhysicianConstants.physician1, System.currentTimeMillis(), Session.loggedInSiteId)
        addOrderLine()
    }

    def addOrderLine(String drugFamilyId = DrugFamily.GEMZAR.id(), lineNumber=1){
        orderLine = medicationOrderService.addMedicationLine(drugFamilyId, lineNumber, 10000, 'IV', clinicalOrder.id)
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A)
    def "I can edit the medication order line"(){
        given: 'I have a medication order line'
            at DispenseHomePage
            setupOrder()
            clickRefreshButton()

        and: 'Select medication order for dispense'
            selectFirstCardByMrn(patient.accountNumber)

        and: 'I am on the dispense planning page'
            at DispensePlanPage

        when: 'I click edit line button'
            clickEditLineButton()

        and: 'I can change quantity'
            medicationOrderPlan.medicationOrderLineMaint.enterQuantity(200)

        and: 'I can change route'
            medicationOrderPlan.medicationOrderLineMaint.selectRoute('Oral')

        and: 'I save changes'
            medicationOrderPlan.medicationOrderLineMaint.saveLineAndClose()

        then: 'New changes are saved'
            verifyLineQuantities(1, 200, 0, 0, 0, 'MG')
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A)
    def "I can click the scroll buttons"(){
        given: 'I have 5 medication order lines'
        setupOrder()
        addOrderLine(DrugFamily.GEMZAR.id(),2)
        addOrderLine(DrugFamily.ALOXI.id(),3)
        addOrderLine(DrugFamily.DECADRON.id(), 4)
        addOrderLine(DrugFamily.FLUOROURACIL.id(), 5)
        addOrderLine(DrugFamily.NEULASTA.id(), 6)
        goToDispensePage()
        clickRefreshButton()

        and: 'Select medication order for dispense'
        selectFirstCardByMrn(patient.accountNumber)

        and: 'I am on the dispense planning page'
        at DispensePlanPage

        and: 'I verify the up scroll button is disabled'
        verifyOrderLineContainerUpScrollIsDisabled()
        verifyOrderLineContainerDownScrollIsEnabled()

        when: 'I click the bottom scroll button'
        clickOrderLineContainerDownScroll()

        then: "I see the up scroll button is enabled and down scroll button is disabled"
        verifyOrderLineContainerUpScrollIsEnabled()
    }


    def createMedicationOrderForPreviousDay(patient, physician) {

        long yesterday = (System.currentTimeMillis()- (24 * 60 * 60 * 1000));

        def orderResponse = medicationOrderService.makeMedicationOrder(patient, physician, yesterday, Session.loggedInSiteId)

        medicationOrderService.addMedicationLine(DrugFamily.GEMZAR.id(), 1, 100, 'IV', orderResponse.id)

        return orderResponse.id;
    }
}
