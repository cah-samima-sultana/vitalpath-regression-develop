package com.cardinalhealth.vitalpath.client.specs.inventory.digpars
import com.cardinalhealth.vitalpath.EnvironmentProperties
import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.client.pages.inventory.OrderPreferencesPage
import com.cardinalhealth.vitalpath.datasetup.ItemConstants
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.extensions.feature.Login

class DigParSpec extends DigParBase {

    def setupSpec(){
        employeeService.updateOrderPreferences(true)

        preventDefaultRefresh = false
    }

    EnvironmentProperties env;

    def setup(){
        env = EnvironmentProperties.instance
        def browserSite1 = env.browserVersion() + " " + PracticeConstants.SITE_1
        def browserSite1Id = locationService.getLocationId(browserSite1)
        def pars = cabinetPartParService.findParsByLocation(browserSite1Id)
        cabinetPartParService.deletePars(pars)

        distributorPartNumberService.deletePreferredParts(browserSite1Id)
        goToDigParPage()
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A)
    def "Access Dig PAR screen and Set Order Preferences link"(){
        given: "I log into the PCC and I select Inventory Drug Pars"

        distributorPartNumberService.setPartAsPreferred(Session.loggedInSiteId, ItemConstants.GEMZAR_1000mg_16729_0117_11.ndc)

        and: "Then the last site used is selected"
        waitFor(3, {siteSelector.selectedValueEquals(Session.loggedInSite)} )

        and: "the drug family filter has the text 'Select A Drug Family'"
        drugFamilySelector.selectedValueEquals("Select A Drug Family")

        when: "I select a Drug Family <Gemzar>"
        drugFamilySelector.openSearchAndSelect("Gemzar")

        and: "the Drug Family has Preferred items selected"
        preferredCount() > 0

        and: "And the TAI filter is defaulted to <All>"
        drugFamilyTais.selectedValueEquals("All")

        and: "And the par filter is defaulted to <Locations w/ PAR Levels Set>"
        parTypes.selectedValueEquals("Locations w/ PAR Levels Set")

        then: "Then all DIGs display for that drug family <DIG for 200 MG INJ, 1000 MG INJ, and 2000 MG INJ>"
        waitForDigHeaders()
        verifyDig("Gemzar", "200", "MG", "INJ")
        verifyDig("Gemzar", "1000", "MG", "INJ")
        verifyDig("Gemzar", "2000", "MG", "INJ")

        and: "I see 'Set Order Preferences' link for all DIGs"
        orderPreferencesLinkCount() == 3

        and: "I see the DIG Header"
        digHeaderCount() > 0

        and: "I see the Best Price Header"
        bestPriceHeaderCount() > 0

        and: "I see the Preferred Header"
        preferredHeaderCount() > 0
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A)
    def "Cabinet PARs details - No PARs Set"(){
        given: "I log into the PCC and I select Inventory Drug Pars"


        and: "I have selected Locations w/PAR Levels set"
        parTypes.selectedValueEquals("Locations w/ PAR Levels Set")

        when: "I select a Drug Family <Gemzar>"
        drugFamilySelector.openSearchAndSelect("Gemzar")

        and: "a DIG in the drug family does not have PARs set"
        waitForDigHeaders()
        $(".filtered-list-detail-row").size() == 0

        then: "the DIGS for the drug family display"
        verifyDig("Gemzar", "200", "MG", "INJ")
        verifyDig("Gemzar", "1000", "MG", "INJ")
        verifyDig("Gemzar", "2000", "MG", "INJ")

        and: "the DIG with no PARs set does not display cabinets"
        $("[data-id='cabinetName']").size() == 0

        and: "I see an option to 'Show all...'"
        $(".filtered-list-side")[0].text() == "Show all..."
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A)
    def "Modify PAR levels for a cabinet - Save"(){
        given: "I have an item to edit"
        parTypes.selectedValueEquals("Locations w/ PAR Levels Set")
        drugFamilySelector.openSearchAndSelect("Gemzar")

        and: "I select the PAR levels I want to edit for that item"
        waitForDigHeaders()
        def firstBestPrice = getFirstBestPrice()
        clickShowAllShowLess(firstBestPrice)

        when: "I enter a Min and Max value that's 4 digits or fewer"
        clickAndEditMin(firstBestPrice, "1")
        clickAndEditMax(firstBestPrice, "10")

        then: "My changes can be saved"
        firstBestPrice.find("[data-id='save']").click()

        and: "My changes are reflected for the item"
        waitFor(1, {firstBestPrice.find("[data-id='save']").size() == 0})
        firstBestPrice.find(".par-text-min")[0].find(".input.disabledElement").size() == 1
        firstBestPrice.find(".par-text-min .input")[0].text() == "1"
        firstBestPrice.find(".par-text-max .input")[0].text() == "10"
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A)
    def "Modify PAR levels for an item - Cancel "() {
        given: "I have an item to edit"
//        gotoInventory()
//        goToDigPars()
        parTypes.selectedValueEquals("Locations w/ PAR Levels Set")
        drugFamilySelector.openSearchAndSelect("Gemzar")

        and: "I select the PAR levels I want to edit for that item"
        waitForDigHeaders()
        def firstBestPrice = getFirstBestPrice()
        clickShowAllShowLess(firstBestPrice)

        when: "I enter a Min and Max value that's 4 digits or fewer"
        clickAndEditMin(firstBestPrice, "1")
        clickAndEditMax(firstBestPrice, "10")

        then: "My changes can be cancelled"
        firstBestPrice.find("[data-id='cancel']").click()

        and: "My changes are reverted to their original values for the item"
        waitFor(1, {firstBestPrice.find("[data-id='cancel']").size() == 0})
        firstBestPrice.find(".par-text-min .input")[0].text() == "0"
        firstBestPrice.find(".par-text-max .input")[0].text() == "0"
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A)
    def "Ordering Preferences Screen - DIG PAR"() {
        given: "I selected Site 1"
        drugFamilySelector.openSearchAndSelect("Gemzar")
        drugFamilyTais.openSearchAndSelect("1000")

        when: "I select Ordering Preferences on DIG A"
        waitForDigHeaders()
        $("[data-id='order-preferences']")[0].find("a").click()

        then: "The Ordering Preferences Screen displays"
        at OrderPreferencesPage

        and: "I see the Site/Location filter"
        pageIsLoaded()
        waitFor(3, { orderPrefSiteSelector })

        and: "the Site/Location filter is defaulted to the Site selected in the Inventory Drug Pars screen"
        orderPrefSiteSelector.selectedValueEquals(Session.loggedInSite)

        and: "I see the Drug Family filter"
        true // TODO This part of the test can be written once VP-5786 is completed

        and: "the Drug Family filter is defaulted to the drug family selected in the Inventory Drug Pars screen"
        true // TODO This part of the test can be written once VP-5786 is completed

        and: "I see the TAI filter"
        true // TODO This part of the test can be written once VP-5786 is completed

        and: "the TAI filter is defaulted to the TAI I selected to set order preferences for from the Inventory Drug Pars screen"
        true // TODO This part of the test can be written once VP-5786 is completed

        and: "I see the title 'PAR Ordering Preferences for'"
        $("[data-id='order-preference-title']").text() == "PAR Ordering Preferences for:"

        and: "I see Drug Family A Name"
        digTitleContains("GEMZAR")

        and: "I see the TAI of drug family A"
        digTitleContains("1000 MG")

        and: "I see the route of drug family A"
        digTitleContains("INJ")

        and: "I see an option to 'Add Part'"
        $(".add-part").size() == 1

        and: "The option to RETURN is enabled"
        $(".cancel-button").size() == 1
        isCancelDisabled() == false

        and: "The option to SAVE is disabled"
        $(".save-button").size() == 1
        isSaveDisabled() == true

        and: "The option to SAVE & RETURN is disabled"
        $(".save-exit-button").size() == 1
        isSaveExitDisabled() == true

        and: "I see the Distributor Part Number Price component"
        $("[data-id='best-price-header']").size() == 1

        and: "The Distributor Part Number Price component displays the best priced (unit cost) for the DIG"
        def bestPrice = findBestPrice()
        $("[data-id='best-price-header']").find("[data-id='unit-cost']").text() == bestPrice

        and: "I see the Distributor Part Card detail"
        getPartNumberCount() > 0
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A)
    def "Set Item as Preferred and select Save"() {
        given: "I am setting Ordering Preferences for a DIG"
        drugFamilySelector.openSearchAndSelect("Gemzar")
        drugFamilyTais.openSearchAndSelect("1000")
        waitForDigHeaders()
        $("[data-id='order-preferences']")[0].find("a").click()
        at OrderPreferencesPage

        and: "I set <item A> as preferred"
        pageIsLoaded()
        waitFor(3, {$(".part-number-row").size() > 0})
        $(".part-number-row .fuse-checkbox-blank")[0].click()

        and: "the option to RETURN is enabled"
        isCancelDisabled() == false

        and: "the option to SAVE is enabled"
        isSaveDisabled() == false

        and: "the option to SAVE & RETURN is enabled"
        isSaveExitDisabled() == false

        when: "I select Save"
        clickSave()

        and: "I see the message 'Ordering Preferences successfully saved'"
        waitFor(3, {$("#toast-container .toast-message").first().text() == "Ordering Preferences successfully saved"})

        then: "I stay on the Ordering Preferences screen"
        at OrderPreferencesPage

        and: "<item A> is set as preferred"
        pageIsLoaded()
        waitFor({$(".save-spinner").size() == 0})
        $(".part-number-row.preferred").size() == 1
        $(".part-number-row.preferred .fuse-checkbox-checked").size() > 0

        and: "the option to RETURN is enabled"
        isCancelDisabled() == false

        and: "the option to SAVE is disabled"
        waitFor(3, {isSaveDisabled() == true})

        and: "the option to SAVE & RETURN is disabled"
        isSaveExitDisabled() == true
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A)
    def "Change Site"() {
        given: "I am on the Ordering Preferences screen"
        drugFamilySelector.openSearchAndSelect("Gemzar")
        drugFamilyTais.openSearchAndSelect("1000")
        waitForDigHeaders()
        $("[data-id='order-preferences']")[0].find("a").click()
        at OrderPreferencesPage

        and: "I am on Site 1"
        pageIsLoaded()
        orderPrefSiteSelector.selectedValueEquals(Session.loggedInSite)

        and: "I am viewing the drug items for DIG A in Site 1"
        $(".part-number-row").size() > 0

        when: "I indicate I want to view Site 2"
        switchOrderPrefSite(env.browserVersion() + " " + PracticeConstants.SITE_2)

        then: "the Order Preference Screen is refreshed"
        at OrderPreferencesPage

        and: "I see the drug items for DIG A in Site 2"
        getPartNumberCount() > 0

        and: "I see the Distributor Part Number Price component"
        $("[data-id='best-price-header']").size() == 1

        and: "the Distributor Part Number Price component displays the best priced (unit cost) for the DIG for Site 2"
        def bestPrice = findBestPrice()
        $("[data-id='best-price-header']").find("[data-id='unit-cost']").text() == bestPrice

        and: "I see the Distributor Part Card detail"
        getPartNumberCount() > 0
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A)
    def "Best Price per unit cost"() {
        given: "I am viewing Ordering Preferences for a DIG"
        drugFamilySelector.openSearchAndSelect("Gemzar")
        drugFamilyTais.openSearchAndSelect("1000")
        waitForDigHeaders()
        $("[data-id='order-preferences']")[0].find("a").click()
        at OrderPreferencesPage

        when: "an item has the lowest cost per unit in the DIG"
        $("[data-id='best-price-header']").size() == 1

        then: "a best price indicator displays to the right of the Pack cost"
        waitFor(3, {$("[data-id='best-price']").size() == 1})

        and: "the text in the Distributor Part Number Card is green"
        $("[data-id='best-price']").css("color") == "rgba(94, 149, 68, 1)"

        and: "Only 1 item has the best price selected - follow same logic that is used for current DIG PARs."
        $("[data-id='best-price']").size() == 1
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A)
    def "Add a Part"() {
        given: "I am on the Ordering Preferences screen for a DIG"
        drugFamilySelector.openSearchAndSelect("Gemzar")
        drugFamilyTais.openSearchAndSelect("1000")
        waitForDigHeaders()
        $("[data-id='order-preferences']")[0].find("a").click()
        at OrderPreferencesPage

        when: "I select to add a part"
        distributorPartNumberService.deletePartByPartNumber("pharmacy-part-1")
        clickAddPart()

        then: "a list of NDCs for the DIG is shown in ascending NDC order with Drug Name and Form"
        waitFor{ partEditorModule }
        partEditorModule.verifyColumnTitles()
        partEditorModule.verifySort(0, "ascending")

        and: "I can scroll up and down the list of NDCs"
        partEditorModule.find(".data-grid").hasClass("scrollContainer")

        and: "I can search for an NDC"
        partEditorModule.selectFilterNdc()
        partEditorModule.search("25021-0209-50")

        and: "the list of NDCs will reduce to those containing the numbers entered"
        waitFor(3, {partEditorModule.verifyRowCount(1)})

        and: "I select an NDC from the list"
        partEditorModule.columnRows[0].click()

        and: "I select Continue"
        partEditorModule.verifyContinueIsEnabled()
        partEditorModule.find("[data-id='continue']")[0].click()

        and: "I select a Distributor"
        partEditorModule.distributorSelector.openAndSelect("Pharmacy")

        and: "I enter a Part number"
        partEditorModule.inputPartNumber("pharmacy-part-1")

        and: "I enter the Order Cost up to 99999.99"
        partEditorModule.inputPrice("2.99")

        and: "I save my changes"
        partEditorModule.clickSave()

        and: "the Ordering Preferences screen refreshes"
        at OrderPreferencesPage

        and: "the part added displays in the default order of the screen"
        waitFor(3, { findPartNumber("pharmacy-part-1") } )

        and: "the part number must be unique to the Distributor and Site (across all DIGs)"
        clickAddPart()
        waitFor{ partEditorModule }
        partEditorModule.selectFilterNdc()
        partEditorModule.search("25021-0209-50")
        partEditorModule.columnRows[0].click()
        partEditorModule.find("[data-id='continue']").click()
        partEditorModule.distributorSelector.openAndSelect("Pharmacy")
        partEditorModule.inputPrice("2.99")
        partEditorModule.inputPartNumber("pharmacy-part-1")
        partEditorModule.isInvalidPartNumber()
        partEditorModule.clickClose()

        and: "I can change to another Site"
        at OrderPreferencesPage
        switchOrderPrefSite(env.browserVersion() + " " + PracticeConstants.SITE_2)

        and: "the part does not exist in the newly selected Site"
        findPartNumber("pharmacy-part-1") == false
    }
}

