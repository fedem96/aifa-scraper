package rest;

import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import utils.Requester;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.System.currentTimeMillis;

@Path("scrape")
public class Scraper {
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    public Response scrape() {
        // TODO: finish and refactor
        long start = currentTimeMillis();
        Map<String,String> parameters = new HashMap<String,String>();
        parameters.put("q", "bundle:confezione_farmaco+sm_field_descrizione_farmaco:*");
        parameters.put("df", "sm_field_descrizione_farmaco");
        parameters.put("wt", "csv");
        parameters.put("rows", "200");
        try {
            String csvText = new Requester().sendRequest(parameters);
            Table table = Table.read().csv(new StringReader(csvText));
            System.out.println(table.columns().stream().map(col -> col.name() + "\n").collect(Collectors.joining()));

            Table tabFarmaci = table.select("sm_field_codice_farmaco", "sm_field_descrizione_farmaco", "sm_field_link_fi", "sm_field_link_rcp",
                    "sm_field_codice_atc", "sm_field_codice_ditta").dropRowsWithMissingValues().dropDuplicateRows(); // TODO: handle missing values
            Table tabDitta = table.select("sm_field_codice_ditta", "sm_field_descrizione_ditta").dropDuplicateRows();
            Table tabPrincipi = table.select("sm_field_codice_atc", "sm_field_descrizione_atc").dropDuplicateRows();
            Table tabConfezioni = table.select("sm_field_aic", "sm_field_descrizione_confezione", "sm_field_stato_farmaco",
                    "sm_field_codice_farmaco").dropDuplicateRows();
            System.out.println("\nFARMACI" + tabFarmaci.first(40));
            System.out.println("\nDITTE" + tabDitta.first(40));
            System.out.println("\nPRINCIPI" + tabPrincipi.first(40));
            System.out.println("\nCONFEZIONI" + tabConfezioni.first(40));
            return Response.ok().entity("done in " + ((currentTimeMillis()-start)*0.001) + " seconds").build();
        } catch (IOException e) {
            return Response.serverError().build();
        }
    }
}
