package com.cardinalhealth.vitalpath.services

import groovy.json.JsonSlurper

class ItemService extends BaseService {

    private String SERVICE_END_POINT = '/masterdata/items'

    ItemService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd) {
        super(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
    }

    def findItemIdByNDC(ndc){
        def item = findItemByNDC(ndc)
        return item != null ? item.id : null
    }

    def findItemByNDC(ndc){
        def entities = fetchItems ("${SERVICE_END_POINT}?drugId=${ndc}")

        return entities.size > 0 ? entities.get(0) : null
    }

    private fetchItems(restEndPoint) {
        def response = restClient.get(path: restEndPoint, sslTrustAllCerts: true)

        return new JsonSlurper().parseText(response.json.toString())
    }

    def findItemIdByName(name){

        def encodedName = URLEncoder.encode(name,'UTF-8')

        def entities = fetchItems ("${SERVICE_END_POINT}?name=${encodedName}")

        return entities.size > 0 ? entities.get(0).id : null
    }

    def updateItem(ndc,name,active){
        def item = findItemByNDC(ndc);
        if(item == null){
            assert false
        }
        def putURL = SERVICE_END_POINT + "/" + item.id
        def nameToSave = name != null ? name : item.name
        def activeToSave= active != null ? active : item.active
        item.name = nameToSave
        item.active= activeToSave
        def response = put(putURL){
            json item
        }
        return response.json

    }
}
