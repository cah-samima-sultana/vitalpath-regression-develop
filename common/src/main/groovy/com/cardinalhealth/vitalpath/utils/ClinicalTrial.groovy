package com.cardinalhealth.vitalpath.utils

enum ClinicalTrial {
    PANACEA ("PANACEA Trial")

    private final String trialName

    ClinicalTrial(trialName) {
        this.trialName = trialName
    }

    public String trialName() { trialName }

    @Override
    public String toString() { trialName }
}

