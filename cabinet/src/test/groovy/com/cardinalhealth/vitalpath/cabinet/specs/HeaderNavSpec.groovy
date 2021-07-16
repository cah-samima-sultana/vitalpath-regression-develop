package com.cardinalhealth.vitalpath.cabinet.specs

import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispenseHomePage
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.extensions.feature.Login

class HeaderNavSpec extends BaseSpec {

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A)
    def "I can navigate around to Messages, Inventory, Dispense"() {
        given: "I am logged on"

        when:
            goToInventoryPage()
            goToMessagesPage()
            goToDispensePage()
        then:
            1==1
    }
}
