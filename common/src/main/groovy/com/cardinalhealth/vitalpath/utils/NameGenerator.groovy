package com.cardinalhealth.vitalpath.utils

public class NameGenerator {

    static lastNames = [
            'Arnold','Shurtz','Oneal','Lynch','Layton','Thompson','Wheeler','Kelly','Langston','Kissee','Smith','Fernandez','McDaniels','Ball','Evatt','Carter','Drinnon','Clark',
            'Cortez','Boyd','Garcia','Duff','Trujillo','Valenzuela','Barnes','Henson','Estrada','Buzbee','Harms','Martin','Wyckoff','Speight','Gonzales','Evans','Jones','Hook','Mabe',
            'Demelo','Miller','My','Thomas','Chapman','Moore','Dowd','Morgan','Cross','Hale','Meza','Bingham','Dever','King','Bostic','Martinez','Dobson','Guyton','Kahler','Wade','Welch',
            'Dube','Ruiz','Rogers','Arney','Bird','Marvin','Rock','Woodcock','Baker','Stafford','Taylor','Neely','Stennett','Glatt','Cruz','Cochran','Bader','Collins','Nelson','Arruda',
            'Cagle','Warren','Riordan','Bolden','Rodriquez','Morales','Watson','Oleary','Foster','Lucero','Eldredge','Maddux','Beardsley','Richardson','Goldsmith','Burkey','Herbert',
            'Adams','Lee','Morrone','Merriman','Partain','Robinson','Wright','Ziebarth','Coleman','Bulger','Ouzts','Jarman','Herrington','Kingsbury','Whitbeck','Millett','Lopes','Hagan',
            'Messner','Vanwyk','Blauvelt','Edwards','Crowe','Glass','Santo','Guffey','Hassel','Cecil','Williamson','Cole','Hackbarth','Eden','Householder','Maher','Quan','Vail','Liner',
            'Hitchcock','Green','Peters','Lawton','Stearns','George','Doyle','Duplessis','Thatcher','Rigney','Gilroy','Williams','Long','Rountree','Neeley','Reed','Carbone','Hollander',
            'Salazar','Kennedy','Gamboa','Grant','Hebert','Maldonado','Burton','Alicea','Douglas','Johnson','Mendez','Shank','Lovett','Rosario','Jacobs','Archambault','Aguilar','Parker',
            'Lopez','Pritchard','McAndrew','Miner','Wooden','Abbott','Chittum','Hernandez','Perez','Lankford','Lozano','Steele','Keys','Ballard','Holland','Even','Kobayashi','Mangrum',
            'Brewer','Perry','Francisco','Willis','James','Madden','Osborne','Sharp','Bishop','Govan','Branch','Huckaby','Rivera','Butz','Cantrell','Curtiss','Logan','Quezada','Bartlett',
            'Drew','Mitchell','Riley','Lawrence','Bateman'
    ]

    static firstNames = [
            'Lee','Samantha','Carl','Gregorio','Leila','Michael','Florinda','Joann','Luis','Allen','Sarah','John','Marion','Vance','Elizabeth','Vicky','Donald',
            'Louise','Linda','Vida','Cordell','Amanda','Eugenia','Edna','Daniel','Ricardo','Brian','Richard','Julio','Jose','Angel','Kristen','Maurice','Terry',
            'Mary','Freddie','Kathy','Ruth','David','Roland','Judy','Elsa','Cynthia','Ramon','Alejandro','Andrew','Deena','Grace','Barbara','Roy','Velda','Paulette',
            'Ana','Christina','Craig','Lyle','Randy','Crystal','Angela','William','Paul','Louis','Larry','Colleen','Patrick','Mark','Ryan','Nicole','Douglas','Pedro',
            'Christine','Omar','Derek','Matthew','Joseph','Jim','Kathryn','Frank','Carolyn','Donna','Shannon','Jerry','Susan','Gilbert','Clara','Roberto','Rebecca',
            'Lloyd','Lindsey','Willie','Rodney','Fred','Wesley','Reynaldo','Todd','Ann','Katherine','Sam','Anthony','Deborah','Priscilla','Debra','Charles','Janet',
            'Lyndsay','Lori','James','Sophie','Gerald','Melvin','Sandy','Joan','Evelyn','Wendy','Rafael','Erwin','Ruby','Arthur','Dorothy','Sharon','Virginia','Hobert',
            'Annette','Randall','Tami','Quinton','Jackeline','Danny','Arnold','Benjamin','Janie','Jean','Kevin','Krysta','Doris','Ida','Joni','Timothy','Katrina','Thomas',
            'Robert','Earl','Angelo','Saul','Pete','Peggy','Katie','Bradford','Vera','Delbert','Oscar','Millard','Dave','Kelly','Luke','Andres','Russell','Catherine','Claud',
            'Raul','Monique','Shirley','Gina','Ronald','Eduardo','Marlon','Keith','George','Glenn','Zachary','Frances','Efrain','Rosemary','Bambi','Margarett','Leon','Emma',
            'Franklin','Barry','Pamela','Meredith','Loyd','Pauline','Tara','Max'
    ]

    static middleNames = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J']


    public static String getFirstName(){
        return getRandomElement(firstNames)
    }

    public static String getLastName(){
        return getRandomElement(lastNames)
    }

    public static String getMiddleName(){
        return getRandomElement(middleNames)
    }

    private static String getRandomElement(ArrayList<String> nameArray){
        int rnd = new Random().nextInt(nameArray.size())
        return nameArray[rnd]
    }
}
