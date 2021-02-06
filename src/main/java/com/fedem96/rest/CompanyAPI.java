package com.fedem96.rest;

import com.fedem96.controller.CompanyController;
import com.fedem96.model.Company;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("companies")
public class CompanyAPI {

    @Inject
    private CompanyController companyController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompanies(){
        List<Company> companies = companyController.getAllCompanies();
        return Response.ok(companies).build();
    }

    @Path("{companyId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompany(@PathParam("companyId") Long companyId){
        Company company = companyController.getById(companyId);
        return Response.ok(company).build();
    }
}
