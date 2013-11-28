package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Dam Linh <damhonglinh@gmail.com>
 */
public class UploadScores implements Runnable {

    private Score score;
    private final String uri = "https://api.parse.com/1/classes/HighScore";
    private final String APP_ID = "JfAKj8y4FKoZy5xFiiWchIhKfvudV96eaXoMKlXW";
    private final String CLIENT_KEY = "xb4HjKgcDo5dD1guipohTt723mLkTgihnXLNzikK";

    public UploadScores(Score score) {
        this.score = score;
    }

    @Override
    public void run() {
        HttpURLConnection connect = null;
        try {
            String charSet = "UTF-8";
            String data = "{\"name\":\"" + score.getName() + "\",\"score\":" + score.getScore() + "}";

            URL url = new URL(uri);
            connect = (HttpURLConnection) url.openConnection();
            connect.setRequestMethod("POST");

            connect.setDoOutput(true);

            connect.addRequestProperty("X-Parse-Application-Id", APP_ID);
            connect.addRequestProperty("X-Parse-REST-API-Key", CLIENT_KEY);
            connect.addRequestProperty("Content-Type", "application/json");
            connect.setReadTimeout(60000);
            connect.setConnectTimeout(60000);

            try (OutputStream output = connect.getOutputStream()) {
                output.write(data.getBytes(charSet));
                output.flush();
            } catch (Exception ex) {
                System.out.println("Exception caught");
                ex.printStackTrace();
            }

            int statusCode = connect.getResponseCode();
            switch (statusCode) {
                case HttpURLConnection.HTTP_OK:
                case 201:// created successfully
                    break;
                default:
                    throw new Exception("Invalid Response Code: " + statusCode);
            }


            BufferedReader br = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
//            System.out.println(sb);
        } catch (Exception e) {
            System.out.println("Exception caught");
            e.printStackTrace();
        } finally {
            if (connect != null) {
                connect.disconnect();
            }
        }
    }
}