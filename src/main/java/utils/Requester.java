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
    long downloadedBytes;

    public Requester() {
        endpointURL = "https://www.agenziafarmaco.gov.it/services/search/select";
        method = "GET";
        downloadedBytes = 0;
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

        String contentString =  content.toString();
        downloadedBytes += contentString.length()*2;
        System.out.println("downloaded " + contentString.length()*2 + " bytes (total: " + downloadedBytes + " bytes)");
        return contentString;
    }

}
