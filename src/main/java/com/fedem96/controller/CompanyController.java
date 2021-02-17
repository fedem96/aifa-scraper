package com.fedem96.controller;

import com.fedem96.dao.CompanyDao;
import com.fedem96.dto.CompanyDto;
import com.fedem96.mapper.CompanyMapper;
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
    @Inject
    private CompanyMapper companyMapper;

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

    public CompanyDto getById(Long companyId) {
        return companyMapper.convert(companyDao.findById(companyId));
    }

    public List<CompanyDto> getAllCompanies() {
        return companyMapper.convert(companyDao.getAll());
    }

    public List<CompanyDto> search(String company){
        return companyMapper.convert(companyDao.search(company));
    }
}
