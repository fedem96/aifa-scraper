package com.fedem96.requester;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Request {

    private Map<String, String> parameters;
    private Map<String, String> queryOnParam;
    private String genericQuery;

    private Requester requester;

    public Request(){
        parameters = new HashMap<>();
        queryOnParam = new HashMap<>();
        genericQuery = null;
    }

    public Request setRequester(Requester requester) {
        this.requester = requester;
        return this;
    }

    public Request format(String format){
        if(!format.equals("csv") && !format.equals("json") && !format.equals("xml")){
            throw new IllegalArgumentException("Invalid format: '" + format + "'. Possible values: {csv, json, xml}");
        }
        parameters.put("wt", format);
        return this;
    }

    public Request year(Integer year) {
        if(year != null)
            this.query(year);
        return this;
    }

    public Request columns(Collection<String> columns) {
        parameters.put("fl", String.join(",", columns));
        return this;
    }

    public Request start(Integer start){
        if(start < 0){
            throw new IllegalArgumentException("start must be >= 0, it can't be '" + start.toString() + "'");
        }
        parameters.put("start", start.toString());
        return this;
    }

    public Request rows(Integer numRows){
        if(numRows < 0){
            throw new IllegalArgumentException("number of rows must be > 0, it can't be '" + numRows.toString() + "'");
        }
        parameters.put("rows", numRows.toString());
        return this;
    }

    public Request query(Object query){
        if(genericQuery != null)
            throw new UnsupportedOperationException("Generic query already set");
        genericQuery = query.toString();
        return this;
    }

    public Request paramQuery(String param, Object query){
        queryOnParam.put(param, query.toString());
        return this;
    }

    public String send() throws IOException {
        parameters.put("q", queryOnParam.entrySet().stream().map(pair -> pair.getKey() + ":" + pair.getValue()).collect(Collectors.joining("+")));
        return requester.sendRequest(parameters);
    }

}
