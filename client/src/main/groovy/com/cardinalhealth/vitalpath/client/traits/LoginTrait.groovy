package com.cardinalhealth.vitalpath.client.traits

import com.cardinalhealth.vitalpath.client.BasePage
import com.cardinalhealth.vitalpath.common.pages.LoginPage


trait LoginTrait  {

    def login(user, password) {
        to LoginPage
        login.loginWith user, password
        at BasePage
    }
}
