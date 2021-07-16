package com.cardinalhealth.vitalpath.services

import groovy.json.JsonSlurper

class InventoryService extends BaseService {

    private String REST_END_POINT = '/inventory/inventory'

    InventoryService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd) {
        super(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
    }


    def getInventory(drugFamilyGroupId, drugId, inventoryLocationId, itemId, patientId){
        def restEndPoint = REST_END_POINT;
        if(drugFamilyGroupId != null){
            restEndPoint+="?drugFamilyGroupId=${drugFamilyGroupId}"
        }
        if(drugId != null){
            restEndPoint+="&item.drugId=${drugId}"
        }
        if(inventoryLocationId != null){
            restEndPoint+="&inventoryLocationId=${inventoryLocationId}"
        }
        if(itemId != null){
            restEndPoint+="&itemId=${itemId}"
        }

        if(patientId != null){
            restEndPoint+="&patientId=${patientId}"
        } else {
            restEndPoint+="&patientId=@null()"
        }


        def response = restClient.get(path: restEndPoint, sslTrustAllCerts: true)

        def entities = new JsonSlurper().parseText(response.json.toString())

        return entities.size > 0 ? entities.get(0) : null;
    }

    def updateInventory(drugFamilyGroupId, inventoryLocationId, itemId, patientId, lotNumber, expirationDate){
        def inventory = getInventory(drugFamilyGroupId, null, inventoryLocationId, itemId, patientId);
        def putURL = REST_END_POINT + "/" + inventory.id
        def response = put(putURL){
            json id: inventory.id,
                    version: inventory.version,
                    tenantId: inventory.tenantId,
                    locationId: inventory.locationId,
                    lotNumber: lotNumber,
                    drugFamilyGroupId: inventory.drugFamilyGroupId,
                    patientId: inventory.patientId,
                    expires: expirationDate,
                    inventoryLocationId: inventory.inventoryLocationId,
                    itemId: inventory.itemId,
                    reasonCodeId: "Restock Cardinal",
                    witnessedBy: null
        }

    }

    def deleteInventory(id) {
        def deleteURL = REST_END_POINT + "/" + id
        delete(deleteURL)
    }
}