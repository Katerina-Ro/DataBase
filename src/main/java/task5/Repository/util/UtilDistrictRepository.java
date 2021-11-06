package task5.Repository.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import task5.Enteties.District;
import task5.Repository.DistrictRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Вспомогательный класс для работы с District
 */
@Repository
public class UtilDistrictRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private DistrictRepository districtRepository;

    @Value("SELECT Id, Name_district FROM District WHERE Id = :Id")
    private String sqlSelectDistrictName;

    @Value("SELECT Id, Name_district FROM District WHERE Name_district = :Name_district")
    private String sqlSelectDistrictId;

    @Autowired
    public UtilDistrictRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Autowired
    public void setDistrictRepository(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    public District getDistrict(ResultSet rs) throws SQLException {
        District districtResultSet = new District();
        districtResultSet.setIdDistrict(rs.getInt("Id"));
        districtResultSet.setNameDistrict((rs.getString("Name_district")));
        return districtResultSet;
    }

    public String getNameDistrict(int idDistrict){
        AtomicReference<String> nameDistrict = new AtomicReference<>();
        namedParameterJdbcTemplate.query(sqlSelectDistrictName, Map.of("Id", idDistrict), rs -> {
            nameDistrict.set(getDistrict(rs).getNameDistrict());
        });
        return nameDistrict.get();
    }

    public int getIdDistrict (String nameDistrict) {
        return districtRepository.createDistrict(nameDistrict).getIdDistrict();
    }

    public boolean checkDistrict(String nameDistrict){
        AtomicBoolean checkDistrict = new AtomicBoolean(false);
        AtomicReference<District> district = new AtomicReference<>(new District());
        namedParameterJdbcTemplate.query(sqlSelectDistrictId, Map.of("Name_district", nameDistrict), rs -> {
            district.set(getDistrict(rs));
            if (district.get().getNameDistrict().equalsIgnoreCase(nameDistrict)){
                checkDistrict.set(true);
            }
        });
        return checkDistrict.get();
    }
}
