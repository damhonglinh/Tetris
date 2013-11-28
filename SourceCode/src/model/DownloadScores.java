package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author Dam Linh <damhonglinh@gmail.com>
 */
public class DownloadScores implements Runnable {

    private ArrayList<Score> bestScores;
    private Model model;
    private final String uri = "https://api.parse.com/1/classes/HighScore";
    private final String APP_ID = "JfAKj8y4FKoZy5xFiiWchIhKfvudV96eaXoMKlXW";
    private final String CLIENT_KEY = "xb4HjKgcDo5dD1guipohTt723mLkTgihnXLNzikK";

    public DownloadScores(Model model) {
        this.model = model;
    }

    @Override
    public void run() {
        bestScores = new ArrayList<>(10);
        HttpURLConnection connect = null;
        try {
            URL url = new URL(uri + "?order=-score&limit=10");
            connect = (HttpURLConnection) url.openConnection();
            connect.setRequestMethod("GET");

            connect.addRequestProperty("X-Parse-Application-Id", APP_ID);
            connect.addRequestProperty("X-Parse-REST-API-Key", CLIENT_KEY);
            connect.setReadTimeout(60000);
            connect.setConnectTimeout(60000);

            int statusCode = connect.getResponseCode();
            switch (statusCode) {
                case HttpURLConnection.HTTP_OK:
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
            System.out.println(sb);

            jsonToScore(sb.toString());
        } catch (Exception e) {
            System.out.println("Exception caught");
            e.printStackTrace();
            bestScores = null;
        } finally {
            if (connect != null) {
                connect.disconnect();
            }
        }

        model.setBestScores(bestScores);
    }

    private void jsonToScore(String jsonString) {
        jsonString = jsonString.substring(jsonString.indexOf('[') + 2, jsonString.indexOf(']') - 1);
        String[] jsonStringArray = jsonString.split("(\\},\\{)");

        for (int i = 0; i < jsonStringArray.length; i++) {
            String s = jsonStringArray[i];
            StringBuilder name = new StringBuilder();
            StringBuilder score = new StringBuilder();
            StringBuilder id = new StringBuilder();

            int countQuotes = 0;
            boolean isSkipFirstChar = false;//checks if the ':' is skipped when get score
            for (int j = 0; j < s.length(); j++) {
                char currentChar = s.charAt(j);
                if (currentChar == '\"') {
                    countQuotes++;
                    continue;
                }

                switch (countQuotes) {
                    case 3:
                        name.append(currentChar);
                        break;
                    case 6:
                        if (!isSkipFirstChar) {
                            isSkipFirstChar = true;
                        } else {
                            score.append(currentChar);
                        }
                        break;
                    case 17:
                        id.append(currentChar);
                        break;
                }
            }

            score = new StringBuilder(score.substring(0, score.length() - 1));

            Score sc = new Score(id.toString(), name.toString(), Long.parseLong(score.toString()));
            bestScores.add(sc);
        }
    }
}