package com.fedem96.rest;

import com.fedem96.controller.CompanyController;
import com.fedem96.controller.DrugController;
import com.fedem96.controller.ActiveIngredientController;
import com.fedem96.dto.DrugDto;

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
    private DrugController drugController;
    @Inject
    private ActiveIngredientController activeIngredientController;
    @Inject
    private CompanyController companyController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@QueryParam("drug") String drug, @QueryParam("activeIngredient") String activeIngredient, @QueryParam("company") String company,
                           @QueryParam("name") String name /*alias for drug*/, @QueryParam("text") String text) {
        if(drug == null) drug = name;
        try {
            if(drug == null && activeIngredient == null && company == null && text == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("no search parameters specified").build();
            } else if(drug == null && activeIngredient == null && company == null && text != null) {
                System.out.println("Searching for '" + text + "' in description of drug, company or activeIngredient");
                List<DrugDto> drugs = drugController.search(text);
                return Response.ok(drugs).build();
            } else if(drug != null && activeIngredient == null && company == null) {
                System.out.println("Searching for '" + drug + "' in description of drug");
                List<DrugDto> drugs = drugController.searchByDrug(drug);
                return Response.ok(drugs).build();
            } else if(drug == null && activeIngredient != null && company == null) {
                System.out.println("Searching for '" + activeIngredient + "' in description of activeIngredient");
                List<DrugDto> drugs = drugController.searchByActiveIngredient(activeIngredient);
                return Response.ok(drugs).build();
            } else if(drug == null && activeIngredient == null && company != null) {
                System.out.println("Searching for '" + company + "' in description of company");
                List<DrugDto> drugs = drugController.searchByCompany(company);
                return Response.ok(drugs).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("too many parameters").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.serverError().build();
    }
}
