package com.cardinalhealth.vitalpath.cabinet.traits

import com.cardinalhealth.vitalpath.common.traits.CommonTrait


trait CabinetTrait extends CommonTrait{

    def navBarClassName = "buttonSidebar"

    def navItemDoesNotExist(selector) {
        def wrapper = $(className(navBarClassName))
        def item = wrapper.find(selector)
        !elementExists(item)
    }

    def navItemExists(selector) {
        def wrapper = $(className(navBarClassName))
        def item = wrapper.find(selector)
        elementExists(item)
    }

}