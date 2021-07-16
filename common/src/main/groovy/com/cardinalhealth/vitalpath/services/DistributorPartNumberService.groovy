package com.cardinalhealth.vitalpath.services
import com.cardinalhealth.vitalpath.EnvironmentProperties
import groovy.json.JsonSlurper

class DistributorPartNumberService extends BaseService {

    private String SERVICE_END_POINT = '/masterdata/distributorpartnumbers'
    private ItemService itemService
    
    DistributorPartNumberService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd) {
        super(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
        itemService = new ItemService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
    }

    def fetchItems(restEndPoint) {
        def response = restClient.get(path: restEndPoint, sslTrustAllCerts: true)
        return new JsonSlurper().parseText(response.json.toString())
    }

    def findVendorById(vendorTypeId){
        EnvironmentProperties env = EnvironmentProperties.instance
        def tenantId = env.tenantId()

        def vendors = fetchItems ("/masterdata/vendors?tenantId=${tenantId}&vendorTypeId=${vendorTypeId}")
        
        return vendors.size > 0 ? vendors.get(0) : null
    }
    
    def findPartByLocationItemId(locationId, itemId){
        def partNumbers = fetchItems ("${SERVICE_END_POINT}?locationId=${locationId}&itemId=${itemId}")
        return partNumbers.size > 0 ? partNumbers.get(0) : null
    }

    def findPartByLocationNdc(locationId, ndc){
        def item = itemService.findItemByNDC(ndc)
        return findPartByLocationItemId(locationId, item.id);
    }

    def findBestPricePartForDig(digId){
        def partNumbers = fetchItems ("${SERVICE_END_POINT}?drug.digId=${digId}")
        return findBestPricePartFromList(partNumbers)
    }

    def findBestPricePartForLocationDig(locationId, digId){
        def partNumbers = fetchItems ("${SERVICE_END_POINT}?locationId=${locationId}&drug.digId=${digId}")
        return findBestPricePartFromList(partNumbers)
    }

    private findBestPricePartFromList(partNumbers){
        def bestPricePart = null
        for(def partNumber : partNumbers){
            if(bestPricePart == null){
                bestPricePart = partNumber
            } else if(bestPricePart.orderCost > partNumber.orderCost){
                bestPricePart = partNumber
            }
        }

        return bestPricePart
    }

    def addPart(siteId, ndc, inventoryItemId){
        EnvironmentProperties env = EnvironmentProperties.instance
        def tenantId = env.tenantId()
        def vendor = findVendorById("CAH")
        def partNumber = ndc == null ? inventoryItemId : ndc;

        Random random = new Random()
        def low = 100
        def high = 10000
        def price = random.nextInt(high - low) + low

        def response = post(SERVICE_END_POINT){
            json tenantId: tenantId,
                    itemId: inventoryItemId,
                    partNumber: partNumber + "-part_number",
                    replacementCost: price,
                    orderCost: price,
                    locationId: siteId,
                    eligible: true,
                    distributorId: vendor.id,
                    drugId: ndc
        }

        return response.json
    }

    def findPartExtById(id){
        def retVal = null
        try {
            retVal = fetchItems("/masterdata/distributorpartnumberexts/${id}")
        } catch(Exception e){

        }
        return retVal
    }

    def createPartExt(id, locationId, preferredFlag){
        EnvironmentProperties env = EnvironmentProperties.instance
        def tenantId = env.tenantId()

        def response = post("/masterdata/distributorpartnumberexts"){
            json id: id,
                tenantId: tenantId,
                locationId: locationId,
                preferredFlag: preferredFlag
        }

        return response.json
    }

    def updatePartExt(partExt, preferredFlag){
        def response = put("/masterdata/distributorpartnumberexts/${partExt.id}"){
            json id: partExt.id,
                    version: partExt.version,
                    tenantId: partExt.tenantId,
                    locationId: partExt.locationId,
                    preferredFlag: preferredFlag
        }

        return response.json
    }
    
    def setPartAsPreferred(siteId, ndc){
        def item = itemService.findItemByNDC(ndc)
        def partNumber = findPartByLocationItemId(siteId, item.id)

        def partNumberExt = null
        if(partNumber != null) {
            partNumberExt = findPartExtById(partNumber.id)

            if (partNumberExt == null) {
                partNumberExt = createPartExt(partNumber.id, siteId, true)
            } else {
                partNumberExt = updatePartExt(partNumberExt, true)
            }
        }

        return partNumberExt;
    }

    def deletePreferredParts(locationId){
        def partNumberExts = fetchItems ("/masterdata/distributorpartnumberexts?locationId=${locationId}")

        for(def partNumberExt : partNumberExts){
            delete("/masterdata/distributorpartnumberexts/${partNumberExt.id}")
        }
    }

    def findPartByPartNumber(partNumber){
        def parts = fetchItems("${SERVICE_END_POINT}?partNumber=${partNumber}")
        return parts.size > 0 ? parts.get(0) : null
    }

    def deletePartByPartNumber(partNumber){
        def part = findPartByPartNumber(partNumber)
        if(part != null){
            delete("${SERVICE_END_POINT}/${part.id}")
        }
    }

    def deletePartById(id){
        delete("${SERVICE_END_POINT}/${id}")

    }
}
