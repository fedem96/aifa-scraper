package com.fedem96.mapper;

import com.fedem96.dto.DrugDto;
import com.fedem96.model.Drug;

import javax.inject.Inject;

public class DrugMapper extends BaseMapper<Drug, DrugDto> {

    @Inject
    private PackagingMapper packagingMapper;

    @Override
    public DrugDto convert(Drug drug){
        if(drug == null){
            throw new IllegalArgumentException("The drug to convert is null");
        }
        DrugDto medDto = new DrugDto();
        medDto.setId(drug.getId());
        medDto.setName(drug.getDescription());
        medDto.setVersions(packagingMapper.convert(drug.getPackagings()));
        medDto.setActiveIngredient(drug.getActiveIngredient().getDescription());
        return medDto;
    }
}
