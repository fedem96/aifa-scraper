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
                contentString = readJSON(start, maxRows);
                break;
            case "xml":
                contentString = readXML(start, maxRows);
                break;
            default:
                throw new IllegalArgumentException("Invalid format: '" + format + "'. Possible values: {csv, json, xml}");
        }
        if(contentString == null) // no more data
            return null;
        downloadedBytes += contentString.length()*2;
        System.out.println("downloaded " + String.format("%.3f", contentString.length()*2.0/1024/1024) + " MiB" +
                " (total: " + String.format("%.3f", downloadedBytes*1.0/1024/1024) + " MiB)");
        return contentString;
    }

    private String readCSV(int start, int maxRows) throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean somethingAdded = false;
        int rowIndex = 0;  // rowIndex==-1 for header row; rowIndex==0 for first data row
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
//            while ((line = br.readLine()) != null) {
//                rowIndex++;
//                if(rowIndex >= 0 && rowIndex < start) // always read first line (contains header)
//                    continue;
//                if(rowIndex == start + maxRows)
//                    break;
//                sb.append(line).append("\n");
//            }
            if ((line = br.readLine()) != null) {
                sb.append(line).append("\n");     // read header
                while ((line = br.readLine()) != null && rowIndex != start + maxRows) {
                    if(rowIndex++ < start)
                        continue;
                    somethingAdded = true;
                    sb.append(line).append("\n");
                }
            }
        }
        String contentString =  sb.toString();
        return somethingAdded ? contentString : null;
    }

    private String readJSON(int start, int maxRows) throws IOException {
        throw new UnsupportedOperationException("method 'readJSON' not implemented yet");
    }

    private String readXML(int start, int maxRows) throws IOException {
        throw new UnsupportedOperationException("method 'readXML' not implemented yet");
    }
}
