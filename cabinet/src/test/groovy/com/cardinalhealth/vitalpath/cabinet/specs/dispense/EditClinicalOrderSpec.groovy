package com.cardinalhealth.vitalpath.cabinet.specs.dispense

import com.cardinalhealth.vitalpath.EnvironmentProperties
import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.annotations.Smoke
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispenseHomePage
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispensePlanPage
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.PlanPickPage
import com.cardinalhealth.vitalpath.cabinet.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.*
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting
import org.apache.commons.lang.StringUtils
import spock.lang.IgnoreRest
import spock.lang.Shared

class EditClinicalOrderSpec extends BaseSpec {

    @Shared patient1
    @Shared patient2

    def order1
    def order2

    def order1Line1, order1Line2, order2Line1, order2Line2

    def gemzarPlanItemId

    def setup(){
        patient1 = patientService.makePatient()
        patient2 = patientService.makePatient()

        def siteOwned = InventorySegment.SITE_OWNED.id()

        setOnHandInventory(siteOwned, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.GEMZAR_1000mg_16729_0117_11.getNdc(), ItemConstants.GEMZAR_1000mg_16729_0117_11.getItemId(), 5000, null, null)
        setOnHandInventory(siteOwned, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(), ItemConstants.ALOXI_200mcg_62856_0797_01.getNdc(), ItemConstants.ALOXI_200mcg_62856_0797_01.getItemId(), 1000, null, null)

        setOnHandInventory(siteOwned, PracticeConstants.Site_2_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET1.path(), ItemConstants.GEMZAR_1000mg_16729_0117_11.getNdc(), ItemConstants.GEMZAR_1000mg_16729_0117_11.getItemId(), 5000, null, null)
        setOnHandInventory(siteOwned, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_3_SHELF_3.path(), ItemConstants.NEULASTA_6mg_55513_0190_01.getNdc(), ItemConstants.NEULASTA_6mg_55513_0190_01.getItemId(), 36, null, null)

        // Order 1
        order1 = medicationOrderService.makeMedicationOrder(patient1, PhysicianConstants.physician1, System.currentTimeMillis(), Session.site1Id)
        order1Line1 = medicationOrderService.addMedicationLine(DrugFamily.GEMZAR.id(), 1, 2000 * 100, 'IV', order1.id)
        order1Line2 = medicationOrderService.addMedicationLine(DrugFamily.ALOXI.id(), 2, 500 * 100, 'IV', order1.id)

        def gemzarItemId = itemService.findItemIdByNDC(ItemConstants.GEMZAR_1000mg_16729_0117_11.ndc)
        def gemzarInventoryLocationId = inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), Session.site1Id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(gemzarItemId, gemzarInventoryLocationId, InventorySegment.SITE_OWNED.id(), null, 2000 * 100, Session.site1Id, order1Line1)

        gemzarPlanItemId = getPlanItemDataId(order1Line1.id, gemzarInventoryLocationId, gemzarItemId, siteOwned, null)

        def aloxiItemId = itemService.findItemIdByNDC(ItemConstants.ALOXI_200mcg_62856_0797_01.ndc)
        def aloxiInventoryLocationId = inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(), Session.site1Id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(aloxiItemId, aloxiInventoryLocationId, InventorySegment.SITE_OWNED.id(), null, 500 * 100, Session.site1Id, order1Line2)

        // Order 2
        order2 = medicationOrderService.makeMedicationOrder(patient2, PhysicianConstants.physician1, System.currentTimeMillis(), Session.site1Id)
        order2Line1 = medicationOrderService.addMedicationLine(DrugFamily.GEMZAR.id(), 1, 2000 * 100, 'IV', order2.id)
        order2Line2 = medicationOrderService.addMedicationLine(DrugFamily.NEULASTA.id(), 2, 12 * 100, 'IV', order2.id)

        def neulastaItemId = itemService.findItemIdByNDC(ItemConstants.NEULASTA_6mg_55513_0190_01.ndc)
        def neulastaInventoryLocationId = inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_3_SHELF_3.path(), Session.site1Id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(neulastaItemId, neulastaInventoryLocationId, InventorySegment.SITE_OWNED.id(), null, 12 * 100, Session.site1Id, order2Line2)

