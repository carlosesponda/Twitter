import java.io.*;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.util.Base64;
import java.util.List;
import java.util.ArrayList;

public class Tweet 
{
	private static String bearerToken = "";
	
	public static void GetOAuthToken()
	{
		String inputStr = "";
		
		try
		{
			//encode the credentials
			String creds = "6tf4ukmX7Azax5MwAFAYf0H7s:77HkGzHOcI0I5RNbl7KXPk0C5gnv2qKhRYhv7E4jDi5EoSYMPQ";
			creds = Base64.getEncoder().encodeToString(creds.getBytes());
			
			//set up web request
			URL url = new URL("https://api.twitter.com/oauth2/token");
			HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization", "Basic " + creds);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		
			String urlParameters = "grant_type=client_credentials";
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuffer response = new StringBuffer();
			
			//read the json into inputStr
			while ((inputStr = in.readLine()) != null)
			{
				response.append(inputStr);
			}
			
			inputStr = response.toString();
			
			in.close();
		}
		
		catch(Exception e)
		{
			System.out.println("Web request failed.");
		}
		
		//extract token
		inputStr = inputStr.substring(inputStr.indexOf("access_token") + 15);
		bearerToken = inputStr.substring(0, inputStr.indexOf('"'));
	}
	
	private static List<String> GetTrendingTags(String woeid)
    {
        String inputStr = "";
       
        try
        {
        	//web request with city id
            URL url = new URL("https://api.twitter.com/1.1/trends/place.json?id=" + woeid);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + bearerToken);
           
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer response = new StringBuffer();
 
            //read json into inputStr
            while ((inputStr = in.readLine()) != null)
            {
                response.append(inputStr);
            }
            inputStr = response.toString();
           
            in.close();
        }
        
        catch(Exception e)
        {
            System.out.println("Web request failed.");
        }
       
        //extract the trending tags of the JSON and put it back into inputStr
        List<String> trendingTags = new ArrayList<String>();
        int index = 0;
        while(index != -1)
        {
            index = inputStr.indexOf("name");
            if(index != -1)
            {
                inputStr = inputStr.substring(index + 7);
                trendingTags.add(inputStr.substring(0, inputStr.indexOf('"')));
            }
        }
       
        return trendingTags;
    }
	
    public static String GetTrendingTagsStr(String woeid)
    {
    	//gets the trending tags
        List<String> list = GetTrendingTags(woeid);
        String ans = "";
        
        // returns a comma separated string
        for(int i = 0; i < list.size(); i++)
        {
            ans += list.get(i) + ", ";
        }
       
        return ans;
    }
}
