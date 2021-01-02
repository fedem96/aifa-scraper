package rest;

import dao.MedicineDao;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("search")
public class Searcher {

    @Inject
    private MedicineDao medDao;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response search(@QueryParam("name") String name, @QueryParam("principle") String principle, @QueryParam("company") String company) {
        try {
            medDao.search(name, principle, company);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok().entity("Hello, I'm searching for " + name +  principle + company + "!!!").build();
    }
}
