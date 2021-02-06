package com.fedem96.mapper;

import com.fedem96.dto.PackagingDto;
import com.fedem96.model.Packaging;

public class PackagingMapper extends BaseMapper<Packaging, PackagingDto>{

    @Override
    public PackagingDto convert(Packaging packaging){
        if(packaging == null){
            throw new IllegalArgumentException("The packaging to convert is null");
        }
        PackagingDto packDto = new PackagingDto();
        packDto.setId(packaging.getId());
//        packDto.setCompany(); // TODO: no company?
        packDto.setDescription(packaging.getDescription());
        packDto.setName(packaging.getDescription()); // TODO: no between name and description
//        packDto.setPrice(); // TODO: where is the price?
//        packDto.setGluten();// TODO: where is gluten?
//        packDto.setLactose();// TODO: where is lactose?
        packDto.setLeaflet(packaging.getMedicine().getLinkFi()); // TODO: leaflet is the same for different packagings
//        packDto.setType(); // TODO: what is this?
//        packDto.setVersionClass();// TODO: what is this?
        return packDto;
    }
}
