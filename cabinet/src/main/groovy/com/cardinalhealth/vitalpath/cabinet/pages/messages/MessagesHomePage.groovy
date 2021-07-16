package com.cardinalhealth.vitalpath.cabinet.pages.messages

import com.cardinalhealth.vitalpath.cabinet.pages.BasePage

class MessagesHomePage extends BasePage {

    static url = "#/cabinet/messages"
    static at = { waitFor {title == "IMS | Cabinet – Messages – Index" } }
//    static at = { id:"#messagesContainer"}

}
