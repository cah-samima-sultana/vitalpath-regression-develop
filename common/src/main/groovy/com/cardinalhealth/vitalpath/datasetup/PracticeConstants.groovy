package com.cardinalhealth.vitalpath.datasetup

class PracticeConstants {

     static enum Site_1_Layout {
         //108
         CABINET_A_DOOR_2('Cabinet A / door 1 / shelf 2 / bin 1 / pocket1'),
         //110,116
         CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1('Cabinet A / door 2 / shelf 1'),
         //CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1('Cabinet A / door 1 / shelf 2 / bin 1 / pocket1'),
         CABINET_A_DOOR_1_SHELF_2_BIN_2_POCKET_1('Cabinet A / Door 1 / Shelf 2 / Bin 2 / pocket1'),
         //111
         CABINET_A_DOOR_1_SHELF_2_BIN_3_POCKET_1('Cabinet A / Door 2 / Shelf 2'),
         CABINET_A_DOOR_3('Cabinet A / Door 2'),
        // line(14),171
         CABINET_A_DOOR_2_SHELF_1('Cabinet A / door 2'),
         CABINET_A_DOOR_2_SHELF_1_BIN_1_POCKET_1('Cabinet A / Door 2 / Shelf 1 / Bin 1 / pocket1'),
         //13,21
         CABINET_A_DOOR_2_SHELF_2('Cabinet A / door 1 / shelf 2 / bin 3 / pocket1'),
         //112
         CABINET_A_DOOR_1_SHELF_2('Cabinet A / Door 1 / Shelf 2'),
         //109,119, 175
         CABINET_A_DOOR_2_SHELF_3('Cabinet A / door 1 / shelf 2 / bin 3 / pocket1'),

         CABINET_A_DOOR_3_SHELF_3('Cabinet A / Door 3 / Shelf 3'),
         //22
         CABINET_A_DOOR_4_SHELF_2_BIN_3_POCKET_1('Cabinet A / Door 2'),
         CABINET_A_DOOR_4('Cabinet A / Door 4'),
         CABINET_A_DRAWER_8_POCKET_1('Cabinet A / Drawer 8 / pocket1'),
         CABINET_B_DOOR_1_SHELF_1_BIN_1_POCKET_1('Cabinet B / Door 1 / Shelf 1 / Bin 1 / pocket1'),
         CABINET_B_DOOR_3_SHELF_1_BIN_2_POCKET_1('Cabinet B / Door 3 / Shelf 1 / Bin 2 / pocket1')

        String fullPath

        Site_1_Layout(path){
            this.fullPath = path
        }

        def path(){
            return fullPath
        }

        def displayPath(){
            fullPath.substring(fullPath.indexOf('/') + 2)
        }
    }

    static enum Site_2_Layout {

        CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET1('Cabinet A / Door 1 / Shelf 2 / Bin 1 / pocket1')

        String fullPath

        Site_2_Layout(path){
            this.fullPath = path
        }

        def path(){
            return fullPath
        }

        def displayPath(){
            fullPath.substring(fullPath.indexOf('/') + 1).trim()
        }
    }

    public static final String SITE_1 = 'Site 1'
    public static final String SITE_2 = 'Site 2'
    public static final String CABINET_A = 'Cabinet A'
    public static final String CABINET_B = 'Cabinet B'
}
