package com.fedem96.requester;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class FileRequester extends Requester{

    long downloadedBytes;
    String file;

    public FileRequester(String filePath) {
        downloadedBytes = 0;
        this.file = filePath;
    }

    @Override
    public String sendRequest(Map<String, String> parameters) throws IOException {
        int maxRows = Integer.parseInt(parameters.getOrDefault("rows", "-1"));
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int rowIndex = 0;
            String line;
            while ((line = br.readLine()) != null) {
                if(rowIndex++ == maxRows)
                    break;
                sb.append(line).append("\n");
            }
        }
        String contentString =  sb.toString();
        downloadedBytes += contentString.length()*2;
        System.out.println("downloaded " + String.format("%.3f", contentString.length()*2.0/1024/1024) + " MiB" +
                " (total: " + String.format("%.3f", downloadedBytes*1.0/1024/1024) + " MiB)");
        return contentString;
    }
}
