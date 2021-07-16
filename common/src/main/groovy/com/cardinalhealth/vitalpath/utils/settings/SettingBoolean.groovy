package com.cardinalhealth.vitalpath.utils.settings


class SettingBoolean extends SettingBase{

    final String TRUE = 'true'
    final String FALSE = 'false'

    public Boolean value

    @Override
    String getValue() {
        return value ? this.TRUE : this.FALSE
    }

}
