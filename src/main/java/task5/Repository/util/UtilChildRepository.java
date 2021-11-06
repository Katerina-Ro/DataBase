package task5.Repository.util;

import org.springframework.stereotype.Repository;
import task5.Enteties.Child;
import task5.Enteties.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import task5.Repository.ParentRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Вспомогательный класс для работы с объектом Child
 */
@Repository
public class UtilChildRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private ParentRepository parentRepository;

    @Value("SELECT Id, Full_name, Age FROM Child WHERE Full_name = :Full_name AND Age = :Age")
    private String sqlSelectCheckChild;

    @Value("SELECT child_id, parent_id From child_parent WHERE child_id = :child_id")
    private String sqlSelestChild_parent;

    @Autowired
    public UtilChildRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Autowired
    public void setParentRepository(ParentRepository parentRepository) {
        this.parentRepository = parentRepository;
    }

    // Проверяем, если ли ребенок в БД
    public boolean checkChildName(String full_name, Collection<Parent> parents, int age) {
        AtomicBoolean checkChildName = new AtomicBoolean(false);
        AtomicReference<Child> childFromDB = new AtomicReference<>(new Child());
        namedParameterJdbcTemplate.query(sqlSelectCheckChild, Map.of("Full_name", full_name,"Age", age),
                rs -> {
                    childFromDB.set(getChild(rs));
                    if(childFromDB.get().getFullName().equalsIgnoreCase(full_name) && childFromDB.get().getAge() == age) {
                        checkChildName.set(true);
                    }
                });
        return checkChildName.get();
    }

    public boolean chekChild_parent(String nameChild, int age, String fullNameParent){
        AtomicBoolean chekChild_parent = new AtomicBoolean(false);
        AtomicReference<Parent> parentFromDB = new AtomicReference<>(new Parent());
        namedParameterJdbcTemplate.query(sqlSelestChild_parent, Map.of("child_id", getIdChild(nameChild, age)),
                rs -> {
                    parentFromDB.set(parentRepository.getParent(rs.getInt("parent_id")));
                    if(parentFromDB.get().getFullName().equalsIgnoreCase(fullNameParent)){
                        chekChild_parent.set(true);
                    }
                });
        return chekChild_parent.get();
    }

    // Получаем объект Child через ResultSet из БД
    public Child getChild(ResultSet rs) throws SQLException {
        Child child = new Child();
        child.setIdChild(rs.getInt("Id"));
        child.setFullName(rs.getString("Full_name"));
        child.setAge(rs.getInt("Age"));
        return child;
    }

    public Integer getIdChild(String fullName, int age) {
        AtomicInteger idChild = new AtomicInteger();
            namedParameterJdbcTemplate.query(sqlSelectCheckChild, Map.of("Full_name", fullName, "Age", age),
                    rs -> {
                idChild.set(getChild(rs).getIdChild());
            });
        return idChild.get();
    }
}


