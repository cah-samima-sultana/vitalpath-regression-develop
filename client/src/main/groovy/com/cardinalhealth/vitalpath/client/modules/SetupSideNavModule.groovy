package com.cardinalhealth.vitalpath.client.modules


class SetupSideNavModule extends BaseModule {

    def labelSettings = 'label-settings'
    def patients = 'patients'

    static content = {
        wrapper(wait: true) { $("data-id" : "setup-side-nav") }
    }

    def clickLabelSettings(){
        waitFor(3, { $("#" + labelSettings).size() > 0 })
        clickId(labelSettings)
    }

    def clickPatients(){
        waitFor(3, { $("#patients").size() > 0 })
        clickId("patients")
    }

}