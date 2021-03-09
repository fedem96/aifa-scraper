package com.fedem96.mapper;

import com.fedem96.dto.PackagingDto;
import com.fedem96.model.Packaging;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PackagingMapper extends BaseMapper<Packaging, PackagingDto>{

    // since DTO contains some data not stored on the database, they are randomly generated

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
        packDto.setPrice(((int)(r.nextFloat()*100)) / 100f);
        packDto.setGluten(r.nextBoolean());
        packDto.setLactose(r.nextBoolean());
        packDto.setLeaflet(packaging.getDrug().getLinkFi());
        packDto.setType(new String[]{"BOTTLE", "CAPSULES", "PLASTER", "SYRINGE", "TABLETS"}[r.nextInt(5)]);
        packDto.setVersionClass(new String[]{"A", "C", "CN", "H"}[r.nextInt(4)]);
        return packDto;
    }

    public List<PackagingDto> convert(List<Packaging> packagings, boolean returnRetired) {
        Stream<Packaging> stream = packagings.stream();
        if(!returnRetired)
            stream = stream.filter(p -> !p.getState().equals("R"));
        return stream.map(d -> this.convert(d)).collect(Collectors.toList());
    }
}