        def location2 = Session.site2Id
        def gemzarInventoryLocation2Id = inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_2_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET1.path(), location2)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(gemzarItemId, gemzarInventoryLocation2Id, InventorySegment.SITE_OWNED.id(), null, 2000 * 100, location2, order2Line1)
    }

    def setOnHandInventory(drugFamilyGroupId, location, ndc, itemId, quantity, expires, patientId){
        def locationId = inventoryLocationService.findInventoryLocationIdByPath(location, Session.site1Id)
        println "${location} - ${locationId}"
        def inventory = inventoryService.getInventory(drugFamilyGroupId, ndc, locationId, itemId, patientId)

        if(inventory != null)
            inventoryAdjustmentService.addInventory(Session.site1Id, locationId, drugFamilyGroupId, ndc, itemId,  -inventory.quantity, null, patientId)

        inventoryAdjustmentService.addInventory(Session.site1Id, locationId, drugFamilyGroupId, ndc, itemId, quantity * 100, expires, patientId)
    }

    def createMedicationOrderLine(drugFamilyId, lineNumber, quantity, orderId) {
        return medicationOrderService.addMedicationLine(drugFamilyId, lineNumber, quantity, "IV", orderId)
    }

    def getPlanItemDataId(clinicalOrderLineId, inventoryLocationId, itemId, inventorySegmentId, patientId){
        if(StringUtils.isEmpty(patientId)){
            patientId = "***"
        }

        String[] keys = [clinicalOrderLineId, inventoryLocationId, itemId, inventorySegmentId, patientId];

        return StringUtils.join(keys, "~")
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.PLAN_DISPENSE_SORT, value = SettingsConstant.PlanDispenseSort.LAW)
            ])
    def "Select Planned MO #1 card at the Cabinet, changes to the planned amount are not persisted"() {
        given: "I am logged on to Site 1/Cabinet A"
            goToDispensePage()

        and: "Select a MO card that has batch planned MOLs"
            clickRefreshButton()
            selectFirstCardByMrn(patient1.accountNumber)

        when: "the planning screen displays"
            at DispensePlanPage

        def planItem = medicationOrderPlan.getPlanItem(1)

        then: "the MOLs planned for the Site 1/Cabinet A are respected"
            medicationOrderPlan.verifyPlanLineCount(2)

        and: "the planned amount matches the planned amount (2000 MG)"
            planItem.verifyPlannedQuantity("2000 MG")

        and: "the NDC matches the NDC planned"
            planItem.verifyNdc(ItemConstants.GEMZAR_1000mg_16729_0117_11.ndc)
            planItem.verifyItemName(ItemConstants.GEMZAR_1000mg_16729_0117_11.name)

        and: "the inventory location matches the inventory location planned"
            planItem.verifyInventoryLocationName(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.displayPath())

        and: "the + button is disabled, the - button is enabled"
            planItem.verifyIncDisabled()
            planItem.verifyDecEnabled()

//        and: "the edit button is disabled"
//        verifyEditDisabled()

        and: "I unplan line 1"
        planItem.clickMinusButton()
        planItem.clickMinusButton()

//        and: "the edit button is enabled"
//        verifyEditEnabled()

        and: "I select line 2"
        selectPlanLine(2)

        def planItem2 = medicationOrderPlan.getPlanItem(1)

        and: "I unplan line 2"
        planItem2.clickMinusButton()
        planItem2.clickMinusButton()

        when: "I select Home"
        clickHomeButton()

        and: "I am at the dispense home page"
        at DispenseHomePage

        waitFor{$(".medicationOrderCard")}

        then: "the MO remains in planned status"
        def order = getFirstCardByMrn(patient1.accountNumber)
        order.isPlanned() == true

        when: "I select batch pick"
//        waitFor{ !$("[data-id='batchPickButton']").hasClass("isDisabled") }
        clickBatchButton()

        and: "I am at the pick screen"
        at PlanPickPage

        and: "the patient expected is selected"
        waitForAnimationToComplete()
        planPick.skipUntilPatientFound(patient1.accountNumber)

        then: "both MOL display"
        waitForAnimationToComplete()
        verifyNumberOfPlanPickItems(2)
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.PLAN_DISPENSE_SORT, value = SettingsConstant.PlanDispenseSort.LAW)
            ])
    def "Select Planned MO #1 card at the Cabinet and do not make any changes"(){
        given: "I am logged on to Site 1/Cabinet A"
            goToDispensePage()

        and: "Select a MO card that has batch planned MOLs"
            clickRefreshButton()
            selectFirstCardByMrn(patient1.accountNumber)

        and: "the planning screen displays"
            at DispensePlanPage

        and: "the MOLs planned for the Site 1/Cabinet A are respected"
            medicationOrderPlan.verifyPlanLineCount(2)

        and: "I do not make any changes to the MO"
        when: "I select Home"
            clickHomeButton()

        and: "I am at the dispense home page"
            at DispenseHomePage

        waitFor{$(".medicationOrderCard")}

        then: "the MO remains in planned status"
            def order = getFirstCardByMrn(patient1.accountNumber)
            order.isPlanned() == true

    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.PLAN_DISPENSE_SORT, value = SettingsConstant.PlanDispenseSort.LAW)
            ])
    def "Select Planned MO #2 card with 1 MOL planned at current site/cabinet & 1 MOL planned at another site/cabinet"(){
        given: "I am logged on to Site 1/Cabinet A"
        goToDispensePage()

        and: "Select a MO card that has batch planned MOLs"
        clickRefreshButton()
        selectFirstCardByMrn(patient2.accountNumber)

        and: "the planning screen displays"
        at DispensePlanPage

        def planItem = medicationOrderPlan.getPlanItem(1)

        and: "the MOLs planned for the Site 1 are respected"
        medicationOrderPlan.verifyPlanLineCount(2)

        and: "the planned amount matches the planned amount (2000 MG)"
        planItem.verifyPlannedQuantity("2000 MG")

        and: "the NDC matches the NDC planned"
        planItem.verifyNdc(ItemConstants.GEMZAR_1000mg_16729_0117_11.ndc)
        planItem.verifyItemName(ItemConstants.GEMZAR_1000mg_16729_0117_11.name)

        and: "the inventory location matches the inventory location planned"
        planItem.verifyInventoryLocationName(PracticeConstants.Site_2_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET1.displayPath())

        and: "only 1 inventory card is displayed for Gemzar"
        medicationOrderPlan.verifyPlanItemCount(1)

        and: "on hand displays as N/A since it is planned for another site & cabinet"
        planItem.verifyOnHandQuantity("Not Applicable")

        and: "the + and - buttons are disabled"
        planItem.verifyIncDisabled()
        planItem.verifyDecDisabled()

        and: "and a banner displays above the inventory cards"
        planItem.verifyBannerText("Drug planned for ${EnvironmentProperties.instance.browserVersion()} Site 2 - Cabinet A".toUpperCase())

        and: "I select the second line (Neulasta)"
        selectPlanLine(2)
        planItem = medicationOrderPlan.getPlanItem(1)

        and: "the planned amount matches the planned amount (12 MG)"
        planItem.verifyPlannedQuantity("12 MG")

        and: "the NDC matches the NDC planned"
        planItem.verifyNdc(ItemConstants.NEULASTA_6mg_55513_0190_01.ndc)
        planItem.verifyItemName(ItemConstants.NEULASTA_6mg_55513_0190_01.name)

        and: "the inventory location matches the inventory location planned"
        planItem.verifyInventoryLocationName(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_3_SHELF_3.displayPath())

        and: "the + button is disabled, the - button is enabled"
        planItem.verifyIncDisabled()
        planItem.verifyDecEnabled()
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.PLAN_DISPENSE_SORT, value = SettingsConstant.PlanDispenseSort.LAW),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)
            ])
    def "Select Planned MO #1 card & update quantity of MOL"(){
        given: "I am logged on to Site 1/Cabinet A"
        goToDispensePage()

        and: "Select a MO card that has batch planned MOLs"
        clickRefreshButton()
        selectFirstCardByMrn(patient1.accountNumber)

        and: "the planning screen displays"
        at DispensePlanPage

        def planItem = medicationOrderPlan.getPlanItem(1)

        and: "the planned amount matches the planned amount (2000 MG)"
        planItem.verifyPlannedQuantity("2000 MG")

        and: "I unplan the line"
        planItem.clickMinusButton()
        planItem.clickMinusButton()

        and: "I select Edit for the first MOL of Gemzar 2000 MG"
        clickEditLineButton()

        when: "I update the quantity to 3000 MG"
        editDoseOnSelectedLine(3000)

        then: "the MOL is no longer planned"
        waitForAnimationToComplete()
        isLineNotStarted(1)

        and: "my other MOL was not changed"
        isLinePlanned(2)

        when: "I select Home"
        clickHomeButton()

        and: "I am at the dispense home page"
        at DispenseHomePage
        clickRefreshButton()

        waitFor{$(".medicationOrderCard")}

        then: "the MO remains in planned status"
        def order = getFirstCardByMrn(patient1.accountNumber)
        order.isPlanned() == true

        when: "I select batch pick"
        waitForBatchPickEnabled()
        clickBatchButton()

        and: "I am at the pick screen"
        at PlanPickPage

        and: "the patient expected is selected"
        waitFor{ $("[data-id='skip-button']").displayed }
        waitForAnimationToComplete()
        skipUntilPatientFound(patient1.accountNumber)

        then: "only one MOL displays"
            verifyNumberOfPlanPickItems(1)

    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.PLAN_DISPENSE_SORT, value = SettingsConstant.PlanDispenseSort.LAW),
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.AUTO_PLAN, value = SettingsConstant.FALSE)
            ])
    def "Select Planned MO #1 card & Add MOL"(){
        given: "I am logged on to Site 1/Cabinet A"
        goToDispensePage()

        and: "Select a MO card that has batch planned MOLs"
        clickRefreshButton()
        selectFirstCardByMrn(patient1.accountNumber)

        and: "the planning screen displays"
        at DispensePlanPage

        and: "I select Add"
        clickAddLineButton()

        when: "I enter Neulasta 6MG Intravenous and save line/close"
        enterNewLineDetails("Neulasta", 6, "Intravenous")

        and: "the new MOL of Neulasta I added has a status of not started"
        medicationOrderPlan.verifyPlanLineCount(3)
        isLineNotStarted(3)

        and: "I fully plan the new MOL of Neulasta"
        selectPlanLine(3)
        planFirstLine()

        and: "my other MOLs (Gemzar & Aloxi) that I planned still have a status of Planned"
        isLinePlanned(1)
        isLinePlanned(2)

        and: "I navigate back to the dispense Home screen"
        clickHomeButton()
        at DispenseHomePage

        waitFor{$(".medicationOrderCard")}

        then: "the badge displayed is Planned"
        def order = getFirstCardByMrn(patient1.accountNumber)
        order.isPlanned() == true

        when: "I select the MO"
        selectFirstCardByMrn(patient1.accountNumber)
        at DispensePlanPage

        then: "I select the line of Neulasta"
        selectPlanLine(3)
        def planItem = medicationOrderPlan.getPlanItem(1)

        and: "the plan for Neulasta has a status of Not Started"
        isLineNotStarted(3)

        and: "the plan was not saved"
        planItem.verifyPlannedQuantity("0 MG")
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.PLAN_DISPENSE_SORT, value = SettingsConstant.PlanDispenseSort.LAW),
                    @Setting(name = SettingsConstant.DISPENSE_DOOR_SELECT, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)
            ])
    def "Select Batch Pick for MO #1 with MOL added at the Cabinet"(){
        given: "I am logged on to Site 1/Cabinet A"
        goToDispensePage()

        and: "I have a MO that has multiple lines that were already planned (Aloxi & Gemzar)"
        clickRefreshButton()
        selectFirstCardByMrn(patient1.accountNumber)

        and: "the planning screen displays"
        at DispensePlanPage

        and: "I select Add"
        clickAddLineButton()

        when: "I enter Neulasta 6MG Intravenous and save line/close"
        enterNewLineDetails("Neulasta", 6, "Intravenous")

        and: "my other MOLs (Gemzar & Aloxi) that I planned still have a status of Planned"
        isLinePlanned(1)
        isLinePlanned(2)

        and: "I navigate back to the dispense Home screen"
        clickHomeButton()
        at DispenseHomePage

        waitFor{$(".medicationOrderCard")}

        and: "I select batch plan"
        clickBatchButton()

        and: "I am at the pick screen"
        at PlanPickPage

        and: "the patient expected is selected"
        waitFor{ $("[data-id='skip-button']").displayed }
        waitForAnimationToComplete()
        skipUntilPatientFound(patient1.accountNumber)

        then: "the MOLs in planned status display (Gemzar & Neulasta)"
            verifyNumberOfPlanPickItems(2)
            verifyPickItemExistByNdc(ItemConstants.GEMZAR_1000mg_16729_0117_11.getNdc())
            verifyPickItemExistByNdc(ItemConstants.ALOXI_200mcg_62856_0797_01.getNdc())
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.PLAN_DISPENSE_SORT, value = SettingsConstant.PlanDispenseSort.LAW),
                    @Setting(name = SettingsConstant.DISPENSE_DOOR_SELECT, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)
            ])
    def "Select Planned MO #1 card & select pick"(){
        given: "I am logged on to Site 1/Cabinet A"
        goToDispensePage()

        and: "I have a MO that has multiple lines that were already planned (Aloxi & Gemzar)"
        clickRefreshButton()
        selectFirstCardByMrn(patient1.accountNumber)

        and: "the planning screen displays"
        at DispensePlanPage

        when: "I select Pick"
        clickPlanPickButton()
        at PlanPickPage

        then: "the MOLs for Gemzar and Aloxi are displayed"
        verifyPickItemExistByNdc(ItemConstants.GEMZAR_1000mg_16729_0117_11.getNdc())
        verifyPickItemExistByNdc(ItemConstants.ALOXI_200mcg_62856_0797_01.getNdc())

        and: "the Planned amount for Gemzar is 2000 MG"
        verifyPickItemPlannedQuantity(ItemConstants.GEMZAR_1000mg_16729_0117_11.getNdc(), "2000")

        and: "the Planned amount for Aloxi is 500 MCG"
        verifyPickItemPlannedQuantity(ItemConstants.ALOXI_200mcg_62856_0797_01.getNdc(), "500")

        and: "I can confirm the inventory cards"
        confirmFirstPlanPickItem()

        and: "I can confirm all the inventory cards"
        clickConfirmAllButton()
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.PLAN_DISPENSE_SORT, value = SettingsConstant.PlanDispenseSort.LAW),
                    @Setting(name = SettingsConstant.DISPENSE_DOOR_SELECT, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)
            ])
    def "Select Planned MO #1 card, select Pick, select Cancel on inventory card"() {
        given: "I am logged on to Site 1/Cabinet A"
        goToDispensePage()

        and: "I have a MO that has multiple lines that were already planned (Aloxi & Gemzar)"
        clickRefreshButton()
        selectFirstCardByMrn(patient1.accountNumber)

        and: "the planning screen displays"
        at DispensePlanPage

        and: "I select - for Gemzar"
        selectPlanLine(1)
        def planItem = medicationOrderPlan.getPlanItem(1)
        planItem.clickMinusButton()

        and: "I select Pick"
        clickPlanPickButton()
        at PlanPickPage

        and: "I select confirm for Aloxi"
        confirmPlanPickItemByNdc(ItemConstants.ALOXI_200mcg_62856_0797_01.getNdc())

        when: "I select cancel for Gemzar"
        waitFor{$("[name=cancelButton]").first().hasClass("isDisabled") == false}
        cancelFirstPlanPickItem()

        then: "I am returned to the planning screen"
        at DispensePlanPage

        and: "the MOL with Aloxi is in a status of Complete"
        isLineComplete(2)

        and: "the MOL with Gemzar is in a status of Planned"
        isLinePlanned(1)

        and: "my Planned MOL of Gemzar retains the planned amount from the PC Client (2000 MG)"
        selectPlanLine(1)
        def planItem2 = medicationOrderPlan.getPlanItem(1)
        planItem2.verifyPlannedQuantity("2000 MG")
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.PLAN_DISPENSE_SORT, value = SettingsConstant.PlanDispenseSort.LAW),
                    @Setting(name = SettingsConstant.DISPENSE_DOOR_SELECT, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)
            ])
    def "Select Planned MO #1 card, select Pick, select Back"() {
        given: "I am logged on to Site 1/Cabinet A"
        goToDispensePage()

        and: "I have a MO that has multiple lines that were already planned (Aloxi & Gemzar)"
        clickRefreshButton()
        waitFor{$(".medicationOrderCard")}
        selectFirstCardByMrn(patient1.accountNumber)

        and: "the planning screen displays"
        at DispensePlanPage

        and: "I select - for Gemzar"
        selectPlanLine(1)
        def planItem = medicationOrderPlan.getPlanItem(1)
        planItem.clickMinusButton()

        and: "I select Pick"
        clickPlanPickButton()
        at PlanPickPage

        when: "I select Back"
        waitForAnimationToComplete()
        clickBackButton()

        then: "I am returned to the planning screen"
        at DispensePlanPage

        and: "the MOLs (Gemzar & Aloxi) retain the planned amounts (2000 MG and 500 MCG)"
        selectPlanLine(1)
        def planItem2 = medicationOrderPlan.getPlanItem(1)
        planItem2.verifyPlannedQuantity("2000 MG")
        selectPlanLine(2)
        def planItem3 = medicationOrderPlan.getPlanItem(1)
        planItem3.verifyPlannedQuantity("500 MCG")

        and: "I go back to the dispense home screen"
        clickHomeButton()
        at DispenseHomePage

        waitFor{$(".medicationOrderCard")}

        and: "I select batch pick"
        clickBatchButton()
        at PlanPickPage
        waitForCards()

        and: "the pick items (Gemzar & Aloxi) retain the planned amounts (2000 MG and 500 MCG)"
        verifyPickItemPlannedQuantity(ItemConstants.GEMZAR_1000mg_16729_0117_11.getNdc(), "2000")
        verifyPickItemPlannedQuantity(ItemConstants.ALOXI_200mcg_62856_0797_01.getNdc(), "500")

    }
}
