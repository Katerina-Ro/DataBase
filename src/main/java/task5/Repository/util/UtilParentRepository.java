package task5.Repository.util;

import org.springframework.stereotype.Repository;
import task5.Enteties.Address;
import task5.Enteties.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import task5.Repository.AddressRepository;
import task5.Repository.DistrictRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Вспомогательный класс для работы с объектом Parent
 */
@Repository
public class UtilParentRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private AddressRepository addressRepository;
    private DistrictRepository districtRepository;
    private UtilAddressRepository utilAddressRepository;

    @Value("SELECT Id, Full_name, IdAddress FROM Parent WHERE Full_name = :Full_name")
    private String sqlSelectCheckParent;

    @Autowired
    public UtilParentRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Autowired
    public void setAddressRepository(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Autowired
    public void setDistrictRepository(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    @Autowired
    public void setUtilAddressRepository(UtilAddressRepository utilAddressRepository) {
        this.utilAddressRepository = utilAddressRepository;
    }

    public boolean checkParentName(String full_name){
        AtomicBoolean checkParentName = new AtomicBoolean(false);
        Parent parentFromDB = new Parent();
        namedParameterJdbcTemplate.query(sqlSelectCheckParent, Map.of("Full_name", full_name), rs -> {
            parentFromDB.setFullName(rs.getString("Full_name"));
            if (parentFromDB.getFullName().equalsIgnoreCase(full_name)) {
                checkParentName.set(true);
            }
        });
        return checkParentName.get();
    }

    public int getIdParent(String full_name, Address address, String nameDistrict){
        AtomicInteger idParent = new AtomicInteger();
        namedParameterJdbcTemplate.query(sqlSelectCheckParent, Map.of("Full_name", full_name, "IdAddress",
                addressRepository.createAddress(districtRepository.createDistrict(nameDistrict).getNameDistrict(),
                        address.getStreet(), address.getNumber_house()).getIDAddress()), rs -> {
            idParent.set(getParent(rs).getIdParent());
        });
        return idParent.get();
    }

    public Parent getParent(ResultSet rs) throws SQLException {
        Parent parent = new Parent();
        parent.setIdParent(rs.getInt("Id"));
        parent.setFullName(rs.getString("Full_name"));
        parent.setIDAddress(addressRepository.getAddress(rs.getInt("IdAddress")));
        return parent;
    }

    public Parent getParent(String fullName, Address address) {
        Parent parent = new Parent();
        parent.setFullName(fullName);
        parent.setIDAddress(address);
        return parent;
    }
}