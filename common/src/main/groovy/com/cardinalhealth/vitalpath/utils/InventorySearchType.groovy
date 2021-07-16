package com.cardinalhealth.vitalpath.utils

enum InventorySearchType {

    ALL('All'),
    ITEM_NAME('Item Name'),
    INV_LOCATION('Inv Location'),
    NDC('NDC'),
    INVENTORY_SEGMENT('Inventory Segment'),
    PATIENT_NAME_MRN('Patient Name/MRN'),
    LOT_NUMBER('Lot Number')

    InventorySearchType(String title) {
        this.title = title
    }

    private final String title

    public String title() { title }

}
