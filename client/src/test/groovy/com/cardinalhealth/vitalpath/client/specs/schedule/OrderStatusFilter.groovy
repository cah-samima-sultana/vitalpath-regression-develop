package com.cardinalhealth.vitalpath.client.specs.schedule

import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.client.pages.schedule.ScheduleHomePage
import com.cardinalhealth.vitalpath.client.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.*
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting
import spock.lang.IgnoreRest

class OrderStatusFilter extends BaseSpec{

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

    def createMedicationOrderLinePlan(siteId, inventorySegmentId, ndc, plannedQuantity, inventoryLocation, orderLine) {
        def itemId = itemService.findItemIdByNDC(ndc)
        def inventoryLocationId = inventoryLocationService.findInventoryLocationIdByPath(inventoryLocation, siteId)

        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(itemId, inventoryLocationId, inventorySegmentId, null, plannedQuantity, Session.loggedInSiteId, orderLine)
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.COMPLETED_FILTER, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.IN_PROGRESS_FILTER, value = SettingsConstant.FALSE)])
    def 'I can see no orders message'() {
        given: 'all order filters are deselected'
            true == true

        when: 'I am on schedule home page'
            to ScheduleHomePage

        then: 'The Schedule Summary should display a message saying there are no orders'
            scheduleSummary.clickRefreshButton()
            scheduleSummary.verifyNoOrdersMessageExists()
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.COMPLETED_FILTER, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.IN_PROGRESS_FILTER, value = SettingsConstant.FALSE)])
    def 'I can see not started orders '() {
        given: 'all order filters are deselected'
            true == true

        when: 'I am on schedule home page'
            to ScheduleHomePage

        and: 'I have order in not-started status'
            setupOrder()
            scheduleSummary.clickRefreshButton()

        and: "I select the NOT STARTED filter"
            scheduleSummary.medicationOrderFilter.clickFilterNotStarted()

        then: 'The not started order is displayed'
            scheduleSummary.openClinicalOrderById(clinicalOrder1.id)
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.COMPLETED_FILTER, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.IN_PROGRESS_FILTER, value = SettingsConstant.FALSE)])
    def 'I can see not planned orders '() {
        given: 'I am on schedule home page'
            to ScheduleHomePage

        and: 'I have order in planned status'
            setupOrder()
            createMedicationOrderLinePlan(Session.loggedInSiteId, InventorySegment.SITE_OWNED.id(), ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 30000,  PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(),  clinicalOrderLine1)
            scheduleSummary.clickRefreshButton()

        when: "I select the planned filter"
            scheduleSummary.medicationOrderFilter.clickFilterPlanned()

        then: 'The planned order is displayed'
            scheduleSummary.openClinicalOrderById(clinicalOrder1.id)
    }
}
