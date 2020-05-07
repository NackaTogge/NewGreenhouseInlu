package Clients;

import Models.Phvalues;
import Models.Response;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class Client {
    private static Phvalues getLatestValues(String selected){
        final String uri = "http://localhost:8080/db/getlatestvalues";
        RestTemplate restTemplate = new RestTemplate();
        Phvalues res = restTemplate.getForObject(uri, Phvalues.class);

        switch (selected) {
            case "A":
                System.out.println("Time: " + res.getTime() + " , Hum: " + res.getHum() + " % , Temp: " + res.getTemp() +
                        " C , Lum: " + res.getLum() + " % , Power consumption per day: " + res.getKwhPDay() + " kWh");
                break;
            case "H":
                System.out.println("Time: " + res.getTime() + " , Hum: " + res.getHum() + " %");
                break;
            case "T":
                System.out.println("Time: " + res.getTime() + " , Temp: " + res.getTemp() + " C");
                break;
            case "L":
                System.out.println("Time: " + res.getTime() + " , Lum: " + res.getLum() + " %");
                break;
            case "P":
                System.out.println("Time: " + res.getTime() + " , Power consumption per day: " + res.getKwhPDay() + " kWh");
                break;
            case "0":
                break;
            default:
                System.out.println("Unforseen Error. Exiting.");
                System.exit(1);
        }
        return res;
    }

    private static void CalcPowerCost(float setPC){
        float powerCost = 7*setPC*GetLatestWeekValues("P");
        System.out.println("Power consumption cost latest week: SEK " + powerCost);
    }

    private static float GetLatestWeekValues (String selected){
        final String uri = "http://localhost:8080/db/getlatestweekvalues";
        RestTemplate restTemplate = new RestTemplate();
        Phvalues[] resArray = restTemplate.getForObject(uri, Phvalues[].class);
        List<Phvalues> result = Arrays.asList(resArray);

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp oneWeekAgo = new Timestamp(now.getTime() - 7 * 24 * 60 * 60 * 1000 );
        long diff;
        float avgKwhPDay = 0f;

        switch (selected) {
            case "H":
                float timeHum = 0;
                for (int i = 0; i < result.size(); i++){
                    if (i == 1) {
                        diff = result.get(i).getTime().getTime() - oneWeekAgo.getTime();
                        timeHum = timeHum + result.get(i-1).getHum() * diff;
                    }
                    else if (i > 1) {
                        diff = result.get(i).getTime().getTime() - result.get(i-1).getTime().getTime();
                        timeHum = timeHum + result.get(i-1).getHum() * diff;
                    }
                    System.out.println("Time: " + result.get(i).getTime() + " , Humidity: " + result.get(i).getHum() +
                            "% , Källa/Typ: " + result.get(i).getRecordType());
                }
                diff = now.getTime() - result.get(result.size() - 1).getTime().getTime();
                timeHum = timeHum + diff * result.get(result.size() - 1).getHum();
                float avgHum = timeHum/(7 * 24 * 60 * 60 * 1000);
                System.out.println("Average Humidity latest week: " + avgHum + " %");
                break;
            case "T":
                float timeTemp = 0;
                for (int i = 0; i < result.size(); i++){
                    if (i == 1) {
                        diff = result.get(i).getTime().getTime() - oneWeekAgo.getTime();
                        timeTemp = timeTemp + result.get(i-1).getTemp() * diff;
                    }
                    else if (i > 1) {
                        diff = result.get(i).getTime().getTime() - result.get(i-1).getTime().getTime();
                        timeTemp = timeTemp + result.get(i-1).getTemp() * diff;
                    }
                    System.out.println("Time: " + result.get(i).getTime() + " , Temperature: " + result.get(i).getTemp() +
                            "C , Källa/Typ: " + result.get(i).getRecordType());
                }
                diff = now.getTime() - result.get(result.size() - 1).getTime().getTime();
                timeTemp = timeTemp + diff * result.get(result.size() - 1).getTemp();
                float avgTemp = timeTemp/(7 * 24 * 60 * 60 * 1000);
                System.out.println("Average Temperature latest week: " + avgTemp + " C");
                break;
            case "L":
                float timeLum = 0;
                for (int i = 0; i < result.size(); i++){
                    if (i == 1) {
                        diff = result.get(i).getTime().getTime() - oneWeekAgo.getTime();
                        timeLum = timeLum + result.get(i-1).getLum() * diff;
//                        System.out.println(timeLum/(90*1000*60*60)); // test print
                    }
                    else if (i > 1) {
                        diff = result.get(i).getTime().getTime() - result.get(i-1).getTime().getTime();
                        timeLum = timeLum + result.get(i-1).getLum() * diff;
//                        System.out.println(timeLum/(90*1000*60*60)); // test print
                    }
                    System.out.println("Time: " + result.get(i).getTime() + " , Lumen: " + result.get(i).getLum() +
                            "% , Källa/Typ: " + result.get(i).getRecordType());
                }
                diff = now.getTime() - result.get(result.size() - 1).getTime().getTime();
                timeLum = timeLum + diff * result.get(result.size() - 1).getLum();
//                System.out.println(timeLum/(90*1000*60*60)); // test print
                float avgLum = timeLum/(7 * 24 * 60 * 60 * 1000);
                System.out.println("Average Lumen latest week: " + avgLum + " %");
                break;
            case "P":
                float timeKwhPDay = 0;
                for (int i = 0; i < result.size(); i++){
                    if (i == 1) {
                        diff = result.get(i).getTime().getTime() - oneWeekAgo.getTime();
                        timeKwhPDay = timeKwhPDay + result.get(i-1).getKwhPDay() * diff;
                    }
                    else if (i > 1) {
                        diff = result.get(i).getTime().getTime() - result.get(i-1).getTime().getTime();
                        timeKwhPDay = timeKwhPDay + result.get(i-1).getKwhPDay() * diff;
                    }
//                    System.out.println("Time: " + result.get(i).getTime() + " , Power consumption per day: " + result.get(i).getKwhPDay() +
//                            "kWh per day , Källa/Typ: " + result.get(i).getRecordType());
                }
                diff = now.getTime() - result.get(result.size() - 1).getTime().getTime();
                timeKwhPDay = timeKwhPDay + diff * result.get(result.size() - 1).getKwhPDay();
                avgKwhPDay = timeKwhPDay/(7 * 24 * 60 * 60 * 1000);
                System.out.println("Average Power consumption per day latest week: " + avgKwhPDay + " kWh per day");
                break;
            default:
                System.out.println("Unforseen Error. Exiting.");
                System.exit(1);
        }
        return avgKwhPDay;
    }

    private static void CreatePhvalues(String measToSet, int setInt, float setFloat, String setString)
    {
        final String uri = "http://localhost:8080/db/phvalues/add";
        Phvalues newPhvalues = getLatestValues("0");

        switch (measToSet) {
            case "setH":
                newPhvalues.setHum(setInt);
                newPhvalues.setRecordType("setH");
                break;
            case "setT":
                newPhvalues.setTemp(setFloat);
                newPhvalues.setRecordType("setT");
                break;
            case "setL":
                newPhvalues.setLum(setInt);
                newPhvalues.setRecordType("setL");
                break;
            case "setA":
                String[] valueArray = setString.split(" ");
                newPhvalues.setHum(Integer.parseInt(valueArray[0]));
                newPhvalues.setTemp(Float.parseFloat(valueArray[1]));
                newPhvalues.setLum(Integer.parseInt(valueArray[2]));
                newPhvalues.setKwhPDay(Float.parseFloat(valueArray[3]));
                newPhvalues.setRecordType("corrAll");
                break;
            default:
                System.out.println("Unforseen Error. Exiting.");
                System.exit(1);
        }
        RestTemplate restTemplate = new RestTemplate();
        Response result = restTemplate.postForObject( uri, newPhvalues, Response.class);
        System.out.println(result.getMessage() + " " + result.getStatus());
    }

    public static void main(String[] args) throws IOException {
        while (true) {
            System.out.println("*** Menu ***: \n" +
                    ">> 1. Joint Current report: View Latest Humidity, Temperature, Lumen and Power consumption values. \n" +
                    ">> 2. View latest Humidity measurement.  " +
                    ">> 3. View latest Temperature measurement.  " +
                    ">> 4. View latest Lumen measurement.  " +
                    ">> 5. View latest daily Power consumption measurement. \n" +
                    ">> 6. Set Humidity.  " +
                    ">> 7. Set Temperature.  " +
                    ">> 8. Set Lumen \n" +
                    ">> 9. View all Humidities during the latest week  " +
                    ">> 10. View all Temperatures during the latest week  " +
                    ">> 11. View all Lumen during the latest week \n" +
                    ">> 12. Calculate Power cost for latest week.  " +
                    ">> 13. Manually correct all measurements.");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String val = reader.readLine();

            switch (val) {
                case "1":
                    System.out.println("1 Joint report of values selected. ");
                    getLatestValues("A");
                    break;
                case "2":
                    System.out.println("2 View latest Humidity measurement selected.");
                    getLatestValues("H");
                    break;
                case "3":
                    System.out.println("3 View latest Temperature measurement selected.");
                    getLatestValues("T");
                    break;
                case "4":
                    System.out.println("4 View latest Lumen measurement selected.");
                    getLatestValues("L");
                    break;
                case "5":
                    System.out.println("5 View latest Power consumption measurement selected.");
                    getLatestValues("P");
                    break;
                case "6":
                    System.out.println("6. Set Humidity selected. ");
                    System.out.println("Please enter new Humidity (%): ");
                    val = reader.readLine();
                    int setH = Integer.parseInt(val);
                    CreatePhvalues("setH", setH, 0f, "");
                    break;
                case "7":
                    System.out.println("7. Set Temperature selected. ");
                    System.out.println("Please enter new Temperature (C): ");
                    val = reader.readLine();
                    Float setT = Float.parseFloat(val);
                    CreatePhvalues("setT", 0, setT, "");
                    break;
                case "8":
                    System.out.println("8. Set Lumen selected. ");
                    System.out.println("Please enter new Lumen (%): ");
                    val = reader.readLine();
                    int setL = Integer.parseInt(val);
                    CreatePhvalues("setL", setL, 0f, "");
                    break;
                case "9":
                    System.out.println("9. View Humidities during latest week selected.");
                    GetLatestWeekValues("H");
                    break;
                case "10":
                    System.out.println("10. View Temperatures during latest week selected.");
                    GetLatestWeekValues("T");
                    break;
                case "11":
                    System.out.println("11. View Lumen during latest week selected.");
                    GetLatestWeekValues("L");
                    break;
                case "12":
                    System.out.println("12. Calculate Power cost for latest week selected. ");
                    System.out.println("Please enter avg Power cost per kWh during the latest week (SEK): ");
                    val = reader.readLine();
                    float setPC = Float.parseFloat(val);
                    CalcPowerCost(setPC);
                    break;
                case "13":
                    System.out.println("13. Manually correct all measurements selected. ");
                    System.out.println("Please enter new vaules for Humidity (%, integer), Temperature (C, float), Lumen (%, integer) " +
                            "Power consumption (kWh/day, float). \nOne line w values separated by spaces w/o letters or special chars.");
                    val = reader.readLine();
                    CreatePhvalues("setA", 0, 0f, val);
                    break;
                default:
                    System.out.println("Icke angivet alternativ/funktion vald. Stänger.");
                    System.exit(1);
            }
        }
    }
}