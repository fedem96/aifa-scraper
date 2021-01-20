package rest;

import controller.ScrapeController;

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
public class Scraper { // TODO: handle date

    @Inject
    ScrapeController scrapeController;

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    public Response scrape(@QueryParam("rows") Integer rows) {
        long start = currentTimeMillis();
        try {
            scrapeController.scrape(rows);
            return Response.ok().entity("done in " + ((currentTimeMillis()-start)*0.001) + " seconds").build();
        } catch (FileNotFoundException e){
            return Response.serverError().entity("remote service unavailable").build();
        }
        catch (IOException e) {
            return Response.serverError().build();
        }
    }
}
