package com.fedem96.api;

import com.fedem96.controller.DrugController;
import com.fedem96.dto.DrugDto;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("drugs")
public class DrugAPI {
    @Inject
    private DrugController drugController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDrugs(@QueryParam("retired") @DefaultValue("false") boolean returnRetired){
        System.out.println("Getting all drugs");
        List<DrugDto> drugs = drugController.getAllDrugs(returnRetired);
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
