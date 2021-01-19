package rest;

import controller.CompanyController;
import controller.MedicineController;
import controller.PackagingController;
import controller.PrincipleController;
import model.Company;
import model.Medicine;
import model.Principle;
import tech.tablesaw.api.Table;
import requester.Requester;

import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.currentTimeMillis;

@Path("scrape")
public class Scraper { // TODO: handle date

    @Inject
    CompanyController companyController;
    @Inject
    MedicineController medicineController;
    @Inject
    PackagingController packagingController;
    @Inject
    PrincipleController principleController;

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    public Response scrape(@QueryParam("rows") String rows) {

        // TODO: finish and refactor
        long start = currentTimeMillis();
        Map<String,String> parameters = new HashMap<String,String>();
        parameters.put("q", "bundle:confezione_farmaco+sm_field_descrizione_farmaco:*");
        parameters.put("df", "sm_field_descrizione_farmaco");
        parameters.put("wt", "csv");
        parameters.put("rows", rows);
        try {
            String csvText = new Requester().sendRequest(parameters);
            System.out.println(csvText);
            Table table = Table.read().csv(new StringReader(csvText));

            Table tabMedicines = table.select("sm_field_codice_farmaco", "sm_field_descrizione_farmaco", "sm_field_link_fi", "sm_field_link_rcp",
                    "sm_field_codice_atc", "sm_field_codice_ditta").dropDuplicateRows(); // TODO: handle missing values
            Table tabCompanies = table.select("sm_field_codice_ditta", "sm_field_descrizione_ditta").dropDuplicateRows();
            Table tabPrinciples = table.select("sm_field_codice_atc", "sm_field_descrizione_atc").dropDuplicateRows();
            Table tabPackagings = table.select("sm_field_aic", "sm_field_descrizione_confezione", "sm_field_stato_farmaco",
                    "sm_field_codice_farmaco").dropDuplicateRows();

//            System.out.println("\nFARMACI" + tabMedicines.first(20));
//            System.out.println("\nDITTE" + tabCompanies.first(20));
//            System.out.println("\nPRINCIPI" + tabPrinciples.first(20));
//            System.out.println("\nCONFEZIONI" + tabPackagings.first(20));

            Map<Long, Company> mapCompanies = companyController.addCompanies(tabCompanies);
            Map<String, Principle> mapPrinciples = principleController.addPrinciples(tabPrinciples);
            Map<Long, Medicine> medicinesMap = medicineController.addMedicines(tabMedicines, mapCompanies, mapPrinciples);
            packagingController.addPackagings(tabPackagings, medicinesMap);

            return Response.ok().entity("done in " + ((currentTimeMillis()-start)*0.001) + " seconds").build();
        } catch (FileNotFoundException e){
            return Response.serverError().entity("remote service unavailable").build();
        }
        catch (IOException e) {
            return Response.serverError().build();
        }
    }
}
