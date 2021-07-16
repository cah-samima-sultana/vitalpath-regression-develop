package com.cardinalhealth.vitalpath.client.specs.schedule
import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.client.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.*
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting

class BatchPlan extends BaseSpec {
    def patient1

    def clinicalOrder1

    def setupOrder(siteId) {
        patient1 = patientService.makePatient()
        clinicalOrder1 = createMedicationOrder(siteId, patient1, PhysicianConstants.physician1)
        return true
    }

    def createMedicationOrder(siteId, patient, physician) {
        medicationOrderService.makeMedicationOrder(patient, physician, System.currentTimeMillis(), siteId)
    }

    def createMedicationOrderLine(orderId, lineNumber, drugFamilyId, quantity) {
        medicationOrderService.addMedicationLine(drugFamilyId, lineNumber, quantity, 'IV', orderId)
    }

    def createMedicationOrderLinePlan(siteId, inventorySegmentId, ndc, plannedQuantity, inventoryLocation, orderLine) {
        def itemId = itemService.findItemIdByNDC(ndc)
        def inventoryLocationId = inventoryLocationService.findInventoryLocationIdByPath(inventoryLocation, siteId)

        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(itemId, inventoryLocationId, inventorySegmentId, null, plannedQuantity, Session.loggedInSiteId, orderLine)
    }

