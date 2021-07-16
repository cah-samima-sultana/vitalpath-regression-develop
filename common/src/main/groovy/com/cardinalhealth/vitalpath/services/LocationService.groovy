package com.cardinalhealth.vitalpath.services

import groovy.json.JsonSlurper

class LocationService extends  BaseService{

    private String REST_END_POINT = '/masterdata/locations/'

    LocationService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd) {
        super(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
    }


    def addSite(siteName){
        def addSiteResponse = post(REST_END_POINT){
            json businessName:siteName,
                    locationName:siteName,
                    address:"555 dublin rd",
                    city:"dublin",
                    state:"ohio",
                    zipCode:"43017",
                    phone:"5555555555",
                    fax:"5555555555",
                    active:true,
                    gmtOffset:-5,
                    dstObserves:true
        }

        // Whenever a site it created, replication routing entries are created as well.
        // This must pause to allow those entries to be processed before any other replicated
        // entries are created / updated
        sleep(61 * 1000)

        return addSiteResponse.json.id
    }

    def getLocationId(name){
        def encodedName = URLEncoder.encode(name,'UTF-8')
        def restEndPoint = "${REST_END_POINT}?locationName=${encodedName}"
        def response = get(restEndPoint, true)

        def locations = new JsonSlurper().parseText(response.json.toString())

        return locations.size > 0 ? locations.get(0).id : null
    }


}
