package com.cardinalhealth.vitalpath.common.traits

import org.openqa.selenium.WebDriverException

trait CommonAnimationTrait {

    def waitForAnimationToComplete() {
        waitForAnimationToComplete(1250)
        return true
    }

    def waitForAnimationToComplete(duration) {
        sleep(duration)
        return true
    }

    def waitForOverlaysAndDropDownsToGoAway() {
        waitForTapToCloseOverlayToGoAway()
        waitForDropDownMaskToGoAway()
        return true
    }

    def waitForDropDownMaskToGoAway() {
        def mask = $('#select2-drop-mask')
        if(mask.size() > 0) {
            waitFor { mask.css('display') != "block" }
        }
        return true
    }

    def waitForTapToCloseOverlayToGoAway() {
        waitFor { $('.overlayDark').hasClass('isBottomOpen') == false }
    }

    def waitForCoveringElementToGoAwayThenDo(myClosure) {
        boolean keepWaiting = true
        int counter = 0;
        int maxIterations = 500;
        while (true && (counter < maxIterations)) {
            try {
                myClosure.call()
                keepWaiting = false
            } catch(WebDriverException wde) {
                if( (! wde.getMessage().contains("Other element would receive the click")) || (! wde.getMessage().contains("element is not attached"))) {
                    keepWaiting = false;
                }
            } catch (Exception e) {
                counter = maxIterations
            }
            if(keepWaiting == false) {
                counter = maxIterations
                break;
            }
            counter++
        }
        if(counter >= maxIterations && keepWaiting == true) {
            println "Wait counter exceeded"
        }
        assert !keepWaiting
    }
}