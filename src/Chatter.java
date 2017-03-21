import java.io.*;
import java.net.*;

public class Chatter 
{
   public static void main(String[] args) throws Exception
   {
        Tweet.GetOAuthToken();
        
        // The server to connect to and our details.
        String server = "irc.freenode.net";
        String nick = "Cflash141";
        String login = "Cflash141";

        // The channel which the bot will join.
        String channel = "#irchacks";
        
        // Connect directly to the IRC server.
        Socket socket = new Socket(server, 6667);
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream( )));
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream( )));
        
        // Log on to the server.
        writer.write("NICK " + nick + "\r\n");
        writer.write("USER " + login + " 8 * : Java IRC Hacks Bot\r\n");
        writer.flush( );
        
        // Read lines from the server until it tells us we have connected.
        String line = null;
        while ((line = reader.readLine( )) != null) {
            if (line.indexOf("004") >= 0) {
                // We are now logged in.
                break;
            }
            else if (line.indexOf("433") >= 0) {
                System.out.println("Nickname is already in use.");
                return;
            }
        }
        
        // Join the channel.
        writer.write("JOIN " + channel + "\r\n");
        writer.flush( );
        
        // Keep reading lines from the server.
        while ((line = reader.readLine( )) != null) {
            if (line.toLowerCase( ).startsWith("PING ")) {
                // We must respond to PINGs to avoid being disconnected.
                writer.write("PONG " + line.substring(5) + "\r\n");
                writer.write("PRIVMSG " + channel + " :I got pinged!\r\n");
                writer.flush( );
            }
            else {
            	// Print the raw line received by the bot.
                System.out.println(line);
                
                //if the keyword weather is found, check additional following cases
                if(line.indexOf("weather") != -1)
                {
	                 // Look for zip codes of length 5 and try to parse as int
	                 
	                 String[] words = line.split(" ");
	                 for(int i = 0; i < words.length; i++)
	                 {
	                	  //if zip code is found then display the weather information
		                  if(words[i].length() == 5 && tryParseInt(words[i]))
		                  {
		                	  int currentTemp = Temperature.getWeather(words[i]);
		                      Structure highLow = Temperature.getForecast(words[i]);
		                         
		                      writer.write("PRIVMSG " + channel + " :The weather is " + currentTemp + " with a high of " + highLow.tempHigh + " and a low of " + highLow.tempLow + "\r\n");
		                      writer.flush();
		                  }
        
	                 }
                }
                
                //dallas woeid is 2388929
                else if(line.indexOf("trending") != -1)
                {
                	// Look for strings that can be parsed as a number
                	String[] words = line.split(" ");
                	for(int i = 0; i < words.length; i++)
                	{
                		if(tryParseInt(words[i]))
                		{
                			String trendingTags = Tweet.GetTrendingTagsStr(words[i]);
                        
                			writer.write("PRIVMSG " + channel + " :" + trendingTags + "\r\n");
                			writer.flush();
                		}
                	}
                }
            }
        }
   }
   
   //find the zip code from the line entered
   public static boolean tryParseInt(String value) 
   {  
        try 
        {  
         Integer.parseInt(value);  
            return true;  
        } 
        catch (NumberFormatException e) 
        {  
         return false;  
        }  
   }
}
