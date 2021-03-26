package com.fedem96.api;

import com.fedem96.controller.ScrapeController;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.io.IOException;

import static java.lang.System.currentTimeMillis;

@Path("scrape")
public class Scraper {

    @Inject
    ScrapeController scrapeController;

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    public Response scrape(@QueryParam("url") String url, @QueryParam("file") String file,
               @QueryParam("firstResult") @DefaultValue("0") Integer firstResult, @QueryParam("downloadSize") Integer downloadSize, @QueryParam("maxResults") @DefaultValue("10") Integer maxResults,
               @QueryParam("checkLastUpdate") @DefaultValue("false") boolean checkLastUpdate, @QueryParam("year") Integer year,
               @QueryParam("transactionSize") @DefaultValue("1000") Integer transactionSize) {
        if(url != null && file != null){
            return Response.serverError().entity("at most one between url and file can be set").build();
        }
        if(downloadSize == null)
            downloadSize = maxResults;
        System.out.println("Scraping");
        long startingTime = currentTimeMillis();
        try {
            if(file == null)
                scrapeController.scrapeURL(url, firstResult, downloadSize, maxResults, year, transactionSize, checkLastUpdate);
            else
                scrapeController.scrapeFile(file, firstResult, downloadSize, maxResults, transactionSize, checkLastUpdate);
            scrapeController.setLastUpdate();
            return Response.ok().entity("done in " + ((currentTimeMillis()-startingTime)*0.001) + " seconds\n").build();
        } catch (FileNotFoundException e){
            return Response.serverError().entity("remote service unavailable").build();
        }
        catch (IOException e) {
            return Response.serverError().build();
        }
    }
}
