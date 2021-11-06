package task5.Repository;

import task5.Enteties.Address;
import task5.Enteties.EducationalEstablishment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import task5.Repository.util.UtilDistrictRepository;
import task5.Repository.util.UtilEducationalEstablishmentRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class Educational_establishmentRepositoryImpl implements Educational_establishmentRepository{

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private UtilEducationalEstablishmentRepository utilEducationalEstablishmentRepository;
    private AddressRepository addressRepository;
    private UtilDistrictRepository utilDistrictRepository;

    @Value("SELECT Number, IdAddress FROM Educational_establishment")
    private String sqlSelectEE;

    @Value("SELECT Number, IdAddress FROM Educational_establishment WHERE Number = :Number")
    private String sqlSelectEEFromNumber;

    @Value("INSERT into Educational_establishment (Number, IdAddress) VALUES (:Number, :IdAddress)")
    private String sqlInsertEE;

    @Autowired
    public Educational_establishmentRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Autowired
    public void setUtilEducationalEstablishmentRepository(UtilEducationalEstablishmentRepository utilEducationalEstablishmentRepository) {
        this.utilEducationalEstablishmentRepository = utilEducationalEstablishmentRepository;
    }

    @Autowired
    public void setUtilDistrictRepository(UtilDistrictRepository utilDistrictRepository) {
        this.utilDistrictRepository = utilDistrictRepository;
    }

    @Autowired
    public void setAddressRepository(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Collection<EducationalEstablishment> getEducational_establishment(String nameDistrict) {
        List<EducationalEstablishment> listEducationalEstablishment = new ArrayList<>();
        namedParameterJdbcTemplate.query(sqlSelectEE, rs ->{
            EducationalEstablishment ee = utilEducationalEstablishmentRepository.getEducationalEstablishment(rs);
            if (nameDistrict.equals(utilDistrictRepository.getNameDistrict(ee.getAddress().getDistrict()))){
                listEducationalEstablishment.add(ee);
            }
        });
        return listEducationalEstablishment;
    }

    @Override
    public EducationalEstablishment  getEducational_establishment(int idEducational_establishment){
        AtomicReference<EducationalEstablishment> educationalEstablishment  = new AtomicReference<>(new EducationalEstablishment());
        namedParameterJdbcTemplate.query(sqlSelectEEFromNumber, Map.of("Number", idEducational_establishment), rs -> {
            educationalEstablishment.set(utilEducationalEstablishmentRepository.getEducationalEstablishment(rs));
        });
        return educationalEstablishment.get();
    }

    @Override
    public void createEducationalEstablishment (String nameDistrict, int numberEE, String nameStreet, int numberHouse){
        if (!utilEducationalEstablishmentRepository.checkEducationalEstablishment(nameStreet, numberHouse)){
            namedParameterJdbcTemplate.update(sqlInsertEE, Map.of("Number", numberEE, "IdAddress",
                    addressRepository.createAddress(nameDistrict, nameStreet, numberHouse).getIDAddress()));
        }
    }
}
