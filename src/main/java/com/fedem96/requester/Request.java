package com.fedem96.requester;

import java.io.IOException;
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
        parameters.put("wt", format);
        return this;
    }

    public Request rows(Integer numRows){
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
