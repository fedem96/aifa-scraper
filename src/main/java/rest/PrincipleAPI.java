package rest;

import controller.PrincipleController;
import model.Principle;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("principles")
public class PrincipleAPI {

    @Inject
    private PrincipleController principleController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrinciples(){
        List<Principle> principles = principleController.getAllPrinciples();
        return Response.ok(principles).build();
    }

    @Path("{principleId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrinciple(@PathParam("principleId") Long principleId){
        Principle principle = principleController.getById(principleId);
        return Response.ok(principle).build();
    }
    
}
