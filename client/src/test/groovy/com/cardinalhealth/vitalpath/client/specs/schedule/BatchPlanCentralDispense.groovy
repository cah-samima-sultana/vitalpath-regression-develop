package com.cardinalhealth.vitalpath.client.specs.schedule

import com.cardinalhealth.vitalpath.EnvironmentProperties
import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.client.pages.schedule.ScheduleHomePage
import com.cardinalhealth.vitalpath.client.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.*
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting

class BatchPlanCentralDispense extends BaseSpec {

    def patient1

    def clinicalOrder1
    def clinicalOrderLine1

    def setupOrder() {
        patient1 = patientService.makePatient()
        clinicalOrder1 = createMedicationOrder(patient1, PhysicianConstants.physician1);
        clinicalOrderLine1 = createMedicationOrderLine(clinicalOrder1.id, 1, ItemConstants.GEMZAR_200mg_00002_7501_01.drugFamilyId, 50000)

        return true
    }


    def createMedicationOrder(patient, physician) {
        medicationOrderService.makeMedicationOrder(patient, physician, System.currentTimeMillis(), Session.loggedInSiteId)
    }

    def createMedicationOrderLine(orderId, lineNumber, drugFamilyId, quantity) {
        medicationOrderService.addMedicationLine(drugFamilyId, lineNumber, quantity, 'IV', orderId)
    }

    @Login(site = PracticeConstants.SITE_1, cabinet = PracticeConstants.CABINET_A,
            siteSettings = [@Setting(name = SettingsConstant.CENTRAL_DISPENSE, value = SettingsConstant.FALSE, location = PracticeConstants.SITE_1),
                    @Setting(name = SettingsConstant.CENTRAL_DISPENSE, value = SettingsConstant.FALSE, location = PracticeConstants.SITE_2),
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.COMPLETED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.IN_PROGRESS_FILTER, value = SettingsConstant.TRUE)])
    def 'I cannot select different site when central dispense is off'() {

        given: 'I am on schedule home page'
            settingsService.turnCentralDispenseOffAllLocations(EnvironmentProperties.instance.tenantId())
            goToSchedulePage()

        and: 'I have a medication order'
            setupOrder()
            scheduleSummary.clickRefreshButton()

        and: "I click on a medication order"
            scheduleSummary.openClinicalOrderById(clinicalOrder1.id)

        and: "The status of the line is not started"
            scheduleDetails.getRowByLineNumber(1).statusIsNotStarted()

        when: "I select the drug name"
            scheduleDetails.getRowByLineNumber(1).clickRow()

        then: "the batch plan pane id displayed"
            scheduleDetails.batchPlanIsPresent()

        and: "I can see an Inventory site selector defaulted to the Admin site"
            scheduleDetails.batchPlan.selectedSiteEquals(Session.site1)

        and: "and I cannot select the Inventory site selector"
            scheduleDetails.batchPlan.siteSelectorIsDisabled()

        and: "I can see an inventory location selector defaulted to 'Select An Inventory Location'"
            scheduleDetails.batchPlan.selectedCabinetEquals("Select an Inventory Location")

        and: "the text displayed below the drop downs is correct"
            def message = scheduleDetails.batchPlan.getChooseLocationMessage()
            message.contains("Choose a site and inventory location for")
            message.contains(DrugFamily.GEMZAR.drugFamilyName())
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.CENTRAL_DISPENSE, value = SettingsConstant.FALSE, location = PracticeConstants.SITE_1),
                    @Setting(name = SettingsConstant.CENTRAL_DISPENSE, value = SettingsConstant.TRUE, location = PracticeConstants.SITE_2),
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.IN_PROGRESS_FILTER, value = SettingsConstant.TRUE)])
    def "I can change the plan site when central dispense is on for other site"() {

        given: 'I am on schedule home page'
            goToSchedulePage()

        and: 'I have a medication order'
            setupOrder()
            scheduleSummary.clickRefreshButton()

        and: "I click on a medication order"
            scheduleSummary.openClinicalOrderById(clinicalOrder1.id)

        when: "I select the drug name"
            scheduleDetails.getRowByLineNumber(1).clickRow()

        then: "the batch plan pane id displayed"
            scheduleDetails.batchPlanIsPresent()

        and: "I can see an Inventory site selector defaulted to the Admin site"
            scheduleDetails.batchPlan.selectedSiteEquals(Session.site1)

        and: "I can change my Inventory site selection"
            scheduleDetails.batchPlan.siteSelectorIsEnabled()

        and: "I can see an inventory location selector defaulted to 'Select An Inventory Location'"
            scheduleDetails.batchPlan.selectedCabinetEquals("Select an Inventory Location")

        and: "and I can change my inventory location selection"
            scheduleDetails.batchPlan.cabinetSelectorIsEnabled()

        then: "I can select a site with central dispense turned on"
        openAndSelectPlanSiteSelector(Session.site2)

    }
}