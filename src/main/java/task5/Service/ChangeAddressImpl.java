package task5.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import task5.Repository.AddressRepository;
import task5.Repository.Educational_establishmentRepository;
import task5.Repository.util.UtilAddressRepository;
import task5.Repository.util.UtilDistrictRepository;
import task5.Repository.util.UtilParentRepository;

import java.util.Map;

@Repository
public class ChangeAddressImpl implements ChangeAddress{

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private AddressRepository addressRepository;
    private Educational_establishmentRepository educational_establishmentRepository;

    private UtilDistrictRepository utilDistrictRepository;
    private UtilParentRepository utilParentRepository;
    private UtilAddressRepository utilAddressRepository;

    @Value("UPDATE Parent Set IdAddress = :IdAddress WHERE Id = :Id")
    private String sqlUpdateAddress_Parent;

    @Autowired
    public ChangeAddressImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Autowired
    public void setAddressRepository(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Autowired
    public void setEducational_establishmentRepository(Educational_establishmentRepository
                                                                   educational_establishmentRepository) {
        this.educational_establishmentRepository = educational_establishmentRepository;
    }

    @Autowired
    public void setUtilDistrictRepository(UtilDistrictRepository utilDistrictRepository) {
        this.utilDistrictRepository = utilDistrictRepository;
    }

    @Autowired
    public void setUtilParentRepository(UtilParentRepository utilParentRepository) {
        this.utilParentRepository = utilParentRepository;
    }

    @Autowired
    public void setUtilAddressRepository(UtilAddressRepository utilAddressRepository) {
        this.utilAddressRepository = utilAddressRepository;
    }

    @Override
    public void changeAddressAndEducationalEstablishment (String fullname, String nameDistrict, String nameStreet,
                                                          int numberHouse, String newNameDistrict,
                                                          String newNameStreet, int newNumberHouse) {
        int newIdaddress = addressRepository.createAddress(newNameDistrict, newNameStreet, newNumberHouse).getIDAddress();
        namedParameterJdbcTemplate.update(sqlUpdateAddress_Parent, Map.of("IdAddress", newIdaddress,
                "Id", utilParentRepository.getIdParent(fullname, utilAddressRepository
                        .getAddress(nameDistrict, nameStreet, numberHouse), nameDistrict)));
        System.out.println("""
                Адрес изменен.\s
                Список учреждений, в которые может быть записан Ваш ребенок:\s
                """ +  educational_establishmentRepository.getEducational_establishment(utilDistrictRepository
                .getNameDistrict(newIdaddress)));
    }
}
