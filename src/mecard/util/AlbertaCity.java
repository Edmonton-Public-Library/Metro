/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Edmonton Public Library
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 *
 */
package mecard.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import mecard.Protocol;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;

/**
 * Source:
 * 
 * Municipal Services Branch
 * 17th Floor Commerce Place
 * 10155 - 102 Street Edmonton, Alberta T5J 4L4
 * Phone: 780-427-2225 Fax: 780-420-1016
 * E-mail: lgs.update@gov.ab.ca
 * 
 * Updated December 28, 2012
 * 
 * Since the initial release there has been a requirement to add place names
 * commonly used for Alberta, but not 'sanctioned'. None-the-less that is where
 * mail is sent and that is the address the people that live there refer to.
 * All additional none sanctioned names are listed in the 6xxx range of codes.
 * 
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public final class AlbertaCity extends City
{
    private static HashMap<String, String> cityMap;
    private static City instance;
    
    public static City getInstanceOf()
    {
        if (instance == null)
        {
            instance = new AlbertaCity();
        }
        return instance;
    }
    
    @Override
    public String getPlaceNameLike(String placeNameFragment)
    {
        for (String fullPlaceName: cityMap.keySet())
        {
            if (fullPlaceName.toLowerCase().endsWith(placeNameFragment.toLowerCase()))
            {
                return fullPlaceName;
            }
        }
        return "";
    }
    
    @Override
    public boolean isPlaceName(String placeName)
    {
        for (String fullPlaceName: cityMap.keySet())
        {
            if (fullPlaceName.compareToIgnoreCase(placeName) == 0)
            {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public List<String> getPlaceNames(String place)
    {
        List<String> listOfNames = new ArrayList<>();
        for (String fullPlaceName: cityMap.keySet())
        {
            if (fullPlaceName.toLowerCase().endsWith(place.toLowerCase()))
            {
                listOfNames.add(fullPlaceName);
            }
        }
        return listOfNames;
    }
    
    @Override
    public String getCityCode(String cityName)
    {
        String returnCity = Protocol.DEFAULT_FIELD_VALUE;
        for (String fullPlaceName: cityMap.keySet())
        {
            if (fullPlaceName.compareToIgnoreCase(cityName) == 0)
            {
                return cityMap.get(fullPlaceName); 
            }
        }
        return returnCity;
    }
    
    private AlbertaCity()
    {
        cityMap = new HashMap<>();
        cityMap.put("Airdrie","0003");
        cityMap.put("Brooks","0043");
        cityMap.put("Calgary","0046");
        cityMap.put("Camrose","0048");
        cityMap.put("Cold Lake","0525");
        cityMap.put("Edmonton","0098");
        cityMap.put("Fort Saskatchewan","0117");
        cityMap.put("Grande Prairie","0132");
        cityMap.put("Lacombe","0194");
        cityMap.put("Leduc","0200");
        cityMap.put("Lethbridge","0203");
        cityMap.put("Lloydminster","0206");
        cityMap.put("Medicine Hat","0217");
        cityMap.put("Red Deer","0262");
        cityMap.put("Spruce Grove","0291");
        cityMap.put("St. Albert","0292");
        cityMap.put("Wetaskiwin","0347");
        cityMap.put("Athabasca","0011");
        cityMap.put("Banff","0387");
        cityMap.put("Barrhead","0014");
        cityMap.put("Bashaw","0016");
        cityMap.put("Bassano","0017");
        cityMap.put("Beaumont","0019");
        cityMap.put("Beaverlodge","0021");
        cityMap.put("Bentley","0024");
        cityMap.put("Black Diamond","0030");
        cityMap.put("Blackfalds","0031");
        cityMap.put("Bon Accord","0034");
        cityMap.put("Bonnyville","0035");
        cityMap.put("Bow Island","0039");
        cityMap.put("Bowden","0040");
        cityMap.put("Bruderheim","0044");
        cityMap.put("Calmar","0047");
        cityMap.put("Canmore","0050");
        cityMap.put("Cardston","0052");
        cityMap.put("Carstairs","0056");
        cityMap.put("Castor","0058");
        cityMap.put("Chestermere","0356");
        cityMap.put("Claresholm","0065");
        cityMap.put("Coaldale","0069");
        cityMap.put("Coalhurst","0360");
        cityMap.put("Cochrane","0070");
        cityMap.put("Coronation","0075");
        cityMap.put("Crossfield","0079");
        cityMap.put("Daysland","0082");
        cityMap.put("Devon","0086");
        cityMap.put("Didsbury","0088");
        cityMap.put("Drayton Valley","0091");
        cityMap.put("Drumheller","0532");
        cityMap.put("Eckville","0095");
        cityMap.put("Edson","0100");
        cityMap.put("Elk Point","0101");
        cityMap.put("Fairview","0106");
        cityMap.put("Falher","0108");
        cityMap.put("Fort Macleod","0115");
        cityMap.put("Fox Creek","0119");
        cityMap.put("Gibbons","0124");
        cityMap.put("Grande Cache","0131");
        cityMap.put("Granum","0135");
        cityMap.put("Grimshaw","0137");
        cityMap.put("Hanna","0141");
        cityMap.put("Hardisty","0143");
        cityMap.put("High Level","0146");
        cityMap.put("High Prairie","0147");
        cityMap.put("High River","0148");
        cityMap.put("Hinton","0151");
        cityMap.put("Innisfail","0180");
        cityMap.put("Irricana","0183");
        cityMap.put("Killam","0188");
        cityMap.put("Lamont","0197");
        cityMap.put("Legal","0202");
        cityMap.put("Magrath","0211");
        cityMap.put("Manning","0212");
        cityMap.put("Mayerthorpe","0215");
        cityMap.put("McLennan","0216");
        cityMap.put("Milk River","0218");
        cityMap.put("Millet","0219");
        cityMap.put("Morinville","0224");
        cityMap.put("Mundare","0227");
        cityMap.put("Nanton","0232");
        cityMap.put("Okotoks","0238");
        cityMap.put("Olds","0239");
        cityMap.put("Onoway","0240");
        cityMap.put("Oyen","0241");
        cityMap.put("Peace River","0247");
        cityMap.put("Penhold","0248");
        cityMap.put("Picture Butte","0249");
        cityMap.put("Pincher Creek","0250");
        cityMap.put("Ponoka","0254");
        cityMap.put("Provost","0257");
        cityMap.put("Rainbow Lake","0260");
        cityMap.put("Raymond","0261");
        cityMap.put("Redcliff","0264");
        cityMap.put("Redwater","0265");
        cityMap.put("Rimbey","0266");
        cityMap.put("Rocky Mountain House","0268");
        cityMap.put("Sedgewick","0280");
        cityMap.put("Sexsmith","0281");
        cityMap.put("Slave Lake","0284");
        cityMap.put("Smoky Lake","0285");
        cityMap.put("Spirit River","0289");
        cityMap.put("St. Paul","0293");
        cityMap.put("Stavely","0297");
        cityMap.put("Stettler","0298");
        cityMap.put("Stony Plain","0301");
        cityMap.put("Strathmore","0303");
        cityMap.put("Sundre","0307");
        cityMap.put("Swan Hills","0309");
        cityMap.put("Sylvan Lake","0310");
        cityMap.put("Taber","0311");
        cityMap.put("Three Hills","0316");
        cityMap.put("Tofield","0318");
        cityMap.put("Trochu","0320");
        cityMap.put("Turner Valley","0321");
        cityMap.put("Two Hills","0322");
        cityMap.put("Valleyview","0325");
        cityMap.put("Vauxhall","0326");
        cityMap.put("Vegreville","0327");
        cityMap.put("Vermilion","0328");
        cityMap.put("Viking","0331");
        cityMap.put("Vulcan","0333");
        cityMap.put("Wainwright","0335");
        cityMap.put("Wembley","0343");
        cityMap.put("Westlock","0345");
        cityMap.put("Whitecourt","0350");
        cityMap.put("Acme","0002");
        cityMap.put("Alberta Beach","0004");
        cityMap.put("Alix","0005");
        cityMap.put("Alliance","0006");
        cityMap.put("Amisk","0007");
        cityMap.put("Andrew","0008");
        cityMap.put("Arrowwood","0010");
        cityMap.put("Barnwell","0363");
        cityMap.put("Barons","0013");
        cityMap.put("Bawlf","0018");
        cityMap.put("Beiseker","0022");
        cityMap.put("Berwyn","0025");
        cityMap.put("Big Valley","0027");
        cityMap.put("Bittern Lake","0029");
        cityMap.put("Botha","0038");
        cityMap.put("Boyle","0041");
        cityMap.put("Breton","0042");
        cityMap.put("Carbon","0051");
        cityMap.put("Carmangay","0054");
        cityMap.put("Caroline","0055");
        cityMap.put("Cereal","0060");
        cityMap.put("Champion","0061");
        cityMap.put("Chauvin","0062");
        cityMap.put("Chipman","0064");
        cityMap.put("Clive","0066");
        cityMap.put("Clyde","0068");
        cityMap.put("Consort","0073");
        cityMap.put("Coutts","0076");
        cityMap.put("Cowley","0077");
        cityMap.put("Cremona","0078");
        cityMap.put("Czar","0081");
        cityMap.put("Delburne","0083");
        cityMap.put("Delia","0084");
        cityMap.put("Dewberry","0087");
        cityMap.put("Donalda","0089");
        cityMap.put("Donnelly","0090");
        cityMap.put("Duchess","0093");
        cityMap.put("Edberg","0096");
        cityMap.put("Edgerton","0097");
        cityMap.put("Elnora","0102");
        cityMap.put("Empress","0103");
        cityMap.put("Ferintosh","0109");
        cityMap.put("Foremost","0112");
        cityMap.put("Forestburg","0113");
        cityMap.put("Gadsby","0121");
        cityMap.put("Galahad","0122");
        cityMap.put("Girouxville","0125");
        cityMap.put("Glendon","0127");
        cityMap.put("Glenwood","0128");
        cityMap.put("Halkirk","0140");
        cityMap.put("Hay Lakes","0144");
        cityMap.put("Heisler","0145");
        cityMap.put("Hill Spring","0149");
        cityMap.put("Hines Creek","0150");
        cityMap.put("Holden","0152");
        cityMap.put("Hughenden","0153");
        cityMap.put("Hussar","0154");
        cityMap.put("Hythe","0155");
        cityMap.put("Innisfree","0181");
        cityMap.put("Irma","0182");
        cityMap.put("Kitscoty","0190");
        cityMap.put("Linden","0205");
        cityMap.put("Lomond","0207");
        cityMap.put("Longview","0208");
        cityMap.put("Lougheed","0209");
        cityMap.put("Mannville","0213");
        cityMap.put("Marwayne","0214");
        cityMap.put("Milo","0220");
        cityMap.put("Minburn","0221");
        cityMap.put("Morrin","0225");
        cityMap.put("Munson","0228");
        cityMap.put("Myrnam","0229");
        cityMap.put("Nampa","0231");
        cityMap.put("Nobleford","0236");
        cityMap.put("Paradise Valley","0244");
        cityMap.put("Rockyford","0270");
        cityMap.put("Rosalind","0271");
        cityMap.put("Rosemary","0272");
        cityMap.put("Rycroft","0275");
        cityMap.put("Ryley","0276");
        cityMap.put("Spring Lake","0099");
        cityMap.put("Standard","0295");
        cityMap.put("Stirling","0300");
        cityMap.put("Strome","0304");
        cityMap.put("Thorsby","0315");
        cityMap.put("Tilley","0317");
        cityMap.put("Veteran","0330");
        cityMap.put("Vilna","0332");
        cityMap.put("Wabamun","0364");
        cityMap.put("Warburg","0338");
        cityMap.put("Warner","0339");
        cityMap.put("Waskatenau","0342");
        cityMap.put("Willingdon","0352");
        cityMap.put("Youngstown","0355");
        cityMap.put("Argentia Beach","0009");
        cityMap.put("Betula Beach","0026");
        cityMap.put("Birch Cove","0384");
        cityMap.put("Birchcliff","0028");
        cityMap.put("Bondiss","0367");
        cityMap.put("Bonnyville Beach","0037");
        cityMap.put("Burnstick Lake","0414");
        cityMap.put("Castle Island","0057");
        cityMap.put("Crystal Springs","0080");
        cityMap.put("Ghost Lake","0123");
        cityMap.put("Golden Days","0129");
        cityMap.put("Grandview","0134");
        cityMap.put("Gull Lake","0138");
        cityMap.put("Half Moon Bay","0358");
        cityMap.put("Horseshoe Bay","0375");
        cityMap.put("Island Lake","0185");
        cityMap.put("Island Lake South","0368");
        cityMap.put("Itaska Beach","0186");
        cityMap.put("Jarvis Bay","0379");
        cityMap.put("Kapasiwin","0187");
        cityMap.put("Lakeview","0196");
        cityMap.put("Larkspur","0378");
        cityMap.put("Ma-Me-O Beach","0210");
        cityMap.put("Mewatha Beach","0359");
        cityMap.put("Nakamun Park","0230");
        cityMap.put("Norglenwold","0237");
        cityMap.put("Norris Beach","0385");
        cityMap.put("Parkland Beach","0374");
        cityMap.put("Pelican Narrows","0362");
        cityMap.put("Point Alison","0253");
        cityMap.put("Poplar Bay","0256");
        cityMap.put("Rochon Sands","0267");
        cityMap.put("Ross Haven","0273");
        cityMap.put("Sandy Beach","0277");
        cityMap.put("Seba Beach","0279");
        cityMap.put("Silver Beach","0282");
        cityMap.put("Silver Sands","0283");
        cityMap.put("South Baptiste","0369");
        cityMap.put("South View","0288");
        cityMap.put("Sunbreaker Cove","0388");
        cityMap.put("Sundance Beach","0306");
        cityMap.put("Sunrise Beach","0386");
        cityMap.put("Sunset Beach","0357");
        cityMap.put("Sunset Point","0308");
        cityMap.put("Val Quentin","0324");
        cityMap.put("Waiparous","0380");
        cityMap.put("West Baptiste","0370");
        cityMap.put("West Cove","0344");
        cityMap.put("Whispering Hills","0371");
        cityMap.put("White Sands","0365");
        cityMap.put("Yellowstone","0354");
        cityMap.put("Abee","0601");
        cityMap.put("Acadia Valley","0602");
        cityMap.put("Aetna","0603");
        cityMap.put("Alcomdale","0604");
        cityMap.put("Alder Flats","0605");
        cityMap.put("Aldersyde","0606");
        cityMap.put("Alhambra","0607");
        cityMap.put("Altario","0608");
        cityMap.put("Antler Lake","0609");
        cityMap.put("Anzac","0610");
        cityMap.put("Ardley","0611");
        cityMap.put("Ardmore","0612");
        cityMap.put("Ardrossan","0613");
        cityMap.put("Armena","0959");
        cityMap.put("Ashmont","0614");
        cityMap.put("Atmore","0616");
        cityMap.put("Balzac","0987");
        cityMap.put("Beauvallon","1050");
        cityMap.put("Beaver Crossing","0956");
        cityMap.put("Beaver Lake","0617");
        cityMap.put("Beaver Mines","0618");
        cityMap.put("Beaverdam","0619");
        cityMap.put("Beazer","0620");
        cityMap.put("Bellis","0621");
        cityMap.put("Benalto","0622");
        cityMap.put("Benchlands","0623");
        cityMap.put("Bezanson","0624");
        cityMap.put("Bindloss","0625");
        cityMap.put("Bircham","1342");
        cityMap.put("Blackfoot","0626");
        cityMap.put("Blackie","0032");
        cityMap.put("Blue Ridge","0627");
        cityMap.put("Bluesky","0628");
        cityMap.put("Bluffton","0629");
        cityMap.put("Bodo","0630");
        cityMap.put("Bottrel","0988");
        cityMap.put("Bow City","5682");
        cityMap.put("Bragg Creek","0631");
        cityMap.put("Brant","0632");
        cityMap.put("Breynat","0950");
        cityMap.put("Brosseau","1221");
        cityMap.put("Brownfield","0633");
        cityMap.put("Brownvale","0634");
        cityMap.put("Bruce","0635");
        cityMap.put("Brule","0636");
        cityMap.put("Buck Creek","0637");
        cityMap.put("Buck Lake","0638");
        cityMap.put("Buford","0640");
        cityMap.put("Burdett","0045");
        cityMap.put("Busby","0641");
        cityMap.put("Byemoor","0642");
        cityMap.put("Cadogan","0643");
        cityMap.put("Cadomin","0644");
        cityMap.put("Cadotte Lake","0645");
        cityMap.put("Calahoo","0646");
        cityMap.put("Calling Lake","0983");
        cityMap.put("Campsie","0648");
        cityMap.put("Canyon Creek","0898");
        cityMap.put("Carbondale","0993");
        cityMap.put("Cardiff","0994");
        cityMap.put("Carseland","0651");
        cityMap.put("Carvel","0652");
        cityMap.put("Carway","0964");
        cityMap.put("Caslan","0653");
        cityMap.put("Cassils","5683");
        cityMap.put("Cayley","0059");
        cityMap.put("Cessford","0654");
        cityMap.put("Chancellor","0655");
        cityMap.put("Cheadle","0656");
        cityMap.put("Cherhill","0657");
        cityMap.put("Cherry Grove","0658");
        cityMap.put("Chin","0659");
        cityMap.put("Chinook","0660");
        cityMap.put("Chisholm","0661");
        cityMap.put("Clairmont","0662");
        cityMap.put("Clandonald","0663");
        cityMap.put("Cleardale","0664");
        cityMap.put("Cluny","0067");
        cityMap.put("Cochrane Lake","0665");
        cityMap.put("Colinton","0666");
        cityMap.put("Collingwood Cove","0668");
        cityMap.put("Compeer","0669");
        cityMap.put("Condor","0670");
        cityMap.put("Conklin","0671");
        cityMap.put("Conrich","0672");
        cityMap.put("Craigmyle","0673");
        cityMap.put("Cynthia","0674");
        cityMap.put("Dalemead","0675");
        cityMap.put("Dalroy","0676");
        cityMap.put("Dapp","0677");
        cityMap.put("De Winton","0678");
        cityMap.put("DeBolt","0680");
        cityMap.put("Dead Man's Flats","3856");
        cityMap.put("Deadwood","0679");
        cityMap.put("Del Bonita","0681");
        cityMap.put("Delacour","0989");
        cityMap.put("Demmitt","1262");
        cityMap.put("Derwent","0085");
        cityMap.put("Desert Blume","4130");
        cityMap.put("Diamond City","0682");
        cityMap.put("Dickson","0683");
        cityMap.put("Dimsdale","1281");
        cityMap.put("Dixonville","0684");
        cityMap.put("Donatville","0952");
        cityMap.put("Dorothy","0685");
        cityMap.put("Duffield","0686");
        cityMap.put("Duhamel","0960");
        cityMap.put("Dunmore","0687");
        cityMap.put("Duvernay","0688");
        cityMap.put("Eaglesham","0094");
        cityMap.put("Edwand","1301");
        cityMap.put("Egremont","0691");
        cityMap.put("Ellscott","0951");
        cityMap.put("Elmworth","1241");
        cityMap.put("Enchant","0692");
        cityMap.put("Endiang","0693");
        cityMap.put("Enilda","0694");
        cityMap.put("Ensign","0695");
        cityMap.put("Entwistle","0104");
        cityMap.put("Erskine","0696");
        cityMap.put("Etzikom","0697");
        cityMap.put("Evansburg","0105");
        cityMap.put("Exshaw","0698");
        cityMap.put("Fabyan","0699");
        cityMap.put("Fallis","0700");
        cityMap.put("Falun","0701");
        cityMap.put("Faust","0702");
        cityMap.put("Fawcett","0703");
        cityMap.put("Flatbush","0705");
        cityMap.put("Fleet","0706");
        cityMap.put("Fort Assiniboine","0114");
        cityMap.put("Fort Chipewyan","0707");
        cityMap.put("Fort Kent","0708");
        cityMap.put("Fort MacKay","0709");
        cityMap.put("Fort McMurray","0116");
        cityMap.put("Fort Vermilion","0710");
        cityMap.put("Gainford","0711");
        cityMap.put("Gem","0712");
        cityMap.put("Gleichen","0126");
        cityMap.put("Glenevis","0714");
        cityMap.put("Goodfare","0976");
        cityMap.put("Goose Lake","0715");
        cityMap.put("Grassland","0716");
        cityMap.put("Grassy Lake","0136");
        cityMap.put("Green Court","0717");
        cityMap.put("Greenshields","0718");
        cityMap.put("Gregoire Lake Estates","1401");
        cityMap.put("Grouard","0719");
        cityMap.put("Grovedale","0979");
        cityMap.put("Gunn","0720");
        cityMap.put("Guy","0721");
        cityMap.put("Gwynne","0722");
        cityMap.put("Hairy Hill","0139");
        cityMap.put("Half Moon Lake","0723");
        cityMap.put("Harvie Heights","0724");
        cityMap.put("Hastings Lake","0949");
        cityMap.put("Haynes","0981");
        cityMap.put("Hays","0725");
        cityMap.put("Hayter","0726");
        cityMap.put("Heinsburg","0727");
        cityMap.put("Heritage Pointe","0971");
        cityMap.put("Herronton","1051");
        cityMap.put("Hesketh","1343");
        cityMap.put("Hilda","0728");
        cityMap.put("Hilliard","0729");
        cityMap.put("Hoadley","0984");
        cityMap.put("Hobbema","0730");
        cityMap.put("Huallen","1261");
        cityMap.put("Huxley","0731");
        cityMap.put("Hylo","0732");
        cityMap.put("Iddesleigh","0733");
        cityMap.put("Indus","0734");
        cityMap.put("Iron Springs","0735");
        cityMap.put("Irvine","0184");
        cityMap.put("Islay","0736");
        cityMap.put("Janet","0990");
        cityMap.put("Janvier South","0953");
        cityMap.put("Jarvie","0737");
        cityMap.put("Jean Cote","0738");
        cityMap.put("Jenner","0739");
        cityMap.put("Joffre","0740");
        cityMap.put("Johnson's Addition","0999");
        cityMap.put("Josephburg","0741");
        cityMap.put("Joussard","0742");
        cityMap.put("Kathyrn","0743");
        cityMap.put("Kavanagh","0744");
        cityMap.put("Keephills","0745");
        cityMap.put("Kelsey","0961");
        cityMap.put("Keoma","0747");
        cityMap.put("Kimball","0965");
        cityMap.put("Kingman","0749");
        cityMap.put("Kinsella","0750");
        cityMap.put("Kinuso","0189");
        cityMap.put("Kirkcaldy","1052");
        cityMap.put("Kirriemuir","0751");
        cityMap.put("La Corey","0752");
        cityMap.put("La Crete","0753");
        cityMap.put("La Glace","0754");
        cityMap.put("Lac Des Arcs","0755");
        cityMap.put("Lac La Biche","0192");
        cityMap.put("Lafond","0756");
        cityMap.put("Lake Louise","0757");
        cityMap.put("Lake Newell Resort","4451");
        cityMap.put("Lamoureux","0995");
        cityMap.put("Landry Heights","0980");
        cityMap.put("Langdon","0758");
        cityMap.put("Lavoy","0199");
        cityMap.put("Leavitt","0966");
        cityMap.put("Leedale","0985");
        cityMap.put("Leslieville","0760");
        cityMap.put("Lindbergh","0761");
        cityMap.put("Linn Valley","0986");
        cityMap.put("Little Buffalo","0762");
        cityMap.put("Little Smoky","0763");
        cityMap.put("Lodgepole","0764");
        cityMap.put("Long Lake","0765");
        cityMap.put("Looma","0766");
        cityMap.put("Lottie Lake","0991");
        cityMap.put("Lousana","0768");
        cityMap.put("Lowland Heights","0769");
        cityMap.put("Lundbreck","0770");
        cityMap.put("Lyalta","0771");
        cityMap.put("MacKay","0772");
        cityMap.put("Madden","0773");
        cityMap.put("Mallaig","0774");
        cityMap.put("Manola","0775");
        cityMap.put("Manyberries","0776");
        cityMap.put("Marie Reine","0777");
        cityMap.put("Markerville","0778");
        cityMap.put("Marlboro","0779");
        cityMap.put("Marten Beach","0780");
        cityMap.put("McLaughlin","0781");
        cityMap.put("Meanook","0782");
        cityMap.put("Mearns","0996");
        cityMap.put("Meeting Creek","0783");
        cityMap.put("Metiskow","0784");
        cityMap.put("Michichi","0785");
        cityMap.put("Millarville","0786");
        cityMap.put("Mirror","0223");
        cityMap.put("Monarch","0787");
        cityMap.put("Monitor","0788");
        cityMap.put("Moon River Estates","1059");
        cityMap.put("Morecambe","1201");
        cityMap.put("Morningside","0789");
        cityMap.put("Mossleigh","0790");
        cityMap.put("Mountain View","0791");
        cityMap.put("Mulhurst Bay","0792");
        cityMap.put("Musidora","0793");
        cityMap.put("Namaka","0795");
        cityMap.put("Namao","0997");
        cityMap.put("Neerlandia","0796");
        cityMap.put("Nestow","1056");
        cityMap.put("Nevis","0797");
        cityMap.put("New Brigden","0798");
        cityMap.put("New Dayton","0799");
        cityMap.put("New Norway","0233");
        cityMap.put("New Sarepta","0234");
        cityMap.put("Newbrook","0888");
        cityMap.put("Nightingale","1161");
        cityMap.put("Nisku","0889");
        cityMap.put("Niton Junction","0890");
        cityMap.put("Nordegg","0969");
        cityMap.put("North Cooking Lake","0891");
        cityMap.put("North Star","0892");
        cityMap.put("Notikewin","0893");
        cityMap.put("Ohaton","0894");
        cityMap.put("Opal","0895");
        cityMap.put("Orion","0974");
        cityMap.put("Orton","1060");
        cityMap.put("Parkland","1061");
        cityMap.put("Patricia","0897");
        cityMap.put("Peers","0801");
        cityMap.put("Pelican Point","0962");
        cityMap.put("Peoria","0955");
        cityMap.put("Perryvale","0954");
        cityMap.put("Pibroch","0802");
        cityMap.put("Pickardville","0803");
        cityMap.put("Pincher Station","0804");
        cityMap.put("Pine Sands","0998");
        cityMap.put("Pinedale","0805");
        cityMap.put("Plamondon","0252");
        cityMap.put("Poplar Ridge","0958");
        cityMap.put("Priddis","0972");
        cityMap.put("Priddis Greens","0973");
        cityMap.put("Purple Springs","0807");
        cityMap.put("Queenstown","0808");
        cityMap.put("Radway","0259");
        cityMap.put("Rainier","0809");
        cityMap.put("Ranfurly","0811");
        cityMap.put("Red Earth Creek","0812");
        cityMap.put("Red Willow","0813");
        cityMap.put("Reno","0814");
        cityMap.put("Ribstone","1055");
        cityMap.put("Rich Valley","0815");
        cityMap.put("Richdale","0816");
        cityMap.put("Ridgevalley","0817");
        cityMap.put("Rivercourse","0818");
        cityMap.put("Riverview","0819");
        cityMap.put("Riviere Qui Barre","0820");
        cityMap.put("Robb","0821");
        cityMap.put("Rochester","0822");
        cityMap.put("Rochfort Bridge","0823");
        cityMap.put("Rocky Rapids","0824");
        cityMap.put("Rolling Hills","0825");
        cityMap.put("Rolly View","0826");
        cityMap.put("Rosebud","0827");
        cityMap.put("Round Hill","0829");
        cityMap.put("Rowley","0992");
        cityMap.put("Rumsey","0274");
        cityMap.put("Sandy Lake","0830");
        cityMap.put("Sangudo","0278");
        cityMap.put("Saprae Creek","1381");
        cityMap.put("Scandia","0831");
        cityMap.put("Schuler","0832");
        cityMap.put("Sedalia","0833");
        cityMap.put("Seven Persons","0835");
        cityMap.put("Shaughnessy","0836");
        cityMap.put("Sherwood Park","0523");
        cityMap.put("Shouldice","1053");
        cityMap.put("Sibbald","0838");
        cityMap.put("Skiff","0975");
        cityMap.put("Smith","0839");
        cityMap.put("South Cooking Lake","0840");
        cityMap.put("Spedden","0841");
        cityMap.put("Spring Coulee","0842");
        cityMap.put("Springbrook","0900");
        cityMap.put("Spruce View","0843");
        cityMap.put("St. Edouard","0844");
        cityMap.put("St. Isidore","0845");
        cityMap.put("St. Lina","0846");
        cityMap.put("St. Michael","0847");
        cityMap.put("St. Vincent","0848");
        cityMap.put("Star","0850");
        cityMap.put("Streamstown","0851");
        cityMap.put("Suffield","0852");
        cityMap.put("Sunnybrook","0853");
        cityMap.put("Sunnynook","0854");
        cityMap.put("Sunnyslope","1341");
        cityMap.put("Swalwell","0855");
        cityMap.put("Tangent","0856");
        cityMap.put("Tawatinaw","1057");
        cityMap.put("Teepee Creek","0977");
        cityMap.put("Tees","0857");
        cityMap.put("Telfordville","0858");
        cityMap.put("Therien","0859");
        cityMap.put("Thorhild","0313");
        cityMap.put("Thunder Lake","0860");
        cityMap.put("Tillicum Beach","0963");
        cityMap.put("Tomahawk","0861");
        cityMap.put("Torrington","0319");
        cityMap.put("Travers","1054");
        cityMap.put("Tulliby Lake","0863");
        cityMap.put("Turin","0864");
        cityMap.put("Twin Butte","1361");
        cityMap.put("Valhalla Centre","0865");
        cityMap.put("Veinerville","0866");
        cityMap.put("Venice","1141");
        cityMap.put("Village at Pigeon Lake","1058");
        cityMap.put("Villeneuve","0867");
        cityMap.put("Vimy","0868");
        cityMap.put("Violet Grove","0957");
        cityMap.put("Wabasca","0869");
        cityMap.put("Wagner","0649");
        cityMap.put("Walsh","0870");
        cityMap.put("Wandering River","0871");
        cityMap.put("Wanham","0337");
        cityMap.put("Wardlow","0872");
        cityMap.put("Warspite","0341");
        cityMap.put("Waterton Park","0874");
        cityMap.put("Watino","0875");
        cityMap.put("Wedgewood","0978");
        cityMap.put("Welling","0877");
        cityMap.put("Welling Station","0967");
        cityMap.put("Westerose","0878");
        cityMap.put("Whitelaw","0880");
        cityMap.put("Whitford","1321");
        cityMap.put("Widewater","0899");
        cityMap.put("Wildwood","0351");
        cityMap.put("Wimborne","0881");
        cityMap.put("Winfield","0882");
        cityMap.put("Withrow","0970");
        cityMap.put("Woking","0883");
        cityMap.put("Woodhouse","1062");
        cityMap.put("Woolford","0968");
        cityMap.put("Worsley","0884");
        cityMap.put("Wostok","0885");
        cityMap.put("Wrentham","0886");
        cityMap.put("Zama City","0887");
        // Counties and M.D.s
        // Officially Municiple districts do take a 'M.D. of' but that interferes
        // with Address2's operation making it too agressive at trimming words.
        // The worst case is a street name will have 'M.D. of' tacked on.
        cityMap.put("Crowsnest Pass", "0361");
        cityMap.put("Jasper", "0418");
        cityMap.put("Mackenzie County", "0505");
        cityMap.put("Strathcona County", "0302");
        cityMap.put("Wood Buffalo", "0508");
        cityMap.put("Acadia", "0001");
        cityMap.put("Athabasca County", "0012");
        cityMap.put("County of Barrhead", "0015");
        cityMap.put("Beaver County", "0020");
        cityMap.put("Big Lakes", "0506");
        cityMap.put("Bighorn", "0382");
        cityMap.put("Birch Hills County", "0502"); 
        cityMap.put("Brazeau County", "0383");
        cityMap.put("Camrose County", "0049");
        cityMap.put("Cardston County", "0053");
        cityMap.put("Clear Hills County", "0504");
        cityMap.put("Clearwater County", "0377");
        cityMap.put("Cypress County", "0376");
        cityMap.put("Flagstaff County", "0110");
        cityMap.put("Foothills", "0111");
        cityMap.put("County of Forty Mile", "0118");
        cityMap.put("County of Grande Prairie", "0133");
        cityMap.put("Greenview", "0481");
        cityMap.put("Kneehill County", "0191");
        cityMap.put("Lac La Biche County", "4353");
        cityMap.put("Lac Ste. Anne County", "0193");
        cityMap.put("Lacombe County", "0195");
        cityMap.put("Lamont County", "0198");
        cityMap.put("Leduc County", "0201");
        cityMap.put("Lesser Slave River", "0507");
        cityMap.put("County of Lethbridge", "0204");
        cityMap.put("County of Minburn", "0222");
        cityMap.put("Mountain View County", "0226");
        cityMap.put("County of Newell", "0235");
        cityMap.put("County of Northern Lights", "0511");
        cityMap.put("Northern Sunrise County", "0496");
        cityMap.put("Opportunity", "0512");
        cityMap.put("Paintearth", "0243");
        cityMap.put("Parkland County", "0245");
        cityMap.put("Peace", "0246");
        cityMap.put("Ponoka County", "0255");
        cityMap.put("Ranchland", "0501");
        cityMap.put("Red Deer County", "0263");
        cityMap.put("Rocky View County", "0269");
        cityMap.put("Saddle Hills County", "0503");
        cityMap.put("Smoky Lake County", "0286");
        cityMap.put("Smoky River", "0287");
        cityMap.put("County of St. Paul", "0294");
        cityMap.put("Starland County", "0296");
        cityMap.put("County of Stettler", "0299");
        cityMap.put("Sturgeon County", "0305");
        cityMap.put("County of Thorhild", "0314");
        cityMap.put("County of Two Hills", "0323");
        cityMap.put("County of Vermilion River", "0329");
        cityMap.put("Vulcan County", "0334");
        cityMap.put("County of Warner", "0340");
        cityMap.put("Westlock County", "0346");
        cityMap.put("County of Wetaskiwin", "0348");
        cityMap.put("Wheatland County", "0349");
        cityMap.put("Willow Creek", "0353");
        cityMap.put("Woodlands County", "0480");
        cityMap.put("Yellowhead County", "0482");
        // Reservation for Chinook Arch.
        cityMap.put("Standoff", "6000");
        // Additional names that are not recognized by Alberta Gov. but are mailing addresses.
        cityMap.put("Blairmore", "6010");
        cityMap.put("Bellevue", "6020");
        cityMap.put("Coleman", "6030");
        cityMap.put("Crowsnest Pass", "6040");
        cityMap.put("Darwell", "6050");
        cityMap.put("Dewinton", "6060");
        cityMap.put("Fenn", "6070");
        cityMap.put("Frank", "6080");
        cityMap.put("Hillcrest", "6090");
        cityMap.put("James River Bridge", "6100");
        cityMap.put("Pine Lake", "6110");
        cityMap.put("Rosedale Valley", "6120");
        cityMap.put("Stauffer", "6130");
        cityMap.put("Water Valley", "6140");
        
        boolean hasDisplayedMessage = false;
        // Now we overlay place name records with config requested codes for BImport.
        Properties properties = PropertyReader.getProperties(ConfigFileTypes.BIMPORT_CITY_MAPPING);
        for(String customPlaceName : properties.stringPropertyNames())
        {
            // Here we will check what the operator has put in the city_st table
            // properties as names of places. We need to match more loosly since 
            // it is so difficult to unify spelling with case consideratons.
            // This next piece of code will check if this place is an official 
            // place name of Alberta and if so will normalize it with the desired
            // place-name code.
            String placeNameTesting = this.getPlaceNameLike(customPlaceName);
            if (placeNameTesting.length() == 0)
            {
                // The keys in the property files are stored in lower case. We can insist
                // but just in case let's initial cap the city names there. To get
                // here we didn't match on an official place name, but we will add
                // it anyway, so clean up the given name to insert as a key.
                placeNameTesting = Text.toDisplayCase(customPlaceName);
            }
            // Now get the property file's associated code for the given place name
            // (real place name or otherwise).
            String preferedCode = properties.getProperty(customPlaceName);
            if (cityMap.containsKey(placeNameTesting) == false)
            {
                if (hasDisplayedMessage == false)
                {
                    System.out.println(new Date() +
                            " The following not recognized as official "
                            + "Alberta place name(s):\n"
                            + "'" + customPlaceName + "::" + preferedCode + "'");
                    hasDisplayedMessage = true;
                }
                else
                {
                    System.out.println("'" + customPlaceName + "::" + preferedCode + "'");
                }
            }
            cityMap.put(placeNameTesting, preferedCode);
        }
    }
}
