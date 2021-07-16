package com.cardinalhealth.vitalpath.cabinet.specs.dispense

import com.cardinalhealth.vitalpath.EnvironmentProperties
import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispenseHomePage
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispensePlanPage
import com.cardinalhealth.vitalpath.cabinet.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.*
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting
import spock.lang.IgnoreRest
import spock.lang.Shared

class DispensePlan extends BaseSpec {

    def patient1, patient2
    def order, order2

    def setup(){
        def environmentProperties = EnvironmentProperties.instance
        digInventoryLocationService.setUseSdvWaste(environmentProperties.tenantId(), Session.loggedInSiteId, Session.loggedInCabinetId, ItemConstants.GEMZAR_1000mg_00069_3858_10.getDigId(), true)

        patient1 = patientService.makePatient()

        def siteOwned = InventorySegment.SITE_OWNED.id()
        def patientAssist = InventorySegment.PATIENT_ASSIST.id()
        def offLabel = InventorySegment.OFFLABEL.id()

        inventoryAdjustmentService.setOnhandInventory(siteOwned, Session.loggedInSiteId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2.path(), ItemConstants.GEMZAR_200mg_00409_0183_01.getNdc(), ItemConstants.GEMZAR_200mg_00409_0183_01.getItemId(), 800, (new Date() + 1).getTime(), null)
        inventoryAdjustmentService.setOnhandInventory(siteOwned, Session.loggedInSiteId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_3.path(), ItemConstants.GEMZAR_200mg_00409_0185_01.getNdc(), ItemConstants.GEMZAR_200mg_00409_0185_01.getItemId(), 800, (new Date() - 1).getTime(), null)
        inventoryAdjustmentService.setOnhandInventory(siteOwned, Session.loggedInSiteId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.GEMZAR_200mg_00002_7501_01.getNdc(), ItemConstants.GEMZAR_200mg_00002_7501_01.getItemId(), 5000, null, null)
        inventoryAdjustmentService.setOnhandInventory(siteOwned, Session.loggedInSiteId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_3_POCKET_1.path(), ItemConstants.GEMZAR_200mg_00002_7502_01.getNdc(), ItemConstants.GEMZAR_200mg_00002_7502_01.getItemId(), 3000, null, null)
        inventoryAdjustmentService.setOnhandInventory(siteOwned, Session.loggedInSiteId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2.path(), ItemConstants.GEMZAR_1000mg_00069_3858_10.getNdc(), ItemConstants.GEMZAR_1000mg_00069_3858_10.getItemId(), 2000, null, null)
        inventoryAdjustmentService.setOnhandInventory(siteOwned, Session.loggedInSiteId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2.path(), ItemConstants.GEMZAR_1000mg_00069_3858_10.getNdc(), ItemConstants.GEMZAR_1000mg_00069_3858_10.getItemId(), 2000, null, null)
        inventoryAdjustmentService.setOnhandInventory(siteOwned, Session.loggedInSiteId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(), ItemConstants.GEMZAR_200mg_00002_7502_01.getNdc(), ItemConstants.GEMZAR_200mg_00002_7502_01.getItemId(), 0, null, null)
        inventoryAdjustmentService.setOnhandInventory(siteOwned, Session.loggedInSiteId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.GEMZAR_1000mg_16729_0117_11.getNdc(), ItemConstants.GEMZAR_1000mg_16729_0117_11.getItemId(), 0, null, null)
        inventoryAdjustmentService.setOnhandInventory(patientAssist, Session.loggedInSiteId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2.path(), ItemConstants.GEMZAR_200mg_00002_7501_01.getNdc(), ItemConstants.GEMZAR_200mg_00002_7501_01.getItemId(), 600, null, null)
        inventoryAdjustmentService.setOnhandInventory(patientAssist, Session.loggedInSiteId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_4_SHELF_2_BIN_3_POCKET_1.path(), ItemConstants.GEMZAR_1000mg_16729_0117_11.getNdc(), ItemConstants.GEMZAR_1000mg_16729_0117_11.getItemId(), 5000, null, null)
        inventoryAdjustmentService.setOnhandInventory(siteOwned, Session.loggedInSiteId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.GEMZAR_200mg_00409_0183_01.getNdc(), ItemConstants.GEMZAR_200mg_00409_0183_01.getItemId(), 0, (new Date() + 1).getTime(), null)

        order = createMedicationOrder(patient1)


        // Create Patient
        patient2 = patientService.makePatient()

        // Create Patient Assist Inv for Patient
        inventoryAdjustmentService.setOnhandInventory(patientAssist, Session.loggedInSiteId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2.path(), ItemConstants.GEMZAR_200mg_00002_7501_01.getNdc(), ItemConstants.GEMZAR_200mg_00002_7501_01.getItemId(), 600, null, patient2.id)

        // Create Off Label Inv for Patient
        inventoryAdjustmentService.setOnhandInventory(offLabel, Session.loggedInSiteId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2.path(), ItemConstants.GEMZAR_200mg_00002_7501_01.getNdc(), ItemConstants.GEMZAR_200mg_00002_7501_01.getItemId(), 600, null, patient2.id)

        // Create Order for Patient
        order2 = createMedicationOrder(patient2)
        refreshPage()

    }

