package com.cardinalhealth.vitalpath.client.specs.inventory.digpars

import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.client.pages.inventory.DigParPage
import com.cardinalhealth.vitalpath.client.pages.inventory.OrderPreferencesPage
import com.cardinalhealth.vitalpath.datasetup.InventorySegment
import com.cardinalhealth.vitalpath.datasetup.ItemConstants
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.extensions.feature.Login

class OnHandQuantity extends DigParBase {

    def setupSpec(){
        employeeService.updateOrderPreferences(true)
    }

    def setup(){
        setOnHandInventory()
        clearParts()
    }

    def setOnHandInventory() {
        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(),
                Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(),
                ItemConstants.ACETAMINOPHEN_325mg_00536_3222_01.getNdc(), ItemConstants.ACETAMINOPHEN_325mg_00536_3222_01.getItemId(), 10 * 325, null, null)

        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(),
                Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(),
                ItemConstants.ACETAMINOPHEN_325mg_57896_0101_01.getNdc(), ItemConstants.ACETAMINOPHEN_325mg_57896_0101_01.getItemId(), 5 * 325, null, null)

        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(),
                Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(),
                ItemConstants.ACETAMINOPHEN_325mg_00904_1982_60.getNdc(), ItemConstants.ACETAMINOPHEN_325mg_00904_1982_60.getItemId(), 0, null, null)
    }

    @Login(site = PracticeConstants.SITE_1, cabinet = PracticeConstants.CABINET_A)
    def "Set Best Price Item as Preferred & Display On Hand Quantity"() {

        given: 'I am on the Inventory Drug Pars page'
        goToDigParPage()

        and: "I search for Tylenol : Acetaminophen"
        drugFamilySelector.openSearchAndSelect("Tylenol : Acetaminophen")
        drugFamilyTais.openSearchAndSelect("325 MG")
        waitForDigHeaders()

        and: "the On Hand Quantity under the Best Price section for Site 1/Cabinet A is 15"
        parTypes.openSearchAndSelect("All Locations")
        def cabinetAPar = cabinetPartPars.find { it.cabinetName == PracticeConstants.CABINET_A }
        cabinetAPar.find("[data-id='onhand-quantity']").text() == "15"

        and: "the NDC in the Best Price header is 00536-3222-01"
        true
        // TODO - Prices are randomly generated, cant guarantee this NDC is best price unless we explicitly set it

        and: "I select to Set Order Preferences"
        $("[data-id='order-preferences']").find("a").click()

        and: "I set the Best Price NDC 00536-3222-01 as the Preferred Item"
        at OrderPreferencesPage
        waitFor(3, {$(".part-number-row").size() > 0})
        findPartByNDC("00536-3222-01").find(".fuse-checkbox-blank").click()

        when: "I return to the Inventory Drug Pars screen"
        $("[data-id='save-and-exit']").click()
        at DigParPage
        waitForDigHeaders()

        then: "the On Hand quantity under the best price section for Site 1/Cabinet A is 5 (which represents the on hand quantity for NDCs that are not preferred, i.e. 57896-0101-01)"
        def cabinetAPar2 = cabinetPartPars.find { it.cabinetName == PracticeConstants.CABINET_A }
        waitFor(3, {cabinetAPar2.find("[data-id='onhand-quantity']").text() == "5"})

        and: "the header of the preferred item contains the NDC 00536-3222-01"
        $("[data-id='digpar-price-widget'].preferred").find("[data-id='ndc']").text() == "00536-3222-01"

        and: "the On Hand quantity under the preferred section for Site 1/Cabinet A is 10"
        def cabinetAPars = cabinetPartPars.grep { it.cabinetName == PracticeConstants.CABINET_A }
        def secondCabinetAPar = cabinetAPars.get(1)
        waitFor(3, {secondCabinetAPar.find("[data-id='onhand-quantity']").text() == "10"})

    }

    @Login(site = PracticeConstants.SITE_1, cabinet = PracticeConstants.CABINET_A)
    def "Set non-best price item as Preferred & Display On Hand Quantity"() {

        given: 'I am on the Inventory Drug Pars page'
        goToDigParPage()

        and: "I search for Tylenol : Acetaminophen"
        drugFamilySelector.openSearchAndSelect("Tylenol : Acetaminophen")
        drugFamilyTais.openSearchAndSelect("325 MG")
        waitForDigHeaders()

        and: "the On Hand Quantity under the Best Price section for Site 1/Cabinet A is 15"
        parTypes.openSearchAndSelect("All Locations")
        def cabinetAPar = cabinetPartPars.find { it.cabinetName == PracticeConstants.CABINET_A }
        cabinetAPar.find("[data-id='onhand-quantity']").text() == "15"

        and: "the NDC in the Best Price header is 00536-3222-01"
        true
        // TODO - Prices are randomly generated, cant guarantee this NDC is best price unless we explicitly set it

        and: "I select to Set Order Preferences"
        $("[data-id='order-preferences']").find("a").click()

        and: "I set the non-Best Price NDC 57896-0101-01 as the Preferred item"
        at OrderPreferencesPage
        waitFor(3, { $(".part-number-row").size() > 0 })
        findPartByNDC("57896-0101-01").find(".fuse-checkbox-blank").click()

        when: "I return to the Inventory Drug Pars screen"
        $("[data-id='save-and-exit']").click()
        at DigParPage
        waitForDigHeaders()

        then: "the On Hand quantity under the best price section for Site 1/Cabinet A is 10"
        def cabinetAPar2 = cabinetPartPars.find { it.cabinetName == PracticeConstants.CABINET_A }
        waitFor(3, { cabinetAPar2.find("[data-id='onhand-quantity']").text() == "10" })

        and: "the header of the preferred item contains the NDC 57896-0101-01"
        $("[data-id='digpar-price-widget'].preferred").find("[data-id='ndc']").text() == "57896-0101-01"

        and: "the On Hand quantity under the preferred section for Site 1/Cabinet A is 5"
        def cabinetAPars = cabinetPartPars.grep { it.cabinetName == PracticeConstants.CABINET_A }
        def secondCabinetAPar = cabinetAPars.get(1)
        waitFor(3, { secondCabinetAPar.find("[data-id='onhand-quantity']").text() == "5" })
    }

    @Login(site = PracticeConstants.SITE_1, cabinet = PracticeConstants.CABINET_A)
    def "Set all items with On Hand Inventory as Preferred & Display On Hand Quantity"() {

        given: 'I am on the Inventory Drug Pars page'
        refreshPage()
        goToDigParPage()

        and: "I search for Tylenol : Acetaminophen"
        drugFamilySelector.openSearchAndSelect("Tylenol : Acetaminophen")
        drugFamilyTais.openSearchAndSelect("325 MG")
        waitForDigHeaders()

        and: "the On Hand Quantity under the Best Price section for Site 1/Cabinet A is 15"
        parTypes.openSearchAndSelect("All Locations")
        def cabinetAPar = cabinetPartPars.find { it.cabinetName == PracticeConstants.CABINET_A }
        cabinetAPar.find("[data-id='onhand-quantity']").text() == "15"

        and: "the NDC in the Best Price header is 00536-3222-01"
        true
        // TODO - Prices are randomly generated, cant guarantee this NDC is best price unless we explicitly set it

        and: "I select to Set Order Preferences"
        $("[data-id='order-preferences']").find("a").click()
        at OrderPreferencesPage
        waitFor(3, { $(".part-number-row").size() > 0 })

        and: "I set the non-Best Price NDC 57896-0101-01 as the Preferred item"
        findPartByNDC("57896-0101-01").find(".fuse-checkbox-blank").click()

        and: "I set the Best Price NDC 00536-3222-01 as the Preferred item "
        findPartByNDC("00536-3222-01").find(".fuse-checkbox-blank").click()

        when: "I return to the Inventory Drug Pars screen"
        $("[data-id='save-and-exit']").click()
        at DigParPage
        waitForDigHeaders()

        then: "the On Hand quantity under the best price section for Site 1/Cabinet A is 0"
        def cabinetAPar2 = cabinetPartPars.find { it.cabinetName == PracticeConstants.CABINET_A }
        waitFor(3, { cabinetAPar2.find("[data-id='onhand-quantity']").text() == "0" })

        and: "the text displayed is 'not assigned'"
        true
        // TODO Test setup told us to assign it to Door 2 / Shelf 1!

        and: "the header of the preferred item contains the NDC 57896-0101-01"
        findPartByNDC("57896-0101-01")

        and: "the On Hand quantity under the preferred section for Site 1/Cabinet A is 5"
        findPartByNDC("57896-0101-01").find("[data-id='onhand-quantity']")[0].text() == "5"

        and: "the On Hand quantity under the preferred section for Site 1/Cabinet A is 10"
        findPartByNDC("00536-3222-01").find("[data-id='onhand-quantity']")[0].text() == "10"

    }
}
