package com.cardinalhealth.vitalpath.common.pages

import geb.Page

class CommonBasePage extends Page{

    static at = { waitFor { $("id": "cardinal-logo") } }
    static String SCROLL_CONTAINER_CLASS = ".scrollContainer"

}


