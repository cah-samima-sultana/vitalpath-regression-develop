package com.cardinalhealth.vitalpath.datasetup

enum OculusConstants {

    NONE("NA", "None"),
    BILATERAL("OU", "Bilateral"),
    RIGHT("OD", "Right"),
    LEFT("OS", "Left")

    private final String id
    private final String description

    OculusConstants(String id, String description) {
        this.id = id
        this.description = description
    }

    String getId() {
        return id
    }

    String getDescription() {
        return description
    }
}



