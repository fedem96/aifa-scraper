package rest;

import controller.MedicineController;
import dto.MedicineDto;
import mapper.MedicineMapper;

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
    @Inject
    private MedicineMapper medicineMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMedicines(){
        List<MedicineDto> medicines = medicineMapper.convert(medicineController.getAllMedicines());
        return Response.ok(medicines).build();
    }

    @Path("{medicineId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMedicine(@PathParam("medicineId") Long medicineId){
        MedicineDto medicine = medicineMapper.convert(medicineController.getById(medicineId));
        return Response.ok(medicine).build();
    }
}
