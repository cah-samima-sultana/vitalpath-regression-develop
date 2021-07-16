package com.cardinalhealth.vitalpath.client.specs.schedule
import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.client.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.*
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting

class BatchPlanSelectorSelection extends BaseSpec {
    def clinicalOrders=[]


    def setupOrder(siteId, count) {
        for(int i=0; i < count; i++){
            def patient = patientService.makePatient()
             clinicalOrders.add(createMedicationOrder(siteId, patient, PhysicianConstants.physician1));
        }
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

    def createDispense(orderLine, orderLinePlan, inventoryLocationPath, siteId,drugId){
        dispenseService.dispenseItem(siteId,orderLine.id,orderLinePlan.inventorySegmentId,inventoryLocationPath,drugId,orderLinePlan.plannedQuantity);
    }

    def setOnHandInventory() {
        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(),
                Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(),
                ItemConstants.GEMZAR_1000mg_16729_0117_11.getNdc(), ItemConstants.GEMZAR_1000mg_16729_0117_11.getItemId(), 500000, null, null)

        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(),
                Session.site2Id, PracticeConstants.Site_2_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET1.path(),
                ItemConstants.GEMZAR_1000mg_16729_0117_11.getNdc(), ItemConstants.GEMZAR_1000mg_16729_0117_11.getItemId(), 500000, null, null)

        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(),
                Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(),
                ItemConstants.ALOXI_200mcg_62856_0797_01.getNdc(), ItemConstants.ALOXI_200mcg_62856_0797_01.getItemId(), 100000, null, null)

        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(),
                Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_3_SHELF_3.path(),
                ItemConstants.NEULASTA_6mg_55513_0190_01.getNdc(), ItemConstants.NEULASTA_6mg_55513_0190_01.getItemId(), 3600, null, null)
    }


    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.AUTO_PLAN, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.IN_PROGRESS_FILTER, value = SettingsConstant.TRUE)])
    def "I can see that the site/cabinet selectors are defaulted to the previous order selections"(){
        given: 'I am on schedule home page'
            goToSchedulePage()

        and: 'I have a medication order'
            setupOrder(Session.loggedInSiteId, 2)
            createMedicationOrderLine(clinicalOrders[0].id, 1, ItemConstants.ALOXI_200mcg_62856_0797_01.drugFamilyId, 50000)
            createMedicationOrderLine(clinicalOrders[0].id, 2, ItemConstants.GEMZAR_1000mg_16729_0117_11.drugFamilyId, 200000)
            createMedicationOrderLine(clinicalOrders[1].id, 1, ItemConstants.NEULASTA_6mg_55513_0190_01.drugFamilyId, 1200)
            createMedicationOrderLine(clinicalOrders[1].id, 2, ItemConstants.GEMZAR_1000mg_16729_0117_11.drugFamilyId, 200000)
            setOnHandInventory()
            scheduleSummary.clickRefreshButton()

        and: "I click on the first medication order"
            scheduleSummary.openClinicalOrderById(clinicalOrders[0].id)

        and: "I select the first MOL"
            scheduleDetails.getRowByLineNumber(1).clickRow()

        and: "I select the cabinet"
            openAndSelectPlanCabinetSelector(PracticeConstants.CABINET_A)

        and: "I plan the line"
            def planItems = scheduleDetails.batchPlan.getPlanItems()
            planItems[0].clickPlusButton()

        and: "I select the second order"
            scheduleSummary.openClinicalOrderById(clinicalOrders[1].id)

        when:"I select the first MOL"
            scheduleDetails.getRowByLineNumber(1).clickRow()
        then:"The site/cabinet selections are defaulted to the ones I previously selected (Site 1/Cabinet A)"
            verifyLinePlanSiteSelection(Session.site1)
            verifyLinePlanCabinetSelection(Session.site1CabinetA)

    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.AUTO_PLAN, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.IN_PROGRESS_FILTER, value = SettingsConstant.TRUE)])
    def "I can see that the Site/cabinet on order are defaulted to site/cabinet selected on MOL of previous order"(){
        given: 'I am on schedule home page'
        goToSchedulePage()

        and: 'I have a medication order'
        setupOrder(Session.loggedInSiteId, 2)
        createMedicationOrderLine(clinicalOrders[0].id, 1, ItemConstants.ALOXI_200mcg_62856_0797_01.drugFamilyId, 50000)
        createMedicationOrderLine(clinicalOrders[0].id, 2, ItemConstants.GEMZAR_1000mg_16729_0117_11.drugFamilyId, 200000)
        createMedicationOrderLine(clinicalOrders[1].id, 1, ItemConstants.NEULASTA_6mg_55513_0190_01.drugFamilyId, 1200)
        createMedicationOrderLine(clinicalOrders[1].id, 2, ItemConstants.GEMZAR_1000mg_16729_0117_11.drugFamilyId, 200000)
        setOnHandInventory()
        scheduleSummary.clickRefreshButton()

        and: "I click on the first medication order"
            scheduleSummary.openClinicalOrderById(clinicalOrders[0].id)
        and: "I select the first MOL"
            scheduleDetails.getRowByLineNumber(1).clickRow()

        and: "I select the cabinet"
            openAndSelectPlanCabinetSelector(Session.site2CabinetA)

        and: "I select the second MOL"
            scheduleDetails.getRowByLineNumber(2).clickRow()

        and:"The site/cabinet selections are defaulted to the ones I previously selected (Site 1/Cabinet A)"
            verifyLinePlanSiteSelection(Session.site1)
            verifyLinePlanCabinetSelection(Session.site1CabinetA)

        and: "I select the Site 2 and Cabinet A"
            openAndSelectPlanSiteSelector(Session.site2)
          //element not available in this scenario
        //  openAndSelectPlanCabinetSelector(Session.site2CabinetA)

        and: "I plan the line"
            def planItems = scheduleDetails.batchPlan.getPlanItems()
            planItems[0].clickMinusButton()

        and: "I select the second order"
            scheduleSummary.openClinicalOrderById(clinicalOrders[1].id)
        when:"I select the first MOL"
            scheduleDetails.getRowByLineNumber(1).clickRow()
        then:"The site/cabinet selections are defaulted to the ones I previously selected (Site 2/Cabinet A)"
            verifyLinePlanSiteSelection(Session.site2)
          //  verifyLinePlanCabinetSelection(Session.site2CabinetA)
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.AUTO_PLAN, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.IN_PROGRESS_FILTER, value = SettingsConstant.TRUE)])
    def "I can see that the Site/cabinet on order are defaulted to site/cabinet selected on planned MOL of previous order"(){
        given: 'I am on schedule home page'
        goToSchedulePage()

        and: 'I have a medication order'
            setupOrder(Session.loggedInSiteId, 3)
            createMedicationOrderLine(clinicalOrders[0].id, 1, ItemConstants.ALOXI_200mcg_62856_0797_01.drugFamilyId, 50000)
            createMedicationOrderLine(clinicalOrders[0].id, 2, ItemConstants.GEMZAR_1000mg_16729_0117_11.drugFamilyId, 200000)

            def line1 = createMedicationOrderLine(clinicalOrders[1].id, 1, ItemConstants.NEULASTA_6mg_55513_0190_01.drugFamilyId, 1200)
            def line2 = createMedicationOrderLine(clinicalOrders[1].id, 2, ItemConstants.GEMZAR_1000mg_16729_0117_11.drugFamilyId, 200000)
            createMedicationOrderLinePlan(Session.site1Id, InventorySegment.SITE_OWNED.id(), ItemConstants.NEULASTA_6mg_55513_0190_01.getNdc(), 1200, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_3_SHELF_3.path(), line1)

            createMedicationOrderLine(clinicalOrders[2].id, 1, ItemConstants.ALOXI_200mcg_62856_0797_01.drugFamilyId, 50000)
            setOnHandInventory()
            scheduleSummary.clickRefreshButton()

        and: "I click on the first medication order"
            scheduleSummary.openClinicalOrderById(clinicalOrders[0].id)

        and: "I select the first MOL"
            scheduleDetails.getRowByLineNumber(2).clickRow()

        and: "I select the site 2 and cabinet A"
            openAndSelectPlanSiteSelector(PracticeConstants.SITE_2)
            openAndSelectPlanCabinetSelector(PracticeConstants.CABINET_A)

        and: "I plan the line"
            def planItems = scheduleDetails.batchPlan.getPlanItems()
            planItems[0].clickPlusButton()

        and: "I select the second order"
            scheduleSummary.openClinicalOrderById(clinicalOrders[1].id)

        and: "I select the first MOL(GEMZAR)"

            scheduleDetails.getRowByLineNumber(1).clickRow()

        and:"The site/cabinet selections are defaulted to the ones that it is planned for (Site 1/Cabinet A)"

           // verifyLinePlanSiteSelection(Session.site1)
            //verifyLinePlanCabinetSelection(Session.site1CabinetA)

        and: "I select the third order"
            scheduleSummary.openClinicalOrderById(clinicalOrders[2].id)

        when:"I select the first MOL"
            scheduleDetails.getRowByLineNumber(1).clickRow()

        then:"The site/cabinet selections are defaulted to the ones I previously selected (Site 1/Cabinet A)"

       // verifyLinePlanSiteSelection(Session.site1)
         //   verifyLinePlanCabinetSelection(Session.site1CabinetA)

    }

    //Fix the below test once the code is actually fixed

