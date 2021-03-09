package com.fedem96.mapper;

import com.fedem96.dto.DrugDto;
import com.fedem96.model.Drug;
import com.fedem96.model.Packaging;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DrugMapper extends BaseMapper<Drug, DrugDto> {

    @Inject
    private PackagingMapper packagingMapper;

    @Override
    public DrugDto convert(Drug drug) {
        return this.convert(drug, false); // by default, retired packagings are not returned
    }

    public DrugDto convert(Drug drug, boolean returnRetired){
        if(drug == null){
            throw new IllegalArgumentException("The drug to convert is null");
        }
        DrugDto medDto = new DrugDto();
        medDto.setId(drug.getId());
        medDto.setName(drug.getDescription());
        medDto.setVersions(packagingMapper.convert(drug.getPackagings(), returnRetired));
        Stream<Packaging> packagingStream = drug.getPackagings().stream();
        if(!returnRetired)
            packagingStream = packagingStream.filter(p -> !p.getState().equals("R")); // skip retired packagings
        String activeIngredientStr = packagingStream
                .map(p -> p.getActiveIngredient().getDescription()) // return only description of active ingredient
                .filter(p -> !p.equals(""))                         // do not return empty active ingredients
                .collect(Collectors.toSet()).stream()               // use set to avoid duplicates
                .collect(Collectors.joining(","));         // separate different active ingredients with a comma
        medDto.setActiveIngredient(activeIngredientStr);
        return medDto;
    }

    public List<DrugDto> convert(Collection<Drug> list, boolean returnRetired) {
        Stream<DrugDto> stream = list.stream().map(d -> this.convert(d, returnRetired));
        if(!returnRetired)
            stream = stream.filter(drugDto -> drugDto.getVersions().size() > 0);
        return stream.collect(Collectors.toList());
    }
}
