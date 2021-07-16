package com.cardinalhealth.vitalpath.utils.settings


class ClinicalOrderLinePrompt extends SettingBase {
    String settingDefinitionId = "clinicalorderlineprompt"

    Choice value

    @Override
    String getValue() {
        String result
        switch (value){
            case Choice.DRUGFAMILYONLY:
                result = "DRUGFAMILYONLY"
                break
            case Choice.DRUGFAMILYFOCUS:
                result = "DRUGFAMILYFOCUS"
                break
            case Choice.ITEMONLY:
                result = "ITEMONLY"
                break
            case Choice.ITEMFOCUS:
                result = "ITEMFOCUS"
                break
            default:
                result = 'DRUGFAMILYONLY'
                break
        }

        result
    }

    enum Choice {
        DRUGFAMILYONLY,
        DRUGFAMILYFOCUS,
        ITEMONLY,
        ITEMFOCUS
    }
}
