package com.cardinalhealth.vitalpath.services

class InventoryAdjustmentService extends BaseService {

    private String REST_END_POINT_ADJUSTMENTS = '/inventory/adjustments/'
    private String REST_END_POINT_INVENTORY = '/inventory/inventory/'

    private InventoryLocationService inventoryLocationService
    private InventoryService inventoryService

    InventoryAdjustmentService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd) {
        super(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
        inventoryService = new InventoryService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
        inventoryLocationService = new InventoryLocationService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
    }


    def addInventory(locationId, inventoryLocationId, drugFamilyGroupId, drugId, itemId, quantity, expires, patientId,partialInUse=false, lotNumber=null){
        def addInventoryResponse = post(REST_END_POINT_ADJUSTMENTS) {
            json adjustmentReasonCodeId: "manual",
                drugFamilyGroupId: drugFamilyGroupId,
                drugId: drugId,
                inventoryLocationId: inventoryLocationId,
                itemId: itemId,
                locationId: locationId,
                patientId: patientId,
                quantity: quantity,
                reason: "Restock Cardinal",
                witnessedBy: null,
                partialInUse: partialInUse,
                lotNumber: lotNumber
        }

        return addInventoryResponse.json.id
    }

    def updateInventory(inventory, lotNumber, expires){
        def updateInventoryResponse = put("${REST_END_POINT_INVENTORY}/${inventory.id}"){
            json id: inventory.id,
            version: inventory.version,
            drugFamilyGroupId: inventory.drugFamilyGroupId,
            expires: expires,
            inventoryLocationId: inventory.inventoryLocationId,
            itemId: inventory.itemId,
            locationId: inventory.locationId,
            lotNumber: lotNumber,
            patientId: inventory.patientId,
            tenantId: inventory.tenantId,
            reasonCodeId: "Restock Cardinal"
        }

        return updateInventoryResponse.json.id
    }

    def setOnhandInventory(drugFamilyGroupId, siteId, location, ndc, itemId, quantity, expires, patientId, partialInUse=false){
        def locationId = inventoryLocationService.findInventoryLocationIdByPath(location, siteId)
        def inventory = inventoryService.getInventory(drugFamilyGroupId, ndc, locationId, itemId, patientId)

        if(inventory != null) {
            addInventory(siteId, locationId, drugFamilyGroupId, ndc, itemId, -inventory.quantity, null, patientId, partialInUse)
        }

        addInventory(siteId, locationId, drugFamilyGroupId, ndc, itemId, quantity * 100, expires, patientId, partialInUse)
    }

    def deleteInventory(drugFamilyGroupId, ndc, inventoryLocationId, itemId, patientId){
        def inventory= inventoryService.getInventory(drugFamilyGroupId, ndc, inventoryLocationId, itemId, patientId)

        if(inventory != null) {
            if(inventory.quantity > 0){
                addInventory(inventory.locationId, inventory.inventoryLocationId, inventory.drugFamilyGroupId, ndc, inventory.itemId, -inventory.quantity, null, inventory.patientId)
            }
            inventoryService.deleteInventory(inventory.id);
        }
    }


}