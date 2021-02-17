package com.fedem96.rest;

import com.fedem96.controller.MedicineController;
import com.fedem96.dto.MedicineDto;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("medicines")
public class MedicineAPI {
    @Inject
    private MedicineController medicineController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMedicines(){
        System.out.println("Getting all medicines");
        List<MedicineDto> medicines = medicineController.getAllMedicines();
        return Response.ok(medicines).build();
    }

    @Path("{medicineId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMedicine(@PathParam("medicineId") Long medicineId){
        System.out.println("Getting medicine with id='" + medicineId + "'");
        MedicineDto medicine = medicineController.getById(medicineId);
        return Response.ok(medicine).build();
    }
}
