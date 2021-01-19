package controller;

import dao.MedicineDao;
import model.Company;
import model.Medicine;
import model.ModelFactory;
import model.Principle;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicineController {

    @Inject
    private MedicineDao medicineDao;

    @Transactional
    public Map<Long, Medicine> addMedicines(Table tab, Map<Long, Company> mapCompanies, Map<String, Principle> mapPrinciples){
        Map<Long, Medicine> map = new HashMap<>();
        for (Row row: tab){
            long code = row.getInt("sm_field_codice_farmaco");
            Medicine medicine = medicineDao.findByCode(code);
            if(medicine == null){
                medicine = ModelFactory.medicine();
            }
            medicine.setCode(code);
            medicine.setDescription(row.getString("sm_field_descrizione_farmaco"));
            medicine.setLinkFi(row.getString("sm_field_link_fi"));
            medicine.setLinkRcp(row.getString("sm_field_link_rcp"));
            medicine.setCompany(mapCompanies.get(row.getInt("sm_field_codice_ditta")));
            medicine.setPrinciple(mapPrinciples.get(row.getString("sm_field_codice_atc")));
            medicineDao.save(medicine);
            map.put(code, medicine);
        }
        return map;
    }

    public List<Medicine> search(String medicine){
        return medicineDao.search(medicine);
    }
}
