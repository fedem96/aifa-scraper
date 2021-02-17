package com.fedem96.mapper;

import com.fedem96.dto.CompanyDto;
import com.fedem96.model.Company;

public class CompanyMapper extends BaseMapper<Company, CompanyDto>{

    @Override
    public CompanyDto convert(Company company){
        if(company == null){
            throw new IllegalArgumentException("The company to convert is null");
        }
        CompanyDto compDto = new CompanyDto();
        compDto.setId(company.getId()); // TODO: probably it's code instead of id
        compDto.setAddress("Via Roma 1, Roma");
        compDto.setEmail(company.getDescription().toLowerCase().replaceAll("(\\s)|(\\.)", "") + "@website.example");
        compDto.setLogo("http://website.example/logo.png");
        compDto.setWebsite("http://website.example");
        compDto.setName(company.getDescription());
        return compDto;
    }
}
