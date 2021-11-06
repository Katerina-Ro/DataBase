package task5.Repository;

import task5.Enteties.Address;
import task5.Enteties.Child;
import task5.Enteties.EducationalEstablishment;
import task5.Enteties.Parent;
import task5.Repository.util.UtilAddressRepository;
import task5.Repository.util.UtilChildRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import task5.Repository.util.UtilDistrictRepository;
import task5.Repository.util.UtilParentRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class ChildRepositoryImpl implements ChildRepository{

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private Educational_establishmentRepositoryImpl educational_establishmentRepository;

    private UtilChildRepository utilChildRepository;
    private UtilParentRepository utilParentRepository;
    private UtilDistrictRepository utilDistrictRepository;
    private UtilAddressRepository utilAddressRepository;

    @Value("INSERT into Child (Full_name, Age) VALUES (:Full_name, :Age)")
    private String sqlInsertChild;

    @Value("SELECT Id, Full_name,IdParent, Age, NumberEducational_establishment FROM Child WHERE Full_name = :Full_name AND IdParent = :IdParent AND Age = :Age")
    private String sqlSelectChild;

    @Value("INSERT into child_parent (child_id, parent_id) VALUES (:child_id, :parent_id)")
    private String sqlInsertChild_Parent;

    @Autowired
    public ChildRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Autowired
    public void setUtilChildRepository(UtilChildRepository utilChildRepository) {
        this.utilChildRepository = utilChildRepository;
    }

    @Autowired
    public void setEducational_establishmentRepository(Educational_establishmentRepositoryImpl
                                                                   educational_establishmentRepository) {
        this.educational_establishmentRepository = educational_establishmentRepository;
    }

    @Autowired
    public void setUtilParentRepository(UtilParentRepository utilParentRepository) {
        this.utilParentRepository = utilParentRepository;
    }

    @Autowired
    public void setUtilDistrictRepository(UtilDistrictRepository utilDistrictRepository) {
        this.utilDistrictRepository = utilDistrictRepository;
    }

    @Autowired
    public void setUtilAddressRepository(UtilAddressRepository utilAddressRepository) {
        this.utilAddressRepository = utilAddressRepository;
    }

    @Override
    public Collection<EducationalEstablishment> createChild(String fullName, Collection<Parent> parents, int age) {
        insertChild(fullName, parents, age);
        Set<EducationalEstablishment> setEE = new HashSet<>();
        for (Parent p : parents) {
            setEE.addAll(educational_establishmentRepository.getEducational_establishment(utilDistrictRepository
                    .getNameDistrict(p.getIDAddress().getDistrict())));
        }
        return setEE;
    }

    @Override
    public void insertChild (String fullNameChild, Collection<Parent> parents, int age) {
        // Проверяем, есть ли ребенок в БД. Если нет, то заносиим
        if (!utilChildRepository.checkChildName(fullNameChild, parents, age)) {
            namedParameterJdbcTemplate.update(sqlInsertChild, Map.of("Full_name", fullNameChild, "Age", age));
        }
        // В цикле перебираем ID родителя и для каждого ID родителя заносим информацию о ребенке
        for (Parent p : parents) {
            if(!utilChildRepository.chekChild_parent(fullNameChild, age, p.getFullName())) {
                // в таблице отношений "ребенок-родитель" (связь Many-To-Many) создаем запись отношений через Id.
                // Если детей несколько, то таких записей в таблице будет несколько
                namedParameterJdbcTemplate.update(sqlInsertChild_Parent, Map.of("child_id",
                        utilChildRepository.getIdChild(fullNameChild, age),
                        "parent_id", utilParentRepository.getIdParent(p.getFullName(),
                                p.getIDAddress(), utilDistrictRepository.getNameDistrict(p.getIDAddress()
                                        .getDistrict()))));
            }
        }
    }

    @Override
    public void insertChildren(String parentFullName, String nameDistrict, String nameStreet, int numberHouse,
                              Collection<Child> children){
        // Получаем адрес из БД
        Address address  = utilAddressRepository.getAddress(nameDistrict, nameStreet, numberHouse);

        // чтобы занести детей в БД, нужно родителей/ родителя перевести в формат списка
        Set<Parent> parentSet = new HashSet<>();
        parentSet.add(utilParentRepository.getParent(parentFullName, address));

        // т.к. детей тоже может быть несколько, то в цикле перебираем список детей и заносим их в БД
        for (Child child : children) {
            // заносим ребенка в БД
            insertChild(child.getFullName(), parentSet, child.getAge());
        }
    }

    @Override
    public Child getChild(int childId){
        AtomicReference<Child> child = new AtomicReference<>(new Child());
        namedParameterJdbcTemplate.query(sqlSelectChild, Map.of("Id", childId), rs -> {
            child.set(utilChildRepository.getChild(rs));
        });
        return child.get();
    }
}

