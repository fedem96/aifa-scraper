package com.fedem96.api;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationPath("api")
@Path("/")
public class APIApplication extends Application {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response hello(){
        return Response.ok("Hello from AIFA-scraper server").build();
    }

}
