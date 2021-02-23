package com.fedem96.mapper;

import com.fedem96.dto.PackagingDto;
import com.fedem96.model.Packaging;

import java.util.Random;

public class PackagingMapper extends BaseMapper<Packaging, PackagingDto>{

    @Override
    public PackagingDto convert(Packaging packaging){
        if(packaging == null){
            throw new IllegalArgumentException("The packaging to convert is null");
        }
        Random r = new Random();
        PackagingDto packDto = new PackagingDto();
        packDto.setId(packaging.getId());
        packDto.setCompany(packaging.getDrug().getCompany().getId());
        packDto.setCompanyName(packaging.getDrug().getCompany().getDescription());
        packDto.setDescription(packaging.getDescription());
        packDto.setName(packaging.getDescription());
        packDto.setPrice(r.nextFloat());
        packDto.setGluten(r.nextBoolean());
        packDto.setLactose(r.nextBoolean());
        packDto.setLeaflet(packaging.getDrug().getLinkFi());
        packDto.setType(new String[]{"BOTTLE", "CAPSULES", "PLASTER", "SYRINGE", "TABLETS"}[r.nextInt(5)]);
        packDto.setVersionClass(new String[]{"A", "C", "CN", "H"}[r.nextInt(4)]);
        return packDto;
    }
}
