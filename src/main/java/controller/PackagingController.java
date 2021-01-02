package controller;

import dao.MedicineDao;
import dao.PackagingDao;
import model.ModelFactory;
import model.Packaging;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import javax.inject.Inject;
import javax.transaction.Transactional;

public class PackagingController {
    @Inject
    PackagingDao packagingDao;

    @Inject
    MedicineDao medicineDao;

    @Transactional
    public void addPackagings(Table tab){
        for (Row row: tab){
            long aic = row.getInt("sm_field_aic");
            Packaging packaging = packagingDao.findByAic(aic);
            if(packaging == null){
                packaging = ModelFactory.packaging();
            }
            packaging.setAic(aic);
            packaging.setDescription(row.getString("sm_field_descrizione_confezione"));
            packaging.setState(row.getString("sm_field_stato_farmaco"));
            packaging.setMedicine(medicineDao.findByCode(row.getInt("sm_field_codice_farmaco")));
            packagingDao.save(packaging);
        }
    }
}
