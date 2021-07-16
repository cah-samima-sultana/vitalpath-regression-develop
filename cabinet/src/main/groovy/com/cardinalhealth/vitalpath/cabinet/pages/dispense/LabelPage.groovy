package com.cardinalhealth.vitalpath.cabinet.pages.dispense

import com.cardinalhealth.vitalpath.cabinet.pages.BasePage

class LabelPage extends BasePage {

    static url = "#/cabinet/dispense/{id}"
    static at = { waitFor(3, {labelOutlet}) }

    static content = {
        labelOutlet(wait: true) { $(".labelOutlet") }
        labelCard(wait: true) { labelOutlet.find(".labelCard") }

        sideBar(wait: true) { waitFor(3, {$(".buttonSidebar")}) }
        sideBarButtons { sideBar.find(".button") }
        templateButton { sideBarButtons.find{ it.text().toLowerCase().equals("template") } }
        backButton { $("[data-id=back-button]") }

        selectedTemplate { $("[data-id=selected-template]")}
    }

    def isLabelTemplateByNumber(String number){
        waitFor(3, { labelCard.hasClass("labelTemplate"+number) } )
    }

}
