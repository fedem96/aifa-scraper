package dto;

import java.util.List;

public class MedicineDto extends BaseDto{
    private Long id;
    private String name;
    private String activePrinciple; // TODO: check var name
    private List<PackagingDto> packagings;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivePrinciple() {
        return activePrinciple;
    }

    public void setActivePrinciple(String activePrinciple) {
        this.activePrinciple = activePrinciple;
    }

    public List<PackagingDto> getPackagings() {
        return packagings;
    }

    public void setPackagings(List<PackagingDto> packagings) {
        this.packagings = packagings;
    }
}
