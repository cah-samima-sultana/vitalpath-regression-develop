package com.cardinalhealth.vitalpath.client.specs.setup

import com.cardinalhealth.vitalpath.client.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.datasetup.SettingsConstant
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting

class Label extends BaseSpec {


    @Login(site = PracticeConstants.SITE_1, cabinet = PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.LABEL_TEMPLATE_DEFINITION, value = SettingsConstant.Labels.LABEL_TEMPLATE_1)])
    def "Label Settings - Template 7"() {
        given: "I am signed on to the PC Client"
        //and: "I have navigated to the Setup2 tab"
        //and: "I am on Label Settings screen"
//        waitFor(3, {$("#setup2-link")})
//        $("#setup2-link").click() // TODO This should not be necessary but to is not working
//        waitFor(3, {$("[href='#/client/setup/label-settings']")})
//        $("[href='#/client/setup/label-settings']").click() // TODO This should not be necessary but to is not working
        goToLabelSettingsPage()

        when: "I review the label templates"
        getLabelTemplateCount() >= 7

        then: "I see Template 7"
        def template7 = getLabelChoiceCardByName("Template 7")
        template7.size() == 1

        and: "I see the layout matches the design (attached)"
        and: "I see the description"
        template7.click()
        template7.find("[data-id=label-choice-header]").text().trim() == "Template 7"

        and: "the Text Description for Template 7 in PC Client is " +
                " Up to two items per label without NDCs and larger font on Dose, Total Volume, Rate and Infusion Time."
        $("[data-id=label-template-07]").text().trim().contains("Up to two items per label without NDCs and larger font on Dose, Total Volume, Rate and Infusion Time.")

        and: "I can save Label Template 7 as my default"
        saveButton.click()

        and: "Save is disabled afterwards"
        waitFor(3, {saveButton.hasClass("isDisabled")})

        and: "When I switch to the 'site' tab"
        siteButton.click()

        and: "Practice Override is set to Label Template 7"
        waitFor(3, {practiceOverride.text().trim() == "Template 7"})
    }

}