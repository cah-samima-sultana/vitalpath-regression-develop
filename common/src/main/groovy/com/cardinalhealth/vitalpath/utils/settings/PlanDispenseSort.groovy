package com.cardinalhealth.vitalpath.utils.settings


class PlanDispenseSort extends SettingBase {
    String settingDefinitionId = "plandispensesort"

    Choice value

    @Override
    String getValue() {
        String result
        switch (value){
            case Choice.LAW:
                result = "LAW"
                break
            case Choice.MAN:
                result = "MAN"
                break
            default:
                result = 'LAW'
                break
        }

        result
    }

    enum Choice {
        LAW,
        MAN
    }
}
