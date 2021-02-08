package com.fedem96.controller;

import com.fedem96.dao.CompanyDao;
import com.fedem96.model.Company;
import com.fedem96.model.ModelFactory;
import tech.tablesaw.api.Row;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyController {

    @Inject
    private CompanyDao companyDao;

    @Transactional
    public Map<Long, Company> addCompanies(Iterable<Row> rows){
        Map<Long, Company> map = new HashMap<>();
        for (Row row: rows){
            long code = row.getInt("sm_field_codice_ditta");
            Company company = companyDao.findByCode(code);
            if(company == null){
                company = ModelFactory.company();
            }
            company.setCode(code);
            company.setDescription(row.getString("sm_field_descrizione_ditta"));
            companyDao.save(company);
            map.put(code, company);
        }
        return map;
    }

    public List<Company> search(String company){
        return companyDao.search(company);
    }

    public Company getById(Long companyId) {
        return companyDao.findById(companyId);
    }

    public List<Company> getAllCompanies() {
        return companyDao.getAll();
    }
}