    def setOnHandInventory() {
        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(),
                Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(),
                ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.getNdc(), ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.getItemId(), 15000, null, null)

        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(),
                Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(),
                ItemConstants.ELOXATIN_100mg_00024_0591_20.getNdc(), ItemConstants.ELOXATIN_100mg_00024_0591_20.getItemId(), 10000, null, null)

        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(),
                Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DRAWER_8_POCKET_1.path(),
                ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.getNdc(), ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.getItemId(), 17400, null, null)
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.IN_PROGRESS_FILTER, value = SettingsConstant.TRUE)])
    def "I can fully plan a drug and unplan a drug"() {

        given: 'I am on schedule home page'
        refreshPage()
        goToSchedulePage()

        and: 'I have a medication order'
            setupOrder(Session.loggedInSiteId)
            createMedicationOrderLine(clinicalOrder1.id, 1, ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.drugFamilyId, 30000)
            setOnHandInventory()
            scheduleSummary.clickRefreshButton()

        and: "I click on a medication order"
            scheduleSummary.openClinicalOrderById(clinicalOrder1.id)

        and: "I select the drug name"
            scheduleDetails.getRowByLineNumber(1).clickRow()
            scheduleDetails.batchPlanIsPresent()

        when: "Select the cabinet with an inventory"
            openAndSelectPlanCabinetSelector(PracticeConstants.CABINET_A)

        and: "I can see plan items"
            waitFor({ scheduleDetails.batchPlan.getPlanItemCount() > 0 })
            def planItems = scheduleDetails.batchPlan.getPlanItems()

        and: "I can plan the item"
            planItems[0].clickPlusButton()
            planItems[0].clickPlusButton()

        then: "The status of the line changes to Planned"
            scheduleDetails.getRowByLineNumber(1).statusIsPlanned()

        and: "A batch planned icon replaces the line's action buttons"
            scheduleDetails.getRowByLineNumber(1).isShowingBatchPlanIcon()

        and: "The discard all button is enabled"
            scheduleDetails.batchPlan.discardButtonIsEnabled()

        and: "The inventory site and location selectors are disabled"
            scheduleDetails.batchPlan.siteSelectorIsDisabled()
            scheduleDetails.batchPlan.cabinetSelectorIsDisabled()

        and: "The growl message appears"
            scheduleDetails.batchPlan.isShowingGrowlMessage()

        and: "The growl message disappears"
            scheduleDetails.batchPlan.isNotShowingGrowlMessage()

        and: "I can unplan the item"
            planItems[0].clickMinusButton()
            planItems[0].clickMinusButton()

        and: "The status of the line changes to not started"
            scheduleDetails.getRowByLineNumber(1).statusIsNotStarted()

        and: "The discard all button is disabled"
            scheduleDetails.batchPlan.discardButtonIsDisabled()
    }


    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.IN_PROGRESS_FILTER, value = SettingsConstant.TRUE)])
    def "I can cancel discard all planned items"() {

        given: 'I am on schedule home page'
            goToSchedulePage()

        and: 'I have a medication order'
            setupOrder(Session.loggedInSiteId)
            def line1 = createMedicationOrderLine(clinicalOrder1.id, 1, ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.drugFamilyId, 30000)
            def line2 = createMedicationOrderLine(clinicalOrder1.id, 2, ItemConstants.ELOXATIN_100mg_00024_0591_20.drugFamilyId, 20000)
            createMedicationOrderLinePlan(Session.loggedInSiteId, InventorySegment.SITE_OWNED.id(), ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.ndc, 30000,  PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(),  line1)
            createMedicationOrderLinePlan(Session.loggedInSiteId, InventorySegment.SITE_OWNED.id(), ItemConstants.ELOXATIN_100mg_00024_0591_20.ndc, 20000,  PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(),  line2)
            setOnHandInventory()
            scheduleSummary.clickRefreshButton()

        and: "I click on a medication order"
            scheduleSummary.openClinicalOrderById(clinicalOrder1.id)

        and: "I select the drug name"
            scheduleDetails.getRowByLineNumber(1).clickRow()

        when: "I click the discard all button"
            scheduleDetails.batchPlan.clickDiscardAllButton()

        then: "A confirmation modal pops up"
            waitFor {confirmationModal.isOnScreen()}

        and: "The primary line on the pop up matches"
            confirmationModal.verifyPrimaryLine("Discarding will remove planning for 2 medication lines. This cannot be undone.")

        and: "The secondary line on the pop up matches"
            confirmationModal.verifySecondaryLine("Are you sure you want to discard all planning?")

        and: "The close button text matches"
            confirmationModal.verifyCloseButton("No, keep all planning and continue")

        and: "The continue button text matches"
            confirmationModal.verifyContinueButton("Yes, discard all planning for this order")

        then: "I click no to cancel the discard"
            confirmationModal.clickCloseButton()

        and: "The confirmation modal is no longer displayed"
            waitFor {confirmationModal.isOnScreen() == false}

        and: "The overlay is no longer displayed"
            waitFor {confirmationModal.overlayIsOnScreen() == false}
    }


    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.IN_PROGRESS_FILTER, value = SettingsConstant.TRUE)])
    def "I can discard all planned items"() {

        given: 'I am on schedule home page'
            goToSchedulePage()

        and: 'I have a medication order'
            setupOrder(Session.loggedInSiteId)
            def line1 = createMedicationOrderLine(clinicalOrder1.id, 1, ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.drugFamilyId, 30000)
            def line2 = createMedicationOrderLine(clinicalOrder1.id, 2, ItemConstants.ELOXATIN_100mg_00024_0591_20.drugFamilyId, 20000)
            createMedicationOrderLinePlan(Session.loggedInSiteId, InventorySegment.SITE_OWNED.id(), ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.ndc, 30000,  PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(),  line1)
            createMedicationOrderLinePlan(Session.loggedInSiteId, InventorySegment.SITE_OWNED.id(), ItemConstants.ELOXATIN_100mg_00024_0591_20.ndc, 20000,  PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(),  line2)
            scheduleSummary.clickRefreshButton()

        and: "I click on a medication order"
            scheduleSummary.openClinicalOrderById(clinicalOrder1.id)

        and: "I select the drug name"
            scheduleDetails.getRowByLineNumber(1).clickRow()

        when: "I click the discard all button"
            scheduleDetails.batchPlan.clickDiscardAllButton()

        then: "A confirmation modal pops up"
            waitFor {confirmationModal.isOnScreen()}

        and: "I click the continue button"
            confirmationModal.clickContinueButton()

        and: "The planning menu is no longer displayed"
            scheduleDetails.tabWidget.displayed

        and: "The line status has changed to not started"
            scheduleDetails.getRowByLineNumber(1).statusIsNotStarted()
            scheduleDetails.getRowByLineNumber(2).statusIsNotStarted()
    }


    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.IN_PROGRESS_FILTER, value = SettingsConstant.TRUE)])
    def "I can select done to return to history screen"(){
        given: 'I am on schedule home page'
            goToSchedulePage()

        and: 'I have a medication order'
        setupOrder(Session.loggedInSiteId)
        createMedicationOrderLine(clinicalOrder1.id, 1, ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.drugFamilyId, 30000)
        scheduleSummary.clickRefreshButton()

        and: "I click on a medication order"
        scheduleSummary.openClinicalOrderById(clinicalOrder1.id)

        and: "I select the drug name"
        scheduleDetails.getRowByLineNumber(1).clickRow()

        and: "I select the done button"
        scheduleDetails.batchPlan.clickDoneButton()

        and: "The history section is displayed"
        scheduleDetails.tabWidget.displayed
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.AUTO_PLAN, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.IN_PROGRESS_FILTER, value = SettingsConstant.TRUE)])
    def "I can autoplan when I batch plan"(){
        given: 'I am on schedule home page'
            goToSchedulePage()

        and: 'I have a medication order'
            setupOrder(Session.loggedInSiteId)
            createMedicationOrderLine(clinicalOrder1.id, 1, ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.drugFamilyId, 30000)
            scheduleSummary.clickRefreshButton()

        and: "I click on a medication order"
            scheduleSummary.openClinicalOrderById(clinicalOrder1.id)

        and: "I select the drug name"
            scheduleDetails.getRowByLineNumber(1).clickRow()

        and: "I select the cabinet"
            openAndSelectPlanCabinetSelector(PracticeConstants.CABINET_A)
        and: "I see the line status is changed to Planned"
            scheduleDetails.getRowByLineNumber(1).statusIsPlanned()
        and: "I see the planned quantity is 300 MG"
            def planItems = scheduleDetails.batchPlan.getPlanItems()
            planItems[0].verifyPlannedQuantity("300 MG")
    }
}
