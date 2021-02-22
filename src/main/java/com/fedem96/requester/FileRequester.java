package com.fedem96.requester;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
        int start = Integer.parseInt(parameters.getOrDefault("start", "0"));
        int maxRows = Integer.parseInt(parameters.getOrDefault("rows", "-1"));
        String format = parameters.getOrDefault("wt", "xml");
        String contentString;
        switch (format){
            case "csv":
                contentString = readCSV(start, maxRows);
                break;
            case "json":
                contentString = readCSV(start, maxRows);
                break;
            case "xml":
                contentString = readCSV(start, maxRows);
                break;
            default:
                throw new IllegalArgumentException("Invalid format: '" + format + "'. Possible values: {csv, json, xml}");
        }
        downloadedBytes += contentString.length()*2;
        System.out.println("downloaded " + String.format("%.3f", contentString.length()*2.0/1024/1024) + " MiB" +
                " (total: " + String.format("%.3f", downloadedBytes*1.0/1024/1024) + " MiB)");
        return contentString;
    }

    private String readCSV(int start, int maxRows) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int rowIndex = -2;  // rowIndex==-1 for header row; rowIndex==0 for first data row
            String line;
            while ((line = br.readLine()) != null) {
                rowIndex++;
                if(rowIndex >= 0 && rowIndex < start) // always read first line (contains header)
                    continue;
                if(rowIndex == start + maxRows)
                    break;
                sb.append(line).append("\n");
            }
        }
        String contentString =  sb.toString();
        return contentString;
    }

    private String readJSON(int start, int maxRows) throws IOException {
        throw new UnsupportedOperationException("method 'readJSON' not implemented yet");
    }

    private String readXML(int start, int maxRows) throws IOException {
        throw new UnsupportedOperationException("method 'readXML' not implemented yet");
    }
}
