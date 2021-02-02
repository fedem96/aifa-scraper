package mapper;

import dto.PackagingDto;
import model.Packaging;

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
        packDto.setName(packaging.getDescription()); // TODO: difference between name and description?
//        packDto.setPrice(); // TODO: where is the price?
//        packDto.setGluten();// TODO: where is gluten?
//        packDto.setLactose();// TODO: where is lactose?
        packDto.setLeaflet(packaging.getMedicine().getLinkFi()); // TODO: check if leaflet is different for different packagings of the same medicine
//        packDto.setType(); // TODO: what is this?
//        packDto.setVersionClass();// TODO: what is this?
        return packDto;
    }
}