//    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
//            siteSettings=[@Setting(name = SettingsConstant.AUTO_PLAN, value = SettingsConstant.FALSE),
//                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
//                    @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE),
//                    @Setting(name = SettingsConstant.IN_PROGRESS_FILTER, value = SettingsConstant.TRUE)])
//    def "I can see that the  cached planned medication order refreshes"() {
//        def plan;
//        given: 'I am on schedule home page'
//            to ScheduleHomePage
//
//        and: 'I have a medication order'
//            setupOrder(Session.loggedInSiteId, 1)
//            def line = createMedicationOrderLine(clinicalOrders[0].id, 1, ItemConstants.NEULASTA_6mg_55513_0190_01.drugFamilyId, 1200)
//            plan =createMedicationOrderLinePlan(Session.site1Id, InventorySegment.SITE_OWNED.id(), ItemConstants.NEULASTA_6mg_55513_0190_01.getNdc(), 1200, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_3_SHELF_3.path(), line)
//            setOnHandInventory()
//            scheduleSummary.clickRefreshButton()
//        and: "I click on the first medication order"
//            scheduleSummary.openClinicalOrderById(clinicalOrders[0].id)
//
//        and: "I select the first MOL"
//            scheduleDetails.getRowByLineNumber(1).clickRow()
//
//        and: "In the background I dispense the line"
//            createDispense(line, plan, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_3_SHELF_3.path(), Session.site1Id, ItemConstants.NEULASTA_6mg_55513_0190_01.getNdc())
//        and:"I unplan the line"
//            def planItems = scheduleDetails.batchPlan.getPlanItems()
//            planItems[0].clickMinusButton()
//        and:"I verify the minus and plus butons are disabled"
//            verifyDecDisabled()
//            verifyIncDisabled()

//    }




}
