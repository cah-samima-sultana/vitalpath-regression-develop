package com.cardinalhealth.vitalpath.client.specs.inventory.digpars;

import com.cardinalhealth.vitalpath.EnvironmentProperties;
import com.cardinalhealth.vitalpath.client.specs.BaseSpec;
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants;

class DigParBase extends BaseSpec {

    def clearParts(){
        EnvironmentProperties env = EnvironmentProperties.instance
        def browserSite1 = env.browserVersion() + " " + PracticeConstants.SITE_1
        def browserSite1Id = locationService.getLocationId(browserSite1)

        def pars = cabinetPartParService.findParsByLocation(browserSite1Id)
        cabinetPartParService.deletePars(pars)

        distributorPartNumberService.deletePreferredParts(browserSite1Id)
    }

}