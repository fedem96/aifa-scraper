package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;

public class Requester {
    // TODO: refactor

    String endpointURL;
    String method;

    public Requester() {
        endpointURL = "https://www.agenziafarmaco.gov.it/services/search/select";
        method = "GET";
    }

    public String sendRequest(Map<String, String> parameters) throws IOException {
        String params = "?" + parameters.entrySet().stream().map(pair -> pair.getKey() + "=" + pair.getValue() + "&").collect(Collectors.joining());
        System.out.println("params " + params);

        URL urlObj = new URL(endpointURL+params);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        con.setRequestMethod(method);

//        int status = con.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            content.append(inputLine).append("\n");

        in.close();
        con.disconnect();

        return content.toString();
    }

}
