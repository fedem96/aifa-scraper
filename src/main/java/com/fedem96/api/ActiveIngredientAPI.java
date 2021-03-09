package com.fedem96.api;

import com.fedem96.controller.ActiveIngredientController;
import com.fedem96.model.ActiveIngredient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("activeIngredients")
public class ActiveIngredientAPI {

    @Inject
    private ActiveIngredientController activeIngredientController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActiveIngredients(){
        System.out.println("Getting all activeIngredients");
        List<ActiveIngredient> activeIngredients = activeIngredientController.getAllActiveIngredients();
        return Response.ok(activeIngredients).build();
    }

    @Path("{activeIngredientId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActiveIngredient(@PathParam("activeIngredientId") Long activeIngredientId){
        System.out.println("Getting activeIngredient with id='" + activeIngredientId +"'");
        ActiveIngredient activeIngredient = activeIngredientController.getById(activeIngredientId);
        return Response.ok(activeIngredient).build();
    }
    
}
