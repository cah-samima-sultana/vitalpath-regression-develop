package com.cardinalhealth.vitalpath.common.modules

class ScrollBoxSearchModule extends CommonBaseModule {

    static content = {
        wrapper(wait: true) { $(".search-text") }
        inputBox(wait: true) { $(".scrollBoxSearchText")}
    }

    def search(value){
        waitFor { inputBox }
        inputBox << value
    }
}