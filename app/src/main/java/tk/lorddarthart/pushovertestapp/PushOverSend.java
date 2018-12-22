package tk.lorddarthart.pushovertestapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;

public class PushOverSend {

    public int sendPush(String token, String user, String device, String title, String text) throws IOException, JSONException, ParseException {
        String url = "https://api.pushover.net/1/messages.json";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("content-type", "application/json; charset=UTF-8");
        JSONObject object = new JSONObject();
        object.put("token", token);
        object.put("user", user);
        object.put("device", device);
        object.put("title", title);
        object.put("message", text);
        try{
            URL tryParse = new URL(text);
            object.put("url", tryParse);
        } catch (IOException e) {

        }
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);

        OutputStream os = con.getOutputStream();
        os.write( object.toString().getBytes("UTF-8") );
        os.close();

        int responseCode = con.getResponseCode();
        return responseCode;
    }

    public int sendPush(String token, String user, String title, String text) throws IOException, JSONException, ParseException {
        String url = "https://api.pushover.net/1/messages.json";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("content-type", "application/json; charset=UTF-8");
        JSONObject object = new JSONObject();
        object.put("token", token);
        object.put("user", user);
        object.put("title", title);
        object.put("message", text);
        try{
            URL tryParse = new URL(text);
            object.put("url", tryParse);
        } catch (IOException e) {

        }
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);

        OutputStream os = con.getOutputStream();
        os.write( object.toString().getBytes("UTF-8") );
        os.close();

        int responseCode = con.getResponseCode();
        return responseCode;
    }

}
