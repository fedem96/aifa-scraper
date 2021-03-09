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
               @QueryParam("start") @DefaultValue("0") Integer start, @QueryParam("pageSize") Integer pageSize, @QueryParam("rows") @DefaultValue("10") Integer rows,
               @QueryParam("checkLastUpdate") @DefaultValue("false") boolean checkLastUpdate, @QueryParam("year") Integer year) {
        if(url != null && file != null){
            return Response.serverError().entity("at most one between url and file can be set").build();
        }
        if(pageSize == null)
            pageSize = rows;
        System.out.println("Scraping");
        long startingTime = currentTimeMillis();
        try {
            if(file == null)
                scrapeController.scrapeURL(url, start, pageSize, rows, year, checkLastUpdate);
            else
                scrapeController.scrapeFile(file, start, pageSize, rows, checkLastUpdate);
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
