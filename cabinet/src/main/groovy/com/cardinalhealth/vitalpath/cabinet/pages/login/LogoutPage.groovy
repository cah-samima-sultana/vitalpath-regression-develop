package com.cardinalhealth.vitalpath.cabinet.pages.login

import com.cardinalhealth.vitalpath.cabinet.pages.BasePage

class LogoutPage extends BasePage {

    static at = { waitFor { title == "IMS | Logout" } }

    static content = {
    }

}