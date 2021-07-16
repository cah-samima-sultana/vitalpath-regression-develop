package com.cardinalhealth.vitalpath.datasetup

class SettingsConstant {
    public static final String TRUE = 'true'
    public static final String FALSE = 'false'
    public static final String RETINA = 'retina'
    public static final String ONCOLOGY = 'oncology'

    public static final String CABINET_TO_CABINET_TRANSFER = 'cabtocabtransferenabled'
    public static final String AUTO_PRINT_LABELS = 'autoprintlabels'
    public static final String CLINICAL_ORDER_LINE_PROMPT = 'clinicalorderlineprompt'
    public static final String CLINICAL_TRIAL_ENABLE = 'clinicaltrialenable'
    public static final String CONTROLLED_SUBSTANCE_VERIFICATION = 'controlledsubstanceverification'
    public static final String DISPENSE_DOOR_SELECT = 'dispensedoorselect'
    public static final String PLAN_DISPENSE_SORT = 'plandispensesort'
    public static final String SYSTEM_USER_LOGIN_REASON = 'systemuserloginreason'
    public static final String WHITE_BAG = 'whitebag'
    public static final String EXPIRED_ITEM_WARNING_DAYS = 'expireditemwarningdays'
    public static final String REUSE_SDV_WASTE = 'reusesdvwaste'
    public static final String COMPLETED_FILTER = 'sfCompleteEnabled'
    public static final String CLINICAL_TRIAL_FILTER = 'sfHasClinicalTrialsEnabled'
    public static final String NOTIFICATIONS_FILTER = 'sfHasNotificationsEnabled'
    public static final String IN_PROGRESS_FILTER = 'sfInProgressEnabled'
    public static final String NEEDS_ATTENTION_FILTER = 'sfNeedsAttentionEnabled'
    public static final String NOT_STARTED_FILTER = 'sfNotStartedEnabled'
    public static final String PLANNED_FILTER = 'sfPlannedEnabled'
    public static final String CENTRAL_DISPENSE = 'centraldispense'
    public static final String AUTO_PLAN = 'dispenseautoplan'
    public static final String POP_INDIVIDUAL_DOORS = 'dispensedoorselect'
    public static final String SHOW_TNONC_DRUGS = 'showTNONCdrugs'
    public static final String SUGGESTED_DISPENSE = 'suggesteddispense'
    public static final String BUSINESS_TYPE = 'businesstype'
    public static final String TREATMENT_DAYS = 'treatmentdays'

    public static final String LABEL_TEMPLATE_DEFINITION = 'labeltemplate'

    class Labels {
        public static final String LABEL_TEMPLATE_1 = 'label-template-01'
        public static final String LABEL_TEMPLATE_7 = 'label-template-07'
    }

    class ClinicalOrderLinePrompt{
        public static final String DRUG_FAMILY_ONLY = 'DRUGFAMILYONLY'
        public static final String DRUG_FAMILY_FOCUS ='DRUGFAMILYFOCUS'
        public static final String ITEM_ONLY ='ITEMONLY'
        public static final String ITEM_FOCUS ='ITEMFOCUS'
    }

    class PlanDispenseSort{
        public static final String LAW ='LAW'
        public static final String MAN ='MAN'
    }

}
