package task5.Repository.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import task5.Enteties.EducationalEstablishment;
import task5.Repository.AddressRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Repository
public class UtilEducationalEstablishmentRepository {

    private AddressRepository addressRepository;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private UtilAddressRepository utilAddressRepository;

    @Getter
    @Value("SELECT Number, IdAddress FROM Educational_establishment WHERE IdAddress = :IdAddress")
    private String sqlSelectEE;

    @Autowired
    public UtilEducationalEstablishmentRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Autowired
    public void setAddressRepository(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Autowired
    public void setUtilAddressRepository(UtilAddressRepository utilAddressRepository) {
        this.utilAddressRepository = utilAddressRepository;
    }

    public EducationalEstablishment getEducationalEstablishment(ResultSet rs) throws SQLException {
        EducationalEstablishment educationalEstablishment = new EducationalEstablishment();
        educationalEstablishment.setNumber(rs.getInt("Number"));
        educationalEstablishment.setAddress(addressRepository.getAddress(rs.getInt("IdAddress")));
        return educationalEstablishment;
    }

    public boolean checkEducationalEstablishment (String nameStreet, int numberHouse){
        AtomicBoolean checkEE = new AtomicBoolean(false);
        namedParameterJdbcTemplate.query(sqlSelectEE, Map.of("IdAddress", utilAddressRepository
                .getIdAddress(nameStreet,numberHouse)), rs -> {
            checkEE.set(utilAddressRepository.getIdAddress(nameStreet, numberHouse) != 0);
        });
        return checkEE.get();
    }
}