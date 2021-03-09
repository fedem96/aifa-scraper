package com.fedem96.api;

import com.fedem96.controller.DrugController;
import com.fedem96.dto.DrugDto;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("drugs")
public class DrugAPI {
    @Inject
    private DrugController drugController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDrugs(){
        System.out.println("Getting all drugs");
        List<DrugDto> drugs = drugController.getAllDrugs();
        return Response.ok(drugs).build();
    }

    @Path("{drugId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDrug(@PathParam("drugId") Long drugId){
        System.out.println("Getting drug with id='" + drugId + "'");
        DrugDto drug = drugController.getById(drugId);
        return Response.ok(drug).build();
    }
}
