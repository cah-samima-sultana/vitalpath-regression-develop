package com.cardinalhealth.vitalpath.datasetup

enum InventorySegment {
    WHITE_BAG('White Bag','1w'),
    PATIENT_ASSIST('Patient Assist','1p'),
    SITE_OWNED('Site Owned','1s'),
    SAMPLES('Samples','samples'),
    OFFLABEL('Off Label','offlabel')

    InventorySegment(String title, String id) {
        this.title = title
        this.id = id
    }

    private final String title
    private final String id

    public String title() { title }
    public String id() { id }

}