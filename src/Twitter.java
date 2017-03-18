import java.io.*;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.util.Base64;
import java.util.List;
import java.util.ArrayList;

public class Twitter
{
    public static String bearerToken = "";

    public static void GetOAuthToken()
    {
        try
        {
            // Encode the credentials
            String creds = "N1rtiIOTa351434zZkQIPCdTa:Kkrrnljc1tA2VprKIveCm2EMcGBTE2q9IoEF32Y84gIBZ1Xzav";
            creds = Base64.getEncoder().encodeToString(creds.getBytes());

            // Setup the web request
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

            // Read the entire json response into inputStr
            String inputStr = "";
            while ((inputStr = in.readLine()) != null)
            {
                response.append(inputStr);
            }
            inputStr = response.toString();
            in.close();

            // Move to access_token in the json
            inputStr = inputStr.substring(inputStr.indexOf("access_token") + 15);

            // Prase access_token
            bearerToken = inputStr.substring(0, inputStr.indexOf('"'));
        }
        catch(Exception e)
        {
            System.out.println("Twitter web request failed. Could not get token.");
        }
    }

    // Returns list of trending tag strings
    public static List<String> GetTrendingTags(String woeid)
    {
        List<String> trendingTags = new ArrayList<String>();

        try
        {
            // Setup the web request
            URL url = new URL("https://api.twitter.com/1.1/trends/place.json?id=" + woeid);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + bearerToken);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer response = new StringBuffer();

            // Read the entire json response int inputStr
            String inputStr = "";
            while ((inputStr = in.readLine()) != null)
            {
                response.append(inputStr);
            }
            inputStr = response.toString();
            in.close();

            // Read each trending tag into the List of trending tags
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
        }
        catch(Exception e)
        {
            System.out.println("Twitter web request failed. Could not get trending tags.");
        }

        return trendingTags;
    }

    // Returns comma seperated String of trending tags
    public static String GetTrendingTagsStr(String woeid)
    {
        List<String> list = GetTrendingTags(woeid);
        String ans = "";
        for(int i = 0; i < list.size(); i++)
        {
            ans += list.get(i) + ", ";
        }

        return ans;
    }
}
