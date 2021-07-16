package com.cardinalhealth.vitalpath.utils

@Deprecated
enum InventoryLocation {
    DOOR1("Door 1", "beb61335-f84c-4777-ab79-48c3bf058b1f"),
    DOOR1_SHELF1("Door 1 / Shelf 1","b91f9f32-948b-462a-a547-6fff93453fc9"),
    DOOR1_SHELF1_BIN1("Door 1 / Shelf 1 / Bin 1", "679762c7-dceb-45b8-9ccc-98839dc68c6c"),
    DOOR1_SHELF1_BIN1_POCKET1("Door 1 / Shelf 1 / Bin 1 / Pocket 1", "f3e59f39-6437-4fb8-aab0-3132c4f9cdae"),
    DOOR1_SHELF1_BIN2_POCKET2("Door 1 / Shelf 1 / Bin 2 / Pocket 2"),
    DOOR1_SHELF1_BIN3_PATIENT_WHITEBAG("Door 1 / Shelf 1 / Bin 3", "3765ec5a-8e77-4a79-a0a4-2fe6ad4cc175"),

    DOOR2("Door 2", "7e4b4b92-ec37-4c99-91ad-c3e1e2c1bb71"),
    DOOR2_SHELF1("Door 2 / Shelf 1", "08cb9e8b-1623-468b-ac0d-0b0ecd78e4f3"),
    DOOR2_SHELF1_BIN1("Door 2 / Shelf 1 / Bin 1", "9600c576-8300-4f2c-89cc-94a356daf235"),
    DOOR2_SHELF1_BIN1_POCKET1("Door 2 / Shelf 1 / Bin 1 / Pocket 1","c689823a-8b1c-425b-919e-82536c980241"),
    DOOR2_SHELF2("Door 2 / Shelf 2", "7e76a6e1-41f1-45e4-a15c-de684ed0ac92"),
    DOOR2_SHELF2_BIN2("Door 2 / Shelf 2 / Bin 2","0c2a0345-ed9e-4dc9-9cca-dba8f8d3b706"),
    DOOR2_SHELF3("Door 2 / Shelf 3", "98f994ed-9b70-4829-80ff-36f61d497195"),
    DOOR2_SHELF3_BIN2("Door 2 / Shelf 3 / Bin 2", "dd662585-4797-4b23-bc9b-564533e64ea4"),

    NARC_DRAWER("drawer CS","7758c08b-2968-4282-982f-5224dbc301bb"),

    DOOR5("Door 5", "061dc507-9ef3-4057-803d-0b8ef2ca1c50"),
    DOOR5_SHELF1("Door 5 / Shelf 1", "4f26aa0e-c707-49c1-becf-c830ae6a08bc"),
    DOOR5_SHELF1_BIN3("Door 5 / Shelf 1 / Bin 3", "fc5195d6-7781-4797-b88a-fd5429a8b69a"),
    DOOR5_SHELF1_BIN3_POCKET1("Door 5 / Shelf 1 / Bin 3 / Pocket 1","38e77827-fd80-4c3f-a59c-a7aa819185a5"),
    DOOR5_SHELF3("Door 5 / Shelf 3", "ad824d37-5571-496e-a097-f5ca0f55d47b"),
    DOOR5_SHELF3_BIN1("Door 5 / Shelf 3 / Bin 1","1c8b344b-ee03-4e2d-8fb0-673450a8a321"),
    DOOR5_SHELF3_BIN1_POCKET1("Door 5 / Shelf 3 / Bin 1 / Pocket 1","3bc25fae-e1cd-481d-b1f3-ac98207d3873"),
    DOOR5_SHELF3_BIN2("Door 5 / Shelf 3 / Bin 2","cf880759-e072-4093-879a-df827efe6fef"),
    DOOR5_SHELF3_BIN2_POCKET1("Door 5 / Shelf 3 / Bin 2 / Pocket 1","3066210f-aa09-4b75-be55-22dcda4193dd")

    private final String locationId
    private final String locationName

    InventoryLocation(String locationName) {
        this.locationName = locationName
    }

    InventoryLocation(String locationName, String locationId) {
        this.locationName = locationName
        this.locationId = locationId
    }

    public String locationId() { locationId }
    public String locationName() { locationName }

    @Override
    public String toString() { locationName }
}

