package com.fedem96.requester;

public class RequestFactory {

    public static Request scrapeFromFile(String file){
        return new Request().setRequester(new FileRequester(file)).format("csv").paramQuery("bundle", "confezione_farmaco");
    }

    public static Request scrapeFromUrl(String url){
        Requester requester = url==null ? new UrlRequester() : new UrlRequester(url);
        return new Request().setRequester(requester).format("csv").paramQuery("bundle", "confezione_farmaco");
    }

}
