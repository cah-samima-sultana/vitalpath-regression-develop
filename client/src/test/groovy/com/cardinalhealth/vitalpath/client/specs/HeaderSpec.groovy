package com.cardinalhealth.vitalpath.client.specs

import com.cardinalhealth.vitalpath.client.BasePage
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.extensions.feature.Login

class HeaderSpec extends BaseSpec {

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A)
    def "I Login into the application"() {
        when:
            at BasePage
        then: "see Schedule tab"
            header.isPresentScheduleLink()
        and: "see Inventory tab"
            header.isPresentInventoryLink()
        and: "see Ordering tab"
            header.isPresentOrderingLink()
        and: "see Billing tab"
            header.isPresentBillingLink()
        and: "see Reports tab"
            header.isPresentReportsLink()
        and: "see Setup tab"
            header.isPresentSetupLink()
        and: "see Setup2 tab"
            header.isPresentSetup2Link()
        and: "see Setup2 tab"
            header.isPresentSetup2Link()
        and: "see Setup2 tab"
            header.isPresentSetup2Link()
//        and: "see Clinical Trail tab"
//            header.isPresentClinicalTrialLink()
        and: "see Messaging link"
            header.isPresentMessagingLink()
        and: "see Settings link"
            header.isPresentSettingLink()
    }
}
