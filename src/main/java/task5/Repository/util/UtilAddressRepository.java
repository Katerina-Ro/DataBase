package task5.Repository.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import task5.Enteties.Address;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Вспомогательный класс для работы с объектом Address
 */
@Repository
public class UtilAddressRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private UtilDistrictRepository utilDistrictRepository;

    @Value("SELECT Id, IdDistrict, Street, Number_house FROM address WHERE Street = :Street AND " +
            "Number_house = :Number_house")
    private String sqlSelectCheckAddress;

    @Value("SELECT Id, IdDistrict, Street, Number_house FROM Address WHERE Street = :Street AND " +
            "Number_house = :Number_house")
    private String sqlSelectAddressId;

    @Autowired
    public UtilAddressRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Autowired
    public void setUtilDistrictRepository(UtilDistrictRepository utilDistrictRepository) {
        this.utilDistrictRepository = utilDistrictRepository;
    }

    public Address getAddress(ResultSet rs) throws SQLException {
        Address addressResultSet = new Address();
        addressResultSet.setIDAddress(rs.getInt("Id"));
        addressResultSet.setStreet((rs.getString("Street")));
        addressResultSet.setDistrict(rs.getInt("IdDistrict"));
        addressResultSet.setNumber_house(rs.getInt("Number_house"));
        return addressResultSet;
    }

    public Address getAddress (String nameDistrict, String nameStreet, int numberHouse){
        Address address = new Address();
        address.setDistrict(utilDistrictRepository.getIdDistrict(nameDistrict));
        address.setStreet(nameStreet);
        address.setNumber_house(numberHouse);
        return address;
    }

    public int getIdAddress (String street, int numberHouse){
          AtomicInteger iDAddress = new AtomicInteger();
        if(checkAddress(street,numberHouse)) {
            namedParameterJdbcTemplate.query(sqlSelectAddressId, Map.of("Street", street, "Number_house",
                    numberHouse), rs -> {
                iDAddress.set(getAddress(rs).getIDAddress());
            });
        } else {
            iDAddress.set(0);
        }
        return iDAddress.get();
    }

    public boolean checkAddress(String nameStreet, int number_house){
        AtomicBoolean checkAddress = new AtomicBoolean(false);
        AtomicReference<Address> address = new AtomicReference<>(new Address());
        namedParameterJdbcTemplate.query(sqlSelectCheckAddress, Map.of("Street", nameStreet,
                "Number_house", number_house), rs -> {
            address.set(getAddress(rs));
            if (address.get().getStreet().equalsIgnoreCase(nameStreet) &&
                    number_house == address.get().getNumber_house()) {
                checkAddress.set(true);
            }
        });
        return checkAddress.get();
    }
}