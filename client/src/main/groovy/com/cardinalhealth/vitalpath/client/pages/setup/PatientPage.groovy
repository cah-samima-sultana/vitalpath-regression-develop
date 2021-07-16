package com.cardinalhealth.vitalpath.client.pages.setup

import com.cardinalhealth.vitalpath.client.BasePage
import com.cardinalhealth.vitalpath.common.modules.SelectorModule
import geb.navigator.Navigator

class PatientPage extends BasePage{

    static url = "#/client/setup/patients"
    static at = { waitFor(3, { $(".setup-patients") }) }

    static content = {
        addButton{ $("[data-id=add-patient]") }

        mrnSearch{ $("thead > tr").eq(1).find("th").eq(3).find("input") }

        patientModal{ $(".patient-modal") }
        firstName{ $(patientModal.find("input[name=firstName]"))}
        lastName{ $(patientModal.find("input[name=lastName]"))}
        accountNumber{ $(patientModal.find("input[name=accountNumber]"))}
        dob{ $(patientModal.find("input[name=pikaday]"))}
        gender{ module(new SelectorModule(selector: $(patientModal.find("[data-id=patient-gender]")))) }

        saveButton{ $("[data-id=modal-save]") }
        cancelButton{ $("[data-id=modal-back]") }
    }

}