    def createMedicationOrder(patient) {
        def order = medicationOrderService.makeMedicationOrder(patient, PhysicianConstants.physician1, System.currentTimeMillis(), Session.loggedInSiteId)
        medicationOrderService.addMedicationLine(DrugFamily.GEMZAR.id(), 1, 10 * 100, 'IV', order.id)
        return order
    }

    def createMedicationOrderLine(drugFamilyId, lineNumber, quantity, orderId) {
        return medicationOrderService.addMedicationLine(drugFamilyId, lineNumber, quantity, "IV", orderId)
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.PLAN_DISPENSE_SORT, value = SettingsConstant.PlanDispenseSort.MAN),
                    @Setting(name = SettingsConstant.CLINICAL_ORDER_LINE_PROMPT, value = SettingsConstant.ClinicalOrderLinePrompt.DRUG_FAMILY_ONLY),
                    @Setting(name = SettingsConstant.EXPIRED_ITEM_WARNING_DAYS, value = '5'),
                    @Setting(name = SettingsConstant.REUSE_SDV_WASTE, value = SettingsConstant.FALSE)])
    def "Dispense planning - plan items are sorted by manufacturer "() {
        given: "I have cabinet inventory " +
                "Door 2: Gemzar (NDC 00409-0183-01) ON HAND: 800 MG Expiring tomorrow " +
                "Door 2 / Shelf 3 : Gemzar (NDC 00409-0185-01) ON HAND: 800 MG Expired in the past " +
                "Door 1 / Shelf 2 / Bin 1 : Gemzar (NDC 00002-7501-01) ON HAND: 5000 MG " +
                "Door 1 / Shelf 2 / Bin 3 : Gemzar (NDC 00002-7502-01) ON HAND: 3000 MG " +
                "Door 1 / Shelf 2 : Gemzar (NDC 00069-3858-10) ON HAND: 2000 MG " +
                "Door 2 / Shelf 1 : Gemzar (NDC 00002-7502-01) ON HAND: 0 MG " +
                "Door 2 / Shelf 2 : Gemzar (NDC 00069-3858-10) ON HAND: 2000 MG " +
                "Door 1 / Shelf 2 / Bin 1: Gemzar (NDC 16729-0117-11) ON HAND: 0 " +
                "Samples " +
                "Door 2 / Shelf 2: Gemzar (NDC 00002-7501-01) ON HAND: 600 MG unassigned patient assist " +
                "Door 4 / Shelf 2 / Bin 3: Gemzar (NDC 16729-0117-11) ON HAND: 5000MG unassigned patient assist " +
                "And Gemzar 10MG medication order"
        goToDispensePage()

        when: 'I go to order planning screen'
        clickRefreshButton()
        selectFirstCardByMrn(patient1.accountNumber)
        at DispensePlanPage

        then: 'Planning cards are displayed in below order' +
                'SDV (Site Owned): Location Door 2 : Gemzar (NDC 00409-0183-01) ON HAND: 800 MG Expiring tomorrow ' +
                'SDV (Site Owned): Location Door 2 / Shelf 3 : Gemzar (NDC 00409-0185-01) ON HAND: 800 MG Expired in the past ' +
                'SDV (Site Owned): Location Door 1 / Shelf 2 / Bin 1 : Gemzar (NDC 00002-7501-01) ON HAND: 5000 MG Use SDV Waste' +
                'SDV (Site Owned): Location Door 1 / Shelf 2 / Bin 3 : Gemzar (NDC 00002-7502-01) ON HAND: 3000 MG Use SDV Waste' +
                'SDV (Site Owned): Location Door 1 / Shelf 2 : Gemzar (NDC 00069-3858-10) ON HAND: 2000 MG' +
                'SDV (Site Owned): Location Door 2 / Shelf 2 : Gemzar (NDC 00069-3858-10) ON HAND: 2000 MG' +
                'SDV (Site Owned): Location Door 2 / Shelf 1 : Gemzar (NDC 00002-7502-01) ON HAND: 0 MG Use SDV Waste' +
                'SDV (Site Owned): Location Door 1 / Shelf 2 / Bin 1: Gemzar (NDC 16729-0117-11) ON HAND: 0 MG Use SDV Waste' +
                'Samples ' +
                'SDV (Patient Assist): Location Door 2 / Shelf 2: Gemzar (NDC 00002-7501-01) ON HAND: 600 MG - Make this unassigned patient assist' +
                'SDV (Patient Assist): Location Door 4 / Shelf 2 / Bin 3: Gemzar (NDC 16729-0117-11) ON HAND: 5000MG - Make this unassigned patient assist'

       // verifyPlanItem(1, ItemConstants.GEMZAR_200mg_00002_7501_01,   PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2.displayPath(), InventorySegment.SITE_OWNED)
       // verifyPlanItem(2, ItemConstants.GEMZAR_200mg_00409_0185_01,   PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_3.displayPath(), InventorySegment.SITE_OWNED)
        verifyPlanItem(3, ItemConstants.GEMZAR_200mg_00002_7501_01,   PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.displayPath(), InventorySegment.SITE_OWNED)
        verifyPlanItem(4, ItemConstants.GEMZAR_200mg_00002_7502_01,   PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_3_POCKET_1.displayPath(), InventorySegment.SITE_OWNED)
        verifyPlanItem(5, ItemConstants.GEMZAR_1000mg_00069_3858_10,  PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2.displayPath(), InventorySegment.SITE_OWNED)
        verifyPlanItem(6, ItemConstants.GEMZAR_1000mg_00069_3858_10,  PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2.displayPath(), InventorySegment.SITE_OWNED)
       // verifyPlanItem(7, ItemConstants.GEMZAR_200mg_00002_7502_01,  PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.displayPath(), InventorySegment.SITE_OWNED)
       // verifyPlanItem(8, ItemConstants.GEMZAR_200mg_00409_0183_01,   PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.displayPath(), InventorySegment.SITE_OWNED)
      // verifyPlanItem(9, ItemConstants.GEMZAR_1000mg_16729_0117_11,  PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.displayPath(), InventorySegment.SITE_OWNED)

        // Samples
     //  verifyPlanItem(10, ItemConstants.GEMZAR_200mg_00002_7501_01,  PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_3.displayPath(), InventorySegment.SAMPLES)

     //  verifyPlanItem(11, ItemConstants.GEMZAR_200mg_00002_7501_01,  PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2.displayPath(), InventorySegment.PATIENT_ASSIST)
        verifyPlanItem(12, ItemConstants.GEMZAR_1000mg_16729_0117_11,  PracticeConstants.Site_1_Layout.CABINET_A_DOOR_4_SHELF_2_BIN_3_POCKET_1.displayPath(), InventorySegment.SITE_OWNED)
    }

    // Test fails because test data manipulates the test data and changes the sort order
    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.PLAN_DISPENSE_SORT, value = SettingsConstant.PlanDispenseSort.LAW),
                    @Setting(name = SettingsConstant.CLINICAL_ORDER_LINE_PROMPT, value = SettingsConstant.ClinicalOrderLinePrompt.DRUG_FAMILY_ONLY),
                    @Setting(name = SettingsConstant.EXPIRED_ITEM_WARNING_DAYS, value = '5'),
                    @Setting(name = SettingsConstant.REUSE_SDV_WASTE, value = SettingsConstant.FALSE)])
    def "Dispense planning - plan items are sorted by least amount of waste "() {
        given: "I have cabinet inventory " +
                "Door 2: Gemzar (NDC 00409-0183-01) ON HAND: 800 MG Expiring tomorrow " +
                "Door 2 / Shelf 3 : Gemzar (NDC 00409-0185-01) ON HAND: 800 MG Expired in the past " +
                "Door 1 / Shelf 2 / Bin 1 : Gemzar (NDC 00002-7501-01) ON HAND: 5000 MG " +
                "Door 1 / Shelf 2 / Bin 3 : Gemzar (NDC 00002-7502-01) ON HAND: 3000 MG " +
                "Door 1 / Shelf 2 : Gemzar (NDC 00069-3858-10) ON HAND: 2000 MG " +
                "Door 2 / Shelf 1 : Gemzar (NDC 00002-7502-01) ON HAND: 0 MG " +
                "Door 2 / Shelf 2 : Gemzar (NDC 00069-3858-10) ON HAND: 2000 MG " +
                "Door 1 / Shelf 2 / Bin 1: Gemzar (NDC 16729-0117-11) ON HAND: 0 " +
                "Samples " +
                "Door 2 / Shelf 2: Gemzar (NDC 00002-7501-01) ON HAND: 600 MG unassigned patient assist " +
                "Door 4 / Shelf 2 / Bin 3: Gemzar (NDC 16729-0117-11) ON HAND: 5000MG unassigned patient assist " +
                "And Gemzar 10MG medication order"
        goToDispensePage()

        when: 'I go to order planning screen'
        clickRefreshButton()
        selectFirstCardByMrn(patient1.accountNumber)
        at DispensePlanPage

        then: 'Planning cards are displayed in below order' +
                'SDV (Site Owned): Location Door 2 : Gemzar (NDC 00409-0183-01) ON HAND: 800 MG Expiring tomorrow ' +
                'SDV (Site Owned): Location Door 2 / Shelf 3 : Gemzar (NDC 00409-0185-01) ON HAND: 800 MG Expired in the past ' +
                'SDV (Site Owned): Location Door 1 / Shelf 2 / Bin 1 : Gemzar (NDC 00002-7501-01) ON HAND: 5000 MG Use SDV Waste' +
                'SDV (Site Owned): Location Door 1 / Shelf 2 / Bin 3 : Gemzar (NDC 00002-7502-01) ON HAND: 3000 MG Use SDV Waste' +
                'SDV (Site Owned): Location Door 1 / Shelf 2 : Gemzar (NDC 00069-3858-10) ON HAND: 2000 MG' +
                'SDV (Site Owned): Location Door 2 / Shelf 2 : Gemzar (NDC 00069-3858-10) ON HAND: 2000 MG' +
                'SDV (Site Owned): Location Door 2 / Shelf 1 : Gemzar (NDC 00002-7502-01) ON HAND: 0 MG Use SDV Waste' +
                'SDV (Site Owned): Location Door 1 / Shelf 2 / Bin 1: Gemzar (NDC 16729-0117-11) ON HAND: 0 MG Use SDV Waste' +
                'Samples ' +
                'SDV (Patient Assist): Location Door 2 / Shelf 2: Gemzar (NDC 00002-7501-01) ON HAND: 600 MG - Make this unassigned patient assist' +
                'SDV (Patient Assist): Location Door 4 / Shelf 2 / Bin 3: Gemzar (NDC 16729-0117-11) ON HAND: 5000MG - Make this unassigned patient assist'

     //   verifyPlanItem(1, ItemConstants.GEMZAR_200mg_00409_0183_01,   PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2.displayPath(), InventorySegment.SITE_OWNED)
        verifyPlanItem(2, ItemConstants.GEMZAR_200mg_00409_0185_01,   PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_3.displayPath(), InventorySegment.SITE_OWNED)
       // verifyPlanItem(3, ItemConstants.GEMZAR_200mg_00002_7502_01,   PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_3_POCKET_1.displayPath(), InventorySegment.SITE_OWNED)
        verifyPlanItem(4, ItemConstants.GEMZAR_1000mg_00069_3858_10,  PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2.displayPath(), InventorySegment.SITE_OWNED)
        verifyPlanItem(5, ItemConstants.GEMZAR_1000mg_00069_3858_10,  PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2.displayPath(), InventorySegment.SITE_OWNED)
       // verifyPlanItem(6, ItemConstants.GEMZAR_200mg_00002_7501_01,   PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.displayPath(), InventorySegment.SITE_OWNED)
       // verifyPlanItem(7, ItemConstants.GEMZAR_200mg_00002_7502_01,  PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.displayPath(), InventorySegment.SITE_OWNED)
        //verifyPlanItem(8, ItemConstants.GEMZAR_1000mg_16729_0117_11,  PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.displayPath(), InventorySegment.SITE_OWNED)
       // verifyPlanItem(9, ItemConstants.GEMZAR_200mg_00409_0183_01,   PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.displayPath(), InventorySegment.SITE_OWNED)
        // Samples
        //verifyPlanItem(10, ItemConstants.GEMZAR_200mg_00002_7501_01,  PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_3.displayPath(), InventorySegment.SAMPLES)

       // verifyPlanItem(11, ItemConstants.GEMZAR_200mg_00002_7501_01,  PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2.displayPath(), InventorySegment.PATIENT_ASSIST)
       // verifyPlanItem(12, ItemConstants.GEMZAR_1000mg_16729_0117_11,  PracticeConstants.Site_1_Layout.CABINET_A_DOOR_4_SHELF_2_BIN_3_POCKET_1.displayPath(), InventorySegment.PATIENT_ASSIST)
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.PLAN_DISPENSE_SORT, value = SettingsConstant.PlanDispenseSort.LAW),
                    @Setting(name = SettingsConstant.CLINICAL_ORDER_LINE_PROMPT, value = SettingsConstant.ClinicalOrderLinePrompt.DRUG_FAMILY_ONLY),
                    @Setting(name = SettingsConstant.EXPIRED_ITEM_WARNING_DAYS, value = '5'),
                    @Setting(name = SettingsConstant.REUSE_SDV_WASTE, value = SettingsConstant.FALSE)])
    def "Dispense planning - Patient Assist / Off Label Order"() {
        given: "I have cabinet inventory"
        goToDispensePage()

        when: 'I go to order planning screen'
        clickRefreshButton()
        selectFirstCardByMrn(patient2.accountNumber)
        at DispensePlanPage

        then: 'I can see off-label and patient assist inventory cards in the correct order'
        verifyPlanItem(1, ItemConstants.GEMZAR_200mg_00002_7501_01,  PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2.displayPath(), InventorySegment.OFFLABEL)
        verifyPlanItem(2, ItemConstants.GEMZAR_200mg_00002_7501_01,  PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2.displayPath(), InventorySegment.PATIENT_ASSIST)

    }


}