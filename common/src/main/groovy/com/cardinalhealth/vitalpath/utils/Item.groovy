package com.cardinalhealth.vitalpath.utils

@Deprecated
enum Item {
    CARBOPLATIN                 ("00703-4244-01", "CARBOplatin (equiv to PARAPLATIN)",          "CARBOplatin (equiv to PARAPLATIN)", "50",  "MG", "fuse-multi-dose-vial"),
    GEMCITABINE_00409_0183_01   ("00409-0183-01", "Gemcitabine HCl (equiv to GEMZAR)",          "Gemcitabine HCl (equiv to GEMZAR)","200", "MG", "fuse-single-dose-vial"),
    GEMCITABINE_00409_0185_01   ("00409-0185-01", "Gemcitabine HCl (equiv to GEMZAR)",          "Gemcitabine HCl (equiv to GEMZAR)","200", "MG", "fuse-single-dose-vial"),
    GEMCITABINE_23155_0213_31   ("23155-0213-31", "Gemcitabine HCl (equiv to GEMZAR)",          "Scott the Man","200", "MG", "fuse-single-dose-vial"),
    GEMCITABINE_55111_0686_07   ("55111-0686-07", "Gemcitabine HCl (equiv to GEMZAR)",          "Gemcitabine HCl (equiv to GEMZAR)","200", "MG", "fuse-single-dose-vial"),
    GEMCITABINE_16729_0117_11   ("16729-0117-11", "Gemcitabine HCl (equiv to GEMZAR)",          "Gemcitabine HCl (equiv to GEMZAR)","200", "MG", "fuse-single-dose-vial"),
    GEMCITABINE_00069_3858_10   ("00069-3858-10", "Gemcitabine HCl (equiv to GEMZAR)",          "Gemcitabine HCl (equiv to GEMZAR)","1000", "MG", "fuse-single-dose-vial"),
    GEMCITABINE_00069_3857_10   ("00069-3857-10", "Gemcitabine HCl (equiv to GEMZAR)",          "Gemcitabine HCl (equiv to GEMZAR)","200", "MG", "fuse-single-dose-vial"),
    FlUOROURACIL_16729_0276_68  ("16729-0276-68", "Fluorouracil  (equiv to ADRUCIL)",          "Fluorouracil  (equiv to ADRUCIL)","500", "MG", "fuse-single-dose-vial"),
    GEMZAR_00002_7501_01        ("00002-7501-01", "GEMZAR (Gemcitabine HCl)",          "GEMZAR (Gemcitabine HCl)","200", "MG", "fuse-single-dose-vial"),
    DECADRON_00641_0367_25      ("00641-0367-25", "Dexamethasone sod phos (equiv to DECADRON)", "Dexamethasone sod phos (equiv to DECADRON)","10", "MG", "fuse-single-dose-vial"),
    DECADRON_63323_0506_01      ("63323-0506-01", "Dexamethasone sod phos (equiv to DECADRON)", "Dexamethasone sod phos (equiv to DECADRON)","10", "MG", "fuse-single-dose-vial"),
    DECADRON_63323_0516_10      ("63323-0516-10", "Dexamethasone sod phos (equiv to DECADRON)", "Dexamethasone sod phos (equiv to DECADRON)","100", "MG", "fuse-multi-dose-vial"),
    DECADRON_63323_0165_30      ("63323-0165-30", "Dexamethasone sod phos (equiv to DECADRON)", "Dexamethasone sod phos (equiv to DECADRON)","120", "MG", "fuse-multi-dose-vial"),
    ACETAMINOPHEN57896_0101_01  ("57896-0101-01", "Acetaminophen (equiv to TYLENOL)",           "Acetaminophen (equiv to TYLENOL)","200", "MG", "fuse-form-tablet"),
    OXYCODONE66689_0403_16      ("66689-0403-16", "OxyCODONE HCl SOLN",                         "OxyCODONE HCl SOLN","1000", "MG", "fuse-ampule"),
    NEULASTA_55513_0190_01      ("55513-0190-01", "NEULASTA (Pegfilgrastim)",                   "NEULASTA (Pegfilgrastim)","6", "MG", "fuse-syringe"),
    DOCETAXEL_66758_0050_01     ("66758-0050-01", "DOCEtaxel (equiv to TAXOTERE)",              "DOCEtaxel (equiv to TAXOTERE)","20", "MG", "fuse-multi-dose-vial"),
    DOCETAXEL_00955_1020_01     ("00955-1020-01", "DOCEtaxel (equiv to TAXOTERE)",              "DOCEtaxel (equiv to TAXOTERE)","20", "MG", "fuse-multi-dose-vial"),
    DOCETAXEL_00409_0201_10     ("00409-0201-10", "DOCEtaxel (equiv to TAXOTERE)",              "DOCEtaxel (equiv to TAXOTERE)","80", "MG", "fuse-multi-dose-vial"),
    GARDASIL_00006_4045_41      ("00006-4045-41", "GARDASIL (Human papillomavirus vacc)",       "GARDASIL (Human papillomavirus vacc)","0.5", "ML", "fuse-single-dose-vial"),
    ALOXI_62856_0797_01         ("62856-0797-01", "ALOXI (Palonosetron HCl)",       "ALOXI (Palonosetron HCl)","250", "MCG", "fuse-single-dose-vial")


    private final String drugId
    private final String drugName
    private final String itemName
    private final String iconClass
    private final String tai
    private final String uom

    Item(drugId, drugName, itemName, tai, uom, iconClass) {
        this.drugName = drugName
        this.itemName = itemName
        this.drugId = drugId
        this.tai = tai
        this.uom = uom
        this.iconClass = iconClass
    }

    public String drugId() { drugId }
    public String drugName() { drugName }
    public String itemName() { itemName }
    public String tai() { tai }
    public String uom() { uom }
    public String iconClass() { iconClass }

    @Override
    public String toString() { drugName }
}

