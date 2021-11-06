package task5.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import task5.Enteties.Address;
import task5.Repository.util.UtilAddressRepository;
import task5.Repository.util.UtilDistrictRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class AddressRepositoryImpl implements AddressRepository{

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private UtilAddressRepository utilAddress;
    private UtilDistrictRepository utilDistrictRepository;

    @Value("INSERT into Address (IdDistrict, Street, Number_house) VALUES (:IdDistrict, :Street, :Number_house)")
    private String sqlInsertAddress;

    @Value("SELECT Id, IdDistrict, Street, Number_house FROM Address WHERE Id = :Id")
    private String sqlSelectCheckAddress;

    @Value("SELECT Id, IdDistrict, Street, Number_house FROM Address WHERE IdDistrict = :IdDistrict")
    private String sqlSelectIdDistrict;

    @Autowired
    public AddressRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Autowired
    public void setUtilAddress(UtilAddressRepository utilAddress) {
        this.utilAddress = utilAddress;
    }

    @Autowired
    public void setUtilDistrictRepository(UtilDistrictRepository utilDistrictRepository) {
        this.utilDistrictRepository = utilDistrictRepository;
    }

    @Override
    public Address createAddress(String nameDistrict, String street, int number_house) {
        Address address;
        if(!utilAddress.checkAddress(street, number_house)) {
            namedParameterJdbcTemplate.update(sqlInsertAddress, Map.of("IdDistrict",
                    utilDistrictRepository.getIdDistrict(nameDistrict), "Street", street,
                    "Number_house", number_house));
        }
        address = getAddress(utilAddress.getIdAddress(street,number_house));
        return address;
    }

    @Override
    public Address getAddress(int idAddress){
        AtomicReference<Address> address = new AtomicReference<>(new Address());
        namedParameterJdbcTemplate.query(sqlSelectCheckAddress, Map.of("Id", idAddress), rs -> {
            address.set(utilAddress.getAddress(rs));
        });
        return address.get();
    }

    @Override
    public Collection<Address> getListAddress (String nameDictrict){
        List<Address> addressList = new ArrayList<>();
        namedParameterJdbcTemplate.query(sqlSelectIdDistrict, Map.of("IdDistrict",
                utilDistrictRepository.getIdDistrict(nameDictrict)), rs -> {
            addressList.add(utilAddress.getAddress(rs));
        });
        return addressList;
    }
}
