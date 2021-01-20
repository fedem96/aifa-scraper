package controller;

import model.Company;
import model.Medicine;
import model.Principle;
import requester.RequestFactory;
import tech.tablesaw.api.Table;

import javax.inject.Inject;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

public class ScrapeController {

    @Inject
    CompanyController companyController;
    @Inject
    MedicineController medicineController;
    @Inject
    PackagingController packagingController;
    @Inject
    PrincipleController principleController;

    public void scrape(Integer maxRows) throws IOException {
        String csvText = RequestFactory.scrapeRequest().rows(maxRows).send();
        System.out.println(csvText);
        Table table = Table.read().csv(new StringReader(csvText));

        Table tabMedicines = table.select("sm_field_codice_farmaco", "sm_field_descrizione_farmaco", "sm_field_link_fi", "sm_field_link_rcp",
                "sm_field_codice_atc", "sm_field_codice_ditta").dropDuplicateRows(); // TODO: handle missing values
        Table tabCompanies = table.select("sm_field_codice_ditta", "sm_field_descrizione_ditta").dropDuplicateRows();
        Table tabPrinciples = table.select("sm_field_codice_atc", "sm_field_descrizione_atc").dropDuplicateRows();
        Table tabPackagings = table.select("sm_field_aic", "sm_field_descrizione_confezione", "sm_field_stato_farmaco",
                "sm_field_codice_farmaco").dropDuplicateRows();

        Map<Long, Company> mapCompanies = companyController.addCompanies(tabCompanies);
        Map<String, Principle> mapPrinciples = principleController.addPrinciples(tabPrinciples);
        Map<Long, Medicine> medicinesMap = medicineController.addMedicines(tabMedicines, mapCompanies, mapPrinciples);
        packagingController.addPackagings(tabPackagings, medicinesMap);
    }
}
