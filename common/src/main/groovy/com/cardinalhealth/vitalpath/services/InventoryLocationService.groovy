package com.cardinalhealth.vitalpath.services

import groovy.json.JsonSlurper

class InventoryLocationService extends  BaseService{

    private String REST_END_POINT = "inventory/inventorylocations/"

    InventoryLocationService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd) {
        super(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
    }

     def addInventoryLocation(locationId, parentId, type, locationName, columnNum, orderNum=0){
        post(REST_END_POINT) {
            json parentId: parentId,
                    locationId: locationId,
                    name: locationName,
                    deleted: false,
                    columnNum: columnNum,
                    orderNum: orderNum,
                    attributes: new LinkedHashMap<String, String>(),
                    description: "",
                    items: null,
                    typeId: type,
                    version: 1
        }
    }

    def findInventoryLocationId(locationId, parentId, typeId, name){
        def encodedName = URLEncoder.encode(name,'UTF-8')

        def restEndPoint = "${REST_END_POINT}?locationId=${locationId}&typeId=${typeId}&name=${encodedName}"

        if (parentId != null){
            restEndPoint += "&parentId=${parentId}"
        }

        def response = restClient.get(path: restEndPoint, sslTrustAllCerts: true)

        def entities = new JsonSlurper().parseText(response.json.toString())

        return entities.size > 0 ? entities.get(0).id : null
    }

    def findInventoryLocationIdByPath(path, siteId) {
        def encodedPath = URLEncoder.encode(path, 'UTF-8')

        def restEndPoint = "${REST_END_POINT}?path=${encodedPath}&locationId=${siteId}"

        def response = restClient.get(path: restEndPoint, sslTrustAllCerts: true)

        def entities = new JsonSlurper().parseText(response.json.toString())

        return entities.size > 0 ? entities.get(0).id : null
    }

}
