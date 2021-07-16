package com.cardinalhealth.vitalpath.client.specs.schedule
import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.client.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.ItemConstants
import com.cardinalhealth.vitalpath.datasetup.PhysicianConstants
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.datasetup.SettingsConstant
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting
import com.cardinalhealth.vitalpath.utils.SelectorOptions

class MedicationOrder extends BaseSpec {

    def patient1
    def clinicalOrder1

    def setup(){
        patient1 = patientService.makePatient()
        refreshPage()
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE)])
    def 'User can add a medication order and edit a line'(){
        given: 'I am on the schedule home page'
        goToSchedulePage()
        
        when: 'I click the add button'
            scheduleSummary.clickAddButton()

        and: 'I search for patient'
            scheduleDetails.clinicalOrderHeader.searchForPatient(new SelectorOptions(searchValue: patient1.accountNumber, actionKey: SelectorOptions.ActionKeys.ENTER))

        and: "I search for physician"
            waitFor(3, { scheduleDetails.clinicalOrderHeader.physicianDropDown.dropDownResults.isDisplayed()})
            scheduleDetails.clinicalOrderHeader.searchForPhysician(new SelectorOptions(searchValue: PhysicianConstants.physician1.lastName, actionKey: SelectorOptions.ActionKeys.ENTER))

        and: "I am in display mode"
            scheduleDetails.clinicalOrderHeader.isInDisplayMode()

        then: "I can search and select a drug family using enter after entering dose"
            scheduleDetails.clinicalOrderLinesGrid.addDrugFamilyLine("Gemzar", "12")

        and: "I can open the line for editing"
            scheduleDetails.clinicalOrderLinesGrid.getRowByLineNumber("0").clickEditButton()

        and: "I can modify the dose"
            scheduleDetails.clinicalOrderLinesGrid.getRowByLineNumber("0").enterDose("20")

        and: "I can click save"
            scheduleDetails.clinicalOrderLinesGrid.getRowByLineNumber("0").clickLineSaveButton()

        and: "The dose equals the input value"
            scheduleDetails.clinicalOrderLinesGrid.getRowByLineNumber("0").doseIsEqualTo("20")
    }

    def setupOrder() {
        patient1 = patientService.makePatient()
        clinicalOrder1 = createMedicationOrder(Session.loggedInSiteId, patient1, PhysicianConstants.physician1);
        createMedicationOrderLine(clinicalOrder1.id, 1, ItemConstants.GEMZAR_1000mg_16729_0117_11.drugFamilyId, 50000)
        return true
    }

    def createMedicationOrder(siteId, patient, physician) {
        medicationOrderService.makeMedicationOrder(patient, physician, System.currentTimeMillis(), siteId)
    }

    def createMedicationOrderLine(orderId, lineNumber, drugFamilyId, quantity) {
        medicationOrderService.addMedicationLine(drugFamilyId, lineNumber, quantity, 'IV', orderId)
    }


    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE)])
    def "Add a line to existing medication order"(){
        when: "I can go to the schedule home page"
            goToSchedulePage()

        and: "I select a medication order"
            setupOrder()
            scheduleSummary.clickRefreshButton()

        and: "I click on a medication order"
            scheduleSummary.openClinicalOrderById(clinicalOrder1.id)

        and: "I click the add line button"
            scheduleDetails.clickAddLine()

        then: "I can search and select a drug family"
            waitFor(3, {scheduleDetails.clinicalOrderLinesGrid.clinicalOrderLineRow.drugFamilyDropDown})
            scheduleDetails.clinicalOrderLinesGrid.addDrugFamilyLine("Lipitor", "1200")

        and: "The line is saved"
            scheduleDetails.clinicalOrderLinesGrid.findRowByLineNumber("1")
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE)])
    def "Can delete order and line"(){
        given: "I am on schedule home page"
            goToSchedulePage()

        and: "I have a medication "
            setupOrder()
            scheduleSummary.clickRefreshButton()

        and: "I click on a medication order"
            scheduleSummary.openClinicalOrderById(clinicalOrder1.id)

        when: "I delete the line"
            scheduleDetails.clinicalOrderLinesGrid.getRowByLineNumber("1").clickLineDeleteButton()

        and: "I confirm the delete of the line"
            scheduleDetails.confirmationDialog.clickContinueButton()

        and: "I  delete the medication order"
            scheduleDetails.clinicalOrderHeader.clickDeleteButton()

        and: "I confirm the delete of the order"
            scheduleDetails.confirmationDialog.clickContinueButton()

        then: "The order is deleted"
            scheduleSummary.cardByMrnNotExist(patient1.accountNumber)
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE)])
    def "Can open dispense receipt"() {
        when: "I can go to the schedule home page"
            goToSchedulePage()

        and: "I select a medication order"
            setupOrder()
            scheduleSummary.clickRefreshButton()

        and: "I click on a medication order"
            scheduleSummary.openClinicalOrderById(clinicalOrder1.id)

        then: "I can open the dispense receipt"
            scheduleDetails.openDispenseReceipt()

        and: "I can see the patient name in the receipt"
            scheduleDetails.pdfContainsValue(patient1.properName)
    }
}
