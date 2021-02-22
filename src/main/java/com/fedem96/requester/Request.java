package com.fedem96.requester;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Request {

    private Map<String, String> parameters;
    private Map<String, String> queryOnParam;

    private Requester requester;

    public Request(){
        parameters = new HashMap<>();
        queryOnParam = new HashMap<>();
    }

    public Request setRequester(Requester requester) {
        this.requester = requester;
        return this;
    }

    public Request dataField(String field){ // TODO: decide whether to remove
        parameters.put("df", field);
        return this;
    }

    public Request format(String format){
        if(!format.equals("csv") && !format.equals("json") && !format.equals("xml")){
            throw new IllegalArgumentException("Invalid format: '" + format + "'. Possible values: {csv, json, xml}");
        }
        parameters.put("wt", format);
        return this;
    }

    public Request columns(Collection<String> columns) {
        parameters.put("fl", columns.stream().collect(Collectors.joining(",")));
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

    public <T> Request paramQuery(String param, T query){
        queryOnParam.put(param, query.toString());
        return this;
    }

    public String send() throws IOException {
        parameters.put("q", queryOnParam.entrySet().stream().map(pair -> pair.getKey() + ":" + pair.getValue()).collect(Collectors.joining("+")));
        return requester.sendRequest(parameters);
    }

    public String send(String query) throws IOException {
        if(queryOnParam.size() > 0)
            System.err.println("WARNING: when you specify the global query, you can't use parameter-specific queries");
        parameters.put("q", query);
        return requester.sendRequest(parameters);
    }

}
