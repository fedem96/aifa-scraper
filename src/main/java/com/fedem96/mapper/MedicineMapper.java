package com.fedem96.mapper;

import com.fedem96.dto.MedicineDto;
import com.fedem96.model.Medicine;

import javax.inject.Inject;

public class MedicineMapper extends BaseMapper<Medicine, MedicineDto> {

    @Inject
    private PackagingMapper packagingMapper;

    @Override
    public MedicineDto convert(Medicine medicine){
        if(medicine == null){
            throw new IllegalArgumentException("The medicine to convert is null");
        }
        MedicineDto medDto = new MedicineDto();
        medDto.setId(medicine.getId());
        medDto.setName(medicine.getDescription());
        medDto.setPackagings(packagingMapper.convert(medicine.getPackagings()));
        medDto.setActivePrinciple(medicine.getPrinciple().getAtc());
        return medDto;
    }
}
