package com.cardinalhealth.vitalpath.datasetup

enum ItemConstants {

    //GEMZAR_200mg_00409_0183_01('00409-0183-01', 'gemcitabine hcl (equiv to gemzar)', '200', 'mg', '2130003410'),
    GEMZAR_200mg_00409_0183_01('00409-0183-01', 'gemzar (gemcitabine hcl) 1000 mg', '200', 'mg', '2130003410'),
    //10,19, 170, 177
    GEMZAR_200mg_00002_7501_01('00002-7501-01', 'gemzar (gemcitabine hcl)', '100', 'mg', '2130003410'),
    //11
    GEMZAR_200mg_00002_7502_01('00002-7501-01', 'gemzar (gemcitabine hcl)', '200', 'mg', '2130003410'),

//166
    GEMZAR_200mg_00409_0185_01('00069-3858-10', 'gemcitabine hcl (equiv to gemzar)', '200', 'mg', '2130003410'),
    //13
    GEMZAR_1000mg_00069_3858_10('00002-7502-01', 'gemzar (gemcitabine hcl) 1000 mg', '1000', 'mg', '2130003410', '2130003410-1000/MG-INJ'),
    //22,72, 78
    GEMZAR_1000mg_16729_0117_11('00002-7502-01', 'gemzar (gemcitabine hcl) 1000 mg', '1000', 'mg', '2130003410', '2130003410-1000/MG-INJ'),

    ALOXI_200mcg_62856_0797_01('62856-0797-01', 'ALOXI (Palonosetron HCl)', '250', 'mcg', '5025007010'),
    ELOXATIN_100mg_00024_0591_20('00024-0591-20', 'ELOXATIN (Oxaliplatin)', '100', 'mg', '2110002800', '2110002800-100/MG-INJ'),
    DOXORUBICIN_HCL_150mg_00069_3033_20('00069-3033-20', 'DOXOrubicin HCl (equiv to ADRIAMYCIN)', '150', 'mg', '2120004010', '2120004010-150/MG-INJ'),
    DECADRON_100mg_63323_0516_10('63323-0516-10', 'Dexamethasone sod phos (equiv to DECADRON)', '100', 'mg', '2210002020'),
    NEULASTA_6mg_55513_0190_01('55513-0190-01', 'NEULASTA (Pegfilgrastim)', '6', 'mg', '8240157000'),
    NEULASTA_55513_0192_01('55513-0192-01', 'NEULASTA OnPro KIT (Pegfilgrastim)', '6', 'mg', '8240157000'),
    FLUOROURACIL_500mg_16729_0276_68('16729-0276-68', 'Fluorouracil (equiv to ADRUCIL)', '500', 'mg', '2130003000'),
    HYDROMORPHONE_DILAUDID2mg_59011_0442_10('59011-0442-10', 'DILAUDID (HYDROmorphone)', '2', 'mg', '6510003510'),
    THALOMID_1400mg_59572_0205_14('59572-0205-14', 'THALOMID (Thalidomide)', '1400', 'mg'),
    ARANESP_300mcg_55513_0111_01('55513-0111-01', 'ARANESP (Darbepoetin alfa/polysorbate)', '300', 'mcg'),

    ACETAMINOPHEN_325mg_00536_3222_01('00536-3222-01', 'Acetaminophen (equiv to TYLENOL)', '325', 'mg', '6420001000', '6420001000-325/MG-ORAL'),
    ACETAMINOPHEN_325mg_57896_0101_01('57896-0101-01', 'Acetaminophen (equiv to TYLENOL)', '325', 'mg', '6420001000', '6420001000-325/MG-ORAL'),
    ACETAMINOPHEN_325mg_00904_1982_60('00904-1982-60', 'Acetaminophen (equiv to TYLENOL)', '325', 'mg', '6420001000', '6420001000-325/MG-ORAL'),

    TENNONCRO5520985_100mg_TNONC_ABAF_0100('TNONC-ABAF-0100', 'TENN-ONC RO5520985/Bevacizumab (STUDY) 2/12', '100', 'mg', 'INV0000371', 'INV0000371-100/MG-INJ'),

    THERMAL_PAPER(null, 'thermal paper', null, null, null, null, false)

    private final String ndc
    private final String name
    private final String tai
    private final String uom
    private final String digId
    private final String drugFamilyId
    private String itemId
    private boolean isDrug

    ItemConstants(String ndc, String name, String tai, String uom, String drugFamilyId = '', String digId = '', boolean isDrug = true) {
        this.ndc = ndc
        this.name = name
        this.tai = tai
        this.uom = uom
        this.digId = digId
        this.drugFamilyId = drugFamilyId
        this.isDrug = isDrug
    }

    String getNdc() {
        return ndc
    }

    String getName() {
        return name
    }

    String getTai() {
        return tai
    }

    String getUom() {
        return uom
    }

    String getDigId() {
        return digId
    }

    String getDrugFamilyId() {
        return drugFamilyId
    }

    String getItemId() {
        return itemId
    }

    void setItemId(String itemId) {
        this.itemId = itemId
    }

    boolean getIsDrug() {
        return isDrug
    }
}