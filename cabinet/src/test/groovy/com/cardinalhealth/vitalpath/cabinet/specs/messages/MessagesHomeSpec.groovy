package com.cardinalhealth.vitalpath.cabinet.specs.messages

import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispenseHomePage
import com.cardinalhealth.vitalpath.cabinet.pages.messages.MessagesHomePage
import com.cardinalhealth.vitalpath.cabinet.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.extensions.feature.Login
import spock.lang.Ignore

@Ignore
class MessagesHomeSpec extends BaseSpec {

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A)
    def "User can get to the messages home page"() {
        given: "I am logged on to Site 1/Cabinet A"
        to DispenseHomePage

        when: "I can go to the messages home page"
            gotoMessages()

        then: "I see the messages home page"
            at MessagesHomePage
    }
}
