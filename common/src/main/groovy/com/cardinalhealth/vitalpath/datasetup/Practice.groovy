package com.cardinalhealth.vitalpath.datasetup

import com.cardinalhealth.vitalpath.EnvironmentProperties
import com.cardinalhealth.vitalpath.services.InventoryLocationService
import com.cardinalhealth.vitalpath.services.LocationService
import com.cardinalhealth.vitalpath.services.SettingsService
import com.esotericsoftware.yamlbeans.YamlReader
import groovy.json.JsonSlurper

class Practice {

    private InventoryLocationService inventoryLocationService
    private LocationService locationService
    private SettingsService settingsService

    def PRACTICE_SETUP_RESOURCE = '/practicesetup.yml'

    def environmentProperties

    def setup() {
        environmentProperties = EnvironmentProperties.instance
        locationService = new LocationService(environmentProperties.servicesUrl(), environmentProperties.restClientAuthId(), environmentProperties.restClientAuthPwd())
        inventoryLocationService = new InventoryLocationService(environmentProperties.servicesUrl(), environmentProperties.restClientAuthId(), environmentProperties.restClientAuthPwd())
        settingsService = new SettingsService(environmentProperties.servicesUrl(), environmentProperties.restClientAuthId(), environmentProperties.restClientAuthPwd())

        Map practiseSetup =  getPracticeSetup()

        for(browser in practiseSetup.get('Browsers')){
            for (location in practiseSetup.get('Setup')) {
                def locationId = setupLocation("${browser} ${location.name}")
                setupCabinets(locationId, location.cabinets)
            }
        }
    }

    private setupCabinets(locationId, cabinets){
        int i = 0;
        for (cabinet in cabinets) {
            def cabinetId = inventoryLocationService.findInventoryLocationId(locationId, null, 'cabinet', cabinet.name)
            if(cabinetId == null){
                cabinetId = createInventoryLocation(locationId, null, 'cabinet', cabinet.name, 0, i)
            }

            setupSettings(cabinet.settings, locationId, cabinetId)
            setupDoors(locationId, cabinetId, cabinet.doors)
            setupDrawers(locationId, cabinetId, cabinet.drawers)

            i++;
        }
    }

    private setupDoors(locationId, cabinetId, doors){
        int i = 0
        for (door in doors) {
            def doorId = inventoryLocationService.findInventoryLocationId(locationId, cabinetId, 'door', door.name)
            if(doorId == null){
                doorId = createInventoryLocation(locationId, cabinetId, 'door', door.name, 0, i)
            }

            setupSettings(door.settings, locationId, doorId)

            setupShelves(locationId, doorId, door.shelves)

            i++
        }
    }

    private setupDrawers(locationId, cabinetId, drawers){
        int i = 0
        for (drawer in drawers) {
            def drawerId = inventoryLocationService.findInventoryLocationId(locationId, cabinetId, 'drawer', drawer.name)
            if(drawerId == null){
                drawerId = createInventoryLocation(locationId, cabinetId, 'drawer', drawer.name, 0, i)
            }

            setupSettings(drawer.settings, locationId, drawerId)
            setupPockets(locationId, drawerId, drawer.pockets)
            i++
        }
    }

    private setupShelves(locationId, doorId, shelves) {
        int i = 0
        for (shelf in shelves) {
            def shelfId = inventoryLocationService.findInventoryLocationId(locationId, doorId, 'shelf', shelf.name)
            if(shelfId == null){
                shelfId = createInventoryLocation(locationId, doorId, 'shelf', shelf.name, 0, i)
            }

            setupSettings(shelf.settings, locationId, shelfId)

            setupBins(locationId, shelfId, shelf.bins)

            i++
        }
    }

    private setupBins(locationId, shelfId, bins){
        int i = 0
        for(bin in bins) {
            def binId = inventoryLocationService.findInventoryLocationId(locationId, shelfId, 'quarterbin', bin.name)
            if(binId == null){
                binId = createInventoryLocation(locationId, shelfId, 'quarterbin', bin.name, i, i)
            }

            setupSettings(bin.settings, locationId, binId)

            setupPockets(locationId, binId, bin.pockets)

            i++
        }
    }

    private setupPockets(locationId, binId, pockets) {
        int i = 0
        for (pocket in pockets) {
            def pocketId = inventoryLocationService.findInventoryLocationId(locationId, binId, 'pocket', pocket.name)
            if(pocketId == null) {
                createInventoryLocation(locationId, binId, 'pocket', pocket.name, 0, i)
            }

            setupSettings(pocket.settings, locationId, pocketId)

            i++
        }
    }

    private createInventoryLocation(locationId, parentId, typeId, name, columnNum, orderNum=0){
        def response = inventoryLocationService.addInventoryLocation(locationId, parentId, typeId, name, columnNum, orderNum)
        def slurper = new JsonSlurper()
        def entities = slurper.parseText(response.json.toString())
        return entities['id']
    }

    private setupLocation(name){
        def locationId = locationService.getLocationId(name)
        if(locationId == null){
            locationId = locationService.addSite(name)
        }
        return locationId
    }

    private createOrUpdateSetting(settingDefinitionId, value, locationId, keyId){
        settingsService.createOrUpdateSetting(settingDefinitionId, value, environmentProperties.tenantId(), locationId, keyId)
    }

    private getPracticeSetup() {
        YamlReader reader = new YamlReader(this.getClass().getResource(PRACTICE_SETUP_RESOURCE).text)
        return (Map) reader.read(TreeMap.class)
    }

    private setupSettings(inventoryLocationSettings, locationId, keyId) {
        if(inventoryLocationSettings){
            for (setting in inventoryLocationSettings) {
                createOrUpdateSetting(setting.settingDefinitionId, setting.value, locationId, keyId)
            }
        }
    }

}
