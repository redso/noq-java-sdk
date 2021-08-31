package hk.noq.roomq;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Utils {

  static String removeNoQToken(String url) {
    return url
            .replaceAll("([&]*)(noq_t=[^&]*)", "")
            .replaceAll("\\?&", "?")
            .replaceAll("\\?$", "");
  }

  static Map<String, Object> sendHTTPRequest(String method, String url, Map<String, Object> data) throws HTTPRequestException {
    try {
      URL obj = new URL(url);

      HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      con.setRequestProperty("Accept", "application/json");
      con.setRequestProperty("User-Agent", "RoomQ Java SDK");

      if ("POST".equals(method)) {
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestMethod(method);
        con.setDoOutput(true);
        JSONObject jo = new JSONObject(data);
        try (OutputStream os = con.getOutputStream()) {
          byte[] input = jo.toString().getBytes(StandardCharsets.UTF_8);
          os.write(input, 0, input.length);
        }
      }

      int responseCode = con.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
        in.close();
        return new JSONObject(response.toString()).toMap();
      } else {
        System.out.println("POST request not worked");
        throw new HTTPRequestException(responseCode);
      }
    } catch (MalformedURLException | ProtocolException e) {
      System.out.println(e.toString());
    } catch (IOException e) {
      System.out.println(e.toString());
    }
    return null;
  }
}
