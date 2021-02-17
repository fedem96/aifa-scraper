package com.fedem96.rest;

import com.fedem96.controller.CompanyController;
import com.fedem96.controller.MedicineController;
import com.fedem96.controller.PrincipleController;
import com.fedem96.dto.MedicineDto;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("{a:search|medications}")
public class Searcher {

    @Inject
    private MedicineController medicineController;
    @Inject
    private PrincipleController principleController;
    @Inject
    private CompanyController companyController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@QueryParam("medicine") String medicine, @QueryParam("principle") String principle, @QueryParam("company") String company,
                           @QueryParam("name") String name, @QueryParam("activeIngredient") String activeIngredient, @QueryParam("text") String text) { // aliases
        if(medicine == null) medicine = name;
        if(principle == null) principle = activeIngredient;
        try {
            if(medicine == null && principle == null && company == null && text == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("no search parameters specified").build();
            } else if(medicine == null && principle == null && company == null && text != null) {
                System.out.println("Searching for '" + text + "' in description of medicine, company or principle");
                List<MedicineDto> medicines = medicineController.search(text);
                return Response.ok(medicines).build();
            } else if(medicine != null && principle == null && company == null) {
                System.out.println("Searching for '" + medicine + "' in description of medicine");
                List<MedicineDto> medicines = medicineController.searchByMedicine(medicine);
                return Response.ok(medicines).build();
            } else if(medicine == null && principle != null && company == null) {
                System.out.println("Searching for '" + principle + "' in description of principle");
                List<MedicineDto> medicines = medicineController.searchByPrinciple(principle);
                return Response.ok(medicines).build();
            } else if(medicine == null && principle == null && company != null) {
                System.out.println("Searching for '" + company + "' in description of company");
                List<MedicineDto> medicines = medicineController.searchByCompany(company);
                return Response.ok(medicines).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("too many parameters").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.serverError().build();
    }
}
