package com.cardinalhealth.vitalpath.services

class DispenseService extends BaseService {

    private String SERVICE_END_POINT = '/inventory/dispenselines'
    private InventoryLocationService inventoryLocationService
    private ItemService itemService

    DispenseService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd) {
        super(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
        inventoryLocationService = new InventoryLocationService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
        itemService = new ItemService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
    }

    def dispenseItem(siteId, lineId, drugFamilyGroupId, inventoryLocation, ndc, quantity, reuseSdvWaste = false, patientId = null){
        def itemId = itemService.findItemIdByNDC(ndc)
        def inventoryLocationId = inventoryLocationService.findInventoryLocationIdByPath(inventoryLocation, siteId)
        def response = post(SERVICE_END_POINT){
            json clinicalOrderLineId: lineId,
                    drugFamilyGroupId: drugFamilyGroupId,
                    inventoryLocationId:inventoryLocationId,
                    itemId: itemId,
                    inventoryPatientId: patientId,
                    planned: 'N',
                    quantity: quantity,
                    reuseSDVWaste: reuseSdvWaste,
                    waste: false
        }

        return response.json
    }

    def dispenseItemNonDrug(siteId, lineId, drugFamilyGroupId, inventoryLocation, itemId, quantity, reuseSdvWaste = false){
        def inventoryLocationId = inventoryLocationService.findInventoryLocationIdByPath(inventoryLocation, siteId)
        def response = post(SERVICE_END_POINT){
            json clinicalOrderLineId: lineId,
                    drugFamilyGroupId: drugFamilyGroupId,
                    inventoryLocationId:inventoryLocationId,
                    itemId: itemId,
                    planned: 'N',
                    quantity: quantity,
                    reuseSDVWaste: reuseSdvWaste,
                    waste: false
        }

        return response.json
    }
}
