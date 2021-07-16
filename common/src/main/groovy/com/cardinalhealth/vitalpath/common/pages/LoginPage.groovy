package com.cardinalhealth.vitalpath.common.pages

import com.cardinalhealth.vitalpath.common.modules.LoginModule
import geb.Page

class LoginPage extends Page {

    static url = "#/login"
    static at = { waitFor {id:"#loginContainer" } }

    static content = {
        login { module(new LoginModule()) }
    }
}
