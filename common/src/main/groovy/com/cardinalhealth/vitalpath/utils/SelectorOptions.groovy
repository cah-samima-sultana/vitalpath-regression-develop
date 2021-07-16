package com.cardinalhealth.vitalpath.utils

import org.openqa.selenium.Keys

class SelectorOptions {

    public enum ActionKeys {
        ENTER (Keys.ENTER),
        ESC (Keys.ESCAPE),
        TAB (Keys.TAB);

        private final key

        ActionKeys(Keys key){
            this.key = key
        }

        public Keys key(){
            return key
        }
    }

    //Value to search for
    String searchValue = null

    //If true the dropdown will attempt to be opened
    Boolean open = true

    //if used this key stroke will be sent to the control when appropriate
    ActionKeys actionKey = null

    Boolean selectItem = true

}
