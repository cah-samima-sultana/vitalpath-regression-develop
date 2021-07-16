package com.cardinalhealth.vitalpath.datasetup

class Suite {

    static void main(String[] args) {
        try {
            System.println("Starting Practice Setup");
            new Practice().setup()
            System.println("Starting Inventory Setup");
            new Inventory().setup()
            System.println("Starting Physicians Setup");
            new Physicians().setup()
        }catch(Exception e){
            e.printStackTrace()
        }
    }
}
