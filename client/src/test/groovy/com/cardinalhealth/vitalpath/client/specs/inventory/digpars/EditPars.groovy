package com.cardinalhealth.vitalpath.client.specs.inventory.digpars

import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.extensions.feature.Login
import org.openqa.selenium.Keys

class EditPars extends DigParBase {

    @Login(site = PracticeConstants.SITE_1, cabinet = PracticeConstants.CABINET_A)
    def "Edit Pars"() {

        given: 'I am on the Inventory Drug Pars page'
        goToDigParPage()

        and: 'I have generated pars for a drug family/TAI (5-FU : Fluorouracil/All)'
        drugFamilySelector.openSearchAndSelect("5-FU : Fluorouracil")
        parTypes.openSearchAndSelect("All Locations")

        and: "I view the results"
        waitForDigHeaders()

        when: "I click the drug par I want to edit"
        def firstParMin = (cabinetPartPars.find { it.cabinetName == PracticeConstants.CABINET_A }).parMin
        firstParMin.click()

        then: "all the pars for that DIG are then in edit mode"
        firstParMin.find("input").hasClass("disabledElement") == false

        and: "I see the option to Cancel"
        $("[data-id='cancel']").displayed

        and: "I see the option to Save"
        $("[data-id='save']").displayed

        and: "I cancel out of editing"
        $("[data-id='cancel']").click()
    }

    @Login(site = PracticeConstants.SITE_1, cabinet = PracticeConstants.CABINET_A)
    def "Tab to Edit Pars"() {

        given: 'I am on the Inventory Drug Pars page'
        goToDigParPage()

        and: "I have generated pars for a drug family/TAI (5-FU : Fluorouracil/All)"
        drugFamilySelector.openSearchAndSelect("5-FU : Fluorouracil")
        parTypes.openSearchAndSelect("All Locations")
        waitForDigHeaders()

        and: "I edit one of the Min par values"
        def firstParMin = (cabinetPartPars.find { it.cabinetName == PracticeConstants.CABINET_A }).parMin
        firstParMin.click()

        when: "I select Tab"
        actions.sendKeys(Keys.TAB).build().perform()

        then: "my cursor is defaulted to the Max par value"
        ($(":focus").parents().grep{ it.hasClass("par-text-max")}).size() == 1

        and: "I select Tab again"
        actions.sendKeys(Keys.TAB).build().perform()

        and: "I go to the next Min par value"
        ($(":focus").parents().grep{ it.hasClass("par-text-min")}).size() == 1

        and: "I select Tab again"
        actions.sendKeys(Keys.TAB).build().perform()

        and: "the previous par was my last par value for that drug"
        ($(":focus").parents().grep{ it.hasClass("par-text-max")}).size() == 1
        $(":focus") == $("input").last()

        and: "I select Tab"
        actions.sendKeys(Keys.TAB).build().perform()

        and: "I am brought back to the first Min par value"
        $(":focus") == $("input").first()

        and: "I cancel out of editing"
        $("[data-id='cancel']").click()
    }

    @Login(site = PracticeConstants.SITE_1, cabinet = PracticeConstants.CABINET_A)
    def "Enter to Edit Pars"() {

        given: 'I am on the Inventory Drug Pars page'
        goToDigParPage()

        and: "I have generated pars for a drug family/TAI (5-FU : Fluorouracil/All)"
        drugFamilySelector.openSearchAndSelect("5-FU : Fluorouracil")
        parTypes.openSearchAndSelect("All Locations")
        waitForDigHeaders()

        and: "I edit one of the Min and Max par values"
        waitForColumnHeaders()
        def firstCabinetPartPar = cabinetPartPars.find { it.cabinetName == PracticeConstants.CABINET_A }
        def firstParMin = firstCabinetPartPar.parMin
        firstParMin.click()
        actions.sendKeys(Keys.NUMPAD1, Keys.TAB, Keys.NUMPAD9).build().perform()

        when: "I select Enter"
        actions.sendKeys(Keys.ENTER).build().perform()

        and: "the growler message displays at the top that says 'PAR levels saved'"
        isShowingGrowler("PAR levels saved")

        then: "my par value changes are saved"
        firstCabinetPartPar.verifyMinEquals("1")
        firstCabinetPartPar.verifyMaxEquals("9")

        and: "the Cancel and Save buttons are no longer displayed"
        $("[data-id='cancel']").displayed == false
        $("[data-id='save']").displayed == false

        and: "I am no longer in Edit mode"
        firstParMin.find("input").hasClass("disabledElement") == false
    }


    @Login(site = PracticeConstants.SITE_1, cabinet = PracticeConstants.CABINET_A)
    def "Cancel Edit to Pars"() {

        clearParts()
        given: 'I am on the Inventory Drug Pars page'
        goToDigParPage()

        and: "I have generated pars for a drug family/TAI"
        drugFamilySelector.openSearchAndSelect("5-FU : Fluorouracil")
        parTypes.openSearchAndSelect("All Locations")
        waitForDigHeaders()

        and: "I edit the pars"
        def firstCabinetPartPar = cabinetPartPars.find { it.cabinetName == PracticeConstants.CABINET_A }
        def firstParMin = firstCabinetPartPar.parMin
        firstParMin.click()
        actions.sendKeys(Keys.NUMPAD1, Keys.TAB, Keys.NUMPAD9).build().perform()

        when: "I select Cancel"
        $("[data-id='cancel']").click()

        then: "the edit to the pars is not saved"
        firstCabinetPartPar.verifyMinEquals("0")
        firstCabinetPartPar.verifyMaxEquals("0")
        $("[data-id='cancel']").displayed == false
        $("[data-id='save']").displayed == false
        firstParMin.find("input").hasClass("disabledElement") == false
    }

    @Login(site = PracticeConstants.SITE_1, cabinet = PracticeConstants.CABINET_A)
    def "Save Edit to Pars"() {

        clearParts()
        given: 'I am on the Inventory Drug Pars page'
        goToDigParPage()

        and: "I have generated pars for a drug family/TAI"
        drugFamilySelector.openSearchAndSelect("5-FU : Fluorouracil")
        parTypes.openSearchAndSelect("All Locations")
        waitForDigHeaders()

        and: "I edit the pars"
        def firstCabinetPartPar = cabinetPartPars.find { it.cabinetName == PracticeConstants.CABINET_A }
        def firstParMin = firstCabinetPartPar.parMin
        firstParMin.click()
        actions.sendKeys(Keys.NUMPAD1, Keys.TAB, Keys.NUMPAD9).build().perform()

        when: "I select Save"
        $("[data-id='save']").click()

        then: "the edit to the pars is saved"
        firstCabinetPartPar.verifyMinEquals("1")
        firstCabinetPartPar.verifyMaxEquals("9")
        $("[data-id='cancel']").displayed == false
        $("[data-id='save']").displayed == false
        firstParMin.find("input").hasClass("disabledElement") == false

    }
}
