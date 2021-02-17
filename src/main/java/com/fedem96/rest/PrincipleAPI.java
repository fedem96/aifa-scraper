package com.fedem96.rest;

import com.fedem96.controller.PrincipleController;
import com.fedem96.model.Principle;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("{a:principles|activeIngredients}")
public class PrincipleAPI {

    @Inject
    private PrincipleController principleController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrinciples(){
        System.out.println("Getting all principles");
        List<Principle> principles = principleController.getAllPrinciples();
        return Response.ok(principles).build();
    }

    @Path("{principleId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrinciple(@PathParam("principleId") Long principleId){
        System.out.println("Getting principle with id='" + principleId +"'");
        Principle principle = principleController.getById(principleId);
        return Response.ok(principle).build();
    }
    
}
