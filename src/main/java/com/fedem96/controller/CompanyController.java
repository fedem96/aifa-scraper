package com.fedem96.controller;

import com.fedem96.dao.CompanyDao;
import com.fedem96.dto.CompanyDto;
import com.fedem96.mapper.CompanyMapper;
import com.fedem96.model.Company;
import com.fedem96.model.ModelFactory;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;
import tech.tablesaw.selection.Selection;
import tech.tablesaw.table.TableSlice;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CompanyController {

    @Inject
    private CompanyDao companyDao;
    @Inject
    private CompanyMapper companyMapper;

    public Map<Long, Company> addCompanies(Table tab, int maxPerTransaction){
        Map<Long, Company> companiesMap = new HashMap<>();
        int numRows = tab.rowCount();
        for(int index = 0; index < numRows; index += maxPerTransaction) {
            TableSlice ts = new TableSlice(tab, Selection.withRange(index, Math.min(index+maxPerTransaction, numRows)));
            Map<Long, Company> addedCompaniesMap = this.extractCompanies(ts);
            companyDao.save(addedCompaniesMap.values());
            companiesMap.putAll(addedCompaniesMap);
        }
        return companiesMap;
    }

    public Map<Long, Company> extractCompanies(Iterable<Row> rows){
        Map<Long, Company> map = new HashMap<>();
        List<Company> companies = new LinkedList<>();
        for (Row row: rows){
            long code = row.getInt("sm_field_codice_ditta");
            Company company = companyDao.findByCode(code);
            if(company == null){
                company = ModelFactory.company();
            }
            company.setCode(code);
            company.setDescription(row.getString("sm_field_descrizione_ditta"));
            companies.add(company);
            map.put(code, company);
//            companyDao.save(company);
        }
        return map;
    }

    public CompanyDto getById(Long companyId) {
        return companyMapper.convert(companyDao.findById(companyId));
    }

    public List<CompanyDto> getAllCompanies() {
        return companyMapper.convert(companyDao.getAll());
    }

    public List<CompanyDto> search(String company, Integer firstResult, Integer maxResults){
        return companyMapper.convert(companyDao.search(company, firstResult, maxResults));
    }
}
