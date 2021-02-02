package mapper;

import dto.CompanyDto;
import model.Company;

public class CompanyMapper extends BaseMapper<Company, CompanyDto>{

    @Override
    public CompanyDto convert(Company company){
        if(company == null){
            throw new IllegalArgumentException("The company to convert is null");
        }
        CompanyDto compDto = new CompanyDto();
        compDto.setId(company.getId()); // TODO: probably it's code instead of id
//        compDto.setAddress(); // TODO: where is the address?
//        compDto.setEmail(); // TODO: where is the email?
//        compDto.setLogo(); // TODO: where is the logo?
//        compDto.setWebsite(); // TODO: where is the website?
        compDto.setName(company.getDescription());
        return compDto;
    }
}
