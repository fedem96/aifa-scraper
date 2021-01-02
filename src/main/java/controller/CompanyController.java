package controller;

import dao.CompanyDao;
import model.Company;
import model.ModelFactory;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import javax.inject.Inject;
import javax.transaction.Transactional;

public class CompanyController {

    @Inject
    private CompanyDao companyDao;

    @Transactional
    public void addCompanies(Table tab){
        for (Row row: tab){
            long code = row.getInt("sm_field_codice_ditta");
            Company company = companyDao.findByCode(code);
            if(company == null){
                company = ModelFactory.company();
            }
            company.setCode(code);
            company.setDescription(row.getString("sm_field_descrizione_ditta"));
            companyDao.save(company);
        }
    }

}
