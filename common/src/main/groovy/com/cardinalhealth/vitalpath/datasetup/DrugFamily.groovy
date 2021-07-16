package com.cardinalhealth.vitalpath.datasetup

enum DrugFamily {
    GEMZAR          ("2130003410","GEMZAR : Gemcitabine HCl"),
    FLUOROURACIL    ("2130003000","5-FU : Fluorouracil"),
    DECADRON        ("2210002020","DECADRON : Dexamethasone sod phos"),
    ALOXI           ("5025007010", "ALOXI : Palonosetron HCl"),
    NEULASTA           ("8240157000", "NEULASTA : Pegfilgrastim"),
    TYLENOL         ("6420001000", "TYLENOL : Acetaminophen"),
    ELOXATIN        ("ELOXATIN : Oxaliplatin", "ELOXATIN : Oxaliplatin"),
    ADRIAMYCIN      ("ADRIAMYCIN", "ADRIAMYCIN : DOXOrubicin HCl")


    private final String id
    private final String drugFamilyName

    DrugFamily(id,drugFamilyName) {
        this.id = id
        this.drugFamilyName = drugFamilyName
    }

    public String id() { id }
    public String drugFamilyName() { drugFamilyName }

    @Override
    public String toString() { id }
}

