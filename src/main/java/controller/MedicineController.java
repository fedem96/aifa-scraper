package controller;

import dao.CompanyDao;
import dao.MedicineDao;
import dao.PrincipleDao;
import model.Medicine;
import model.ModelFactory;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

public class MedicineController {

    @Inject
    private MedicineDao medicineDao;

    @Inject
    private CompanyDao companyDao;
    @Inject
    private PrincipleDao principleDao;

    @Transactional
    public void addMedicines(Table tab){
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
            medicine.setCompany(companyDao.findByCode(row.getInt("sm_field_codice_ditta")));
            medicine.setPrinciple(principleDao.findByAtc(row.getString("sm_field_codice_atc")));
            medicineDao.save(medicine);
        }
    }

    public List<Medicine> search(String medicine){
        return medicineDao.search(medicine);
    }
}
