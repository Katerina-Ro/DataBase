package task5.Repository;

import task5.Enteties.Child;
import task5.Enteties.Parent;
import task5.Repository.util.UtilParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class ParentRepositoryImpl implements ParentRepository{

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private AddressRepository addressRepository;
    private DistrictRepository districtRepository;
    private ChildRepository childRepository;

    private UtilParentRepository utilParentRepository;

    @Value("INSERT into Parent (Full_name, IdAddress) VALUES (:Full_name, :IdAddress)")
    private String sqlInsertParent;

    @Value("SELECT Id, Full_name, IdAddress FROM Parent WHERE Id = :Id")
    private String sqlSelectParent;

    @Autowired
    public ParentRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Autowired
    public void setUtilParentRepository(UtilParentRepository utilParentRepository) {
        this.utilParentRepository = utilParentRepository;
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
    public void setChildRepository(ChildRepository childRepository) {
        this.childRepository = childRepository;
    }

    @Override
    public void createParent(String fullName, String nameDistrict, String nameStreet, int numberHouse) {
        // проверяем, есть ли такой человек в БД. Если нет, заносим. Если есть, ничего не делаем
        if(!utilParentRepository.checkParentName(fullName)){
            namedParameterJdbcTemplate.update(sqlInsertParent, Map.of("Full_name", fullName, "IdAddress",
                    addressRepository.createAddress(districtRepository.createDistrict(nameDistrict).getNameDistrict(),
                            nameStreet, numberHouse).getIDAddress()));
        }
    }

    @Override
    public void createParentChild(String fullName, String nameDistrict, String nameStreet, int numberHouse,
                             Collection<Child> children) {
        // заносим родителя в БД
        if(!utilParentRepository.checkParentName(fullName)) {
            createParent(fullName, nameDistrict, nameStreet, numberHouse);
        }
            childRepository.insertChildren(fullName, nameDistrict, nameStreet, numberHouse, children);
    }

    @Override
    public void updateParentChild (String fullname, String nameDistrict, String nameStreet, int numberHouse,
                                   Collection<Child> children) {
        if(utilParentRepository.checkParentName(fullname)) {
            childRepository.insertChildren(fullname, nameDistrict, nameStreet, numberHouse, children);
        }
    }

    @Override
    public Parent getParent(int parentId){
        /*
         Класс java.util.concurrent.atomic.AtomicReference предоставляет операции над базовой ссылкой на объект, которые можно читать и записывать
         атомарно, а также содержит расширенные атомарные операции. AtomicReference поддерживает атомарные операции
         с базовой переменной объекта
         */
        AtomicReference<Parent> parent = new AtomicReference<>(new Parent());
        namedParameterJdbcTemplate.query(sqlSelectParent, Map.of("Id", parentId), rs -> {
            parent.set(utilParentRepository.getParent(rs));
        });
        return parent.get();
    }
}