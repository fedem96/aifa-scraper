package com.fedem96.rest;

import com.fedem96.controller.ScrapeController;

import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
               @QueryParam("start") Integer start, @QueryParam("pageSize") Integer pageSize, @QueryParam("rows") Integer rows) {
        if(url != null && file != null){
            return Response.serverError().entity("at most one between url and file can be set").build();
        }
        if(start == null)
            start = 0;
        if(pageSize == null)
            pageSize = rows;
        System.out.println("Scraping");
        long startingTime = currentTimeMillis();
        try {
            if(file == null)
                scrapeController.scrapeURL(url, start, pageSize, rows);
            else
                scrapeController.scrapeFile(file, start, pageSize, rows);
            scrapeController.setLastUpdate();
            return Response.ok().entity("done in " + ((currentTimeMillis()-startingTime)*0.001) + " seconds").build();
        } catch (FileNotFoundException e){
            return Response.serverError().entity("remote service unavailable").build();
        }
        catch (IOException e) {
            return Response.serverError().build();
        }
    }
}
