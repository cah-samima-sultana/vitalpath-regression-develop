package com.cardinalhealth.vitalpath.datasetup

import com.cardinalhealth.vitalpath.EnvironmentProperties
import com.cardinalhealth.vitalpath.services.*
import com.esotericsoftware.yamlbeans.YamlReader

class Inventory {

    private InventoryAdjustmentService inventoryAdjustmentService
    private InventoryService inventoryService
    private InventoryLocationService inventoryLocationService
    private LocationService locationService
    private ItemService itemService
    private DistributorPartNumberService distributorPartNumberService

    def INVENTORY_SETUP_RESOURCE = '/inventorysetup.yml'

    def setup() {
        def environmentProperties = EnvironmentProperties.instance
        inventoryLocationService = new InventoryLocationService(environmentProperties.servicesUrl(), environmentProperties.restClientAuthId(), environmentProperties.restClientAuthPwd())
        inventoryService = new InventoryService(environmentProperties.servicesUrl(), environmentProperties.restClientAuthId(), environmentProperties.restClientAuthPwd())
        locationService = new LocationService(environmentProperties.servicesUrl(), environmentProperties.restClientAuthId(), environmentProperties.restClientAuthPwd())
        inventoryAdjustmentService = new InventoryAdjustmentService(environmentProperties.servicesUrl(), environmentProperties.restClientAuthId(), environmentProperties.restClientAuthPwd())
        itemService = new ItemService(environmentProperties.servicesUrl(), environmentProperties.restClientAuthId(), environmentProperties.restClientAuthPwd())
        distributorPartNumberService = new DistributorPartNumberService(environmentProperties.servicesUrl(), environmentProperties.restClientAuthId(), environmentProperties.restClientAuthPwd())

        Map inventorySetup = getInventorySetup()

        for(browser in inventorySetup.get('Browsers')) {
            for (location in inventorySetup.get('Setup')) {
                def siteId = locationService.getLocationId("${browser} ${location.key}")
                println "${browser} ${location.key}"
                setupInventory(siteId, location.value)
            }
        }
    }

    def setupInventory(siteId, inventoryLocations) {
        for (inventoryLocation in inventoryLocations) {
            println inventoryLocation.key
            def inventoryLocationId = inventoryLocationService.findInventoryLocationIdByPath(inventoryLocation.key, siteId)
            if (inventoryLocationId != null) {
                addInventory(siteId, inventoryLocationId, inventoryLocation.value)
            }
        }
    }

    def addInventory(siteId, inventoryLocationId, inventoryItems) {
        for (inventoryItem in inventoryItems) {
            def inventoryItemId
            if (inventoryItem.isDrug == "true") {
                inventoryItemId = itemService.findItemIdByNDC(inventoryItem.ndc)
            }
            else {
                inventoryItemId = itemService.findItemIdByName(inventoryItem.name)
            }
            if (inventoryItemId != null) {
                inventoryAdjustmentService.addInventory(siteId, inventoryLocationId, inventoryItem.dfgId, inventoryItem.ndc, inventoryItemId, 0, null, null, false, inventoryItem.lotNumber)
                distributorPartNumberService.addPart(siteId, inventoryItem.ndc, inventoryItemId)
            }
        }
    }

    private getInventorySetup() {
        YamlReader reader = new YamlReader(this.getClass().getResource(INVENTORY_SETUP_RESOURCE).text)
        return (Map) reader.read(TreeMap.class)
    }
}
