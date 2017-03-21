import java.net.*;
import java.io.*;

public class Temperature
{
   //function to grab the temperature from a zip code
   public static int getWeather(String zipcode)
   {
        double temp;
        String inputLine = "";
        
        try
        {

        	//used a different opensource weather website
            URL urlID = new URL("http://api.wunderground.com/api/3466a88f38b75816/conditions/q/" + zipcode + ".json");
            URLConnection connection = urlID.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            
            //instead of using a library i just stored all the json info as a string
            while (in.ready()) 
            {
                inputLine += in.readLine() + "\n";
            }
            in.close();


        }
        
        //print out if web request fails
        catch(Exception e)
        {
            System.out.println("Web Request Failed");
        }
        
        //find the temperature in farenheiht and grab the number only
        inputLine = inputLine.substring(inputLine.indexOf("temp_f") + 8);
        temp = Double.parseDouble(inputLine.substring(0, inputLine.indexOf(',')));
        //rounds the temperature
        return (int)Math.round(temp);
    }
    
   public static Structure getForecast(String zipcode)
   {
        String inputLine = "";
        
        try
        {

            URL urlID = new URL("http://api.wunderground.com/api/3466a88f38b75816/forecast/q/" + zipcode + ".json");
            URLConnection connection = urlID.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            

            while (in.ready()) 
            {
                inputLine += in.readLine() + "\n";
            }
            in.close();


        }
        
        //print out if web request fails
        catch(Exception e)
        {
            System.out.println("Web Request Failed");
        }
        
        //find the first keyword high and display only the number
        inputLine = inputLine.substring(inputLine.indexOf("\"high\": {") + 26);
        int tempHigh = Integer.parseInt(inputLine.substring(0, inputLine.indexOf('"')));
        
        //find the first low after high and display only the number
        inputLine = inputLine.substring(inputLine.indexOf("low") + 24);
        int tempLow = Integer.parseInt(inputLine.substring(0, inputLine.indexOf('"')));
        
        return new Structure(tempHigh, tempLow);
   }   
   

}