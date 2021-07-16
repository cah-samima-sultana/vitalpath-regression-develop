package com.cardinalhealth.vitalpath.client.pages.setup
import com.cardinalhealth.vitalpath.client.BasePage
import com.cardinalhealth.vitalpath.client.modules.ScheduleDetailsModule
import com.cardinalhealth.vitalpath.client.modules.ScheduleSummaryModule
import geb.navigator.Navigator

class LabelPage extends BasePage{

    static url = "#/client/setup/label-settings"
    static at = { waitFor(3, { $('[data-id="label-settings"]') }) }

    static content = {
        practiceButton{ $("#selectPracticeButton") }
        siteButton{ $("#selectSiteButton") }
        cabinetButton{ $("#selectCabinetButton") }

        selectedLabel{ $('[data-id="selected-label"]')}

        scrollContainer{ $(".scrollContainer") }
        labelChoiceCards{ scrollContainer.find(".labelChoiceCard") }

        labelCard{ $(".labelCard") }

        defaultLabel{ $('[data-id="default-label"]') }

        practiceOverride{ $("[data-id=practice-override]")}
        siteOverride{ $("[data-id=site-override]")}

        saveButton{ $("#saveButton") }
        cancelButton{ $("#cancelButton") }
        resetButton{ $("#resetButton") }
    }

    int getLabelTemplateCount(){
        return labelChoiceCards.size()
    }

    Navigator getLabelChoiceCardByName(String name){
        return labelChoiceCards.find{ it.text().contains(name) }
    }
}
