package task5.Repository;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import task5.Enteties.District;
import task5.Repository.util.UtilDistrictRepository;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class DistrictRepositoryImpl implements DistrictRepository{

    @Getter
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private UtilDistrictRepository utilDistrict;

    @Value("INSERT into District (Name_district) VALUES (:Name_district)")
    private String sqlInsertDistrict;

    @Value("SELECT Id, Name_district FROM District WHERE Name_district = :Name_district")
    private String sqlSelectDistrictId;

    @Value("SELECT Id, Name_district FROM District WHERE Id = :Id")
    private String sqlSelectDistrict;

    @Autowired
    public DistrictRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Autowired
    public void setUtilDistrict(UtilDistrictRepository utilDistrict) {
        this.utilDistrict = utilDistrict;
    }

    @Override
    public District createDistrict (String name) {
        AtomicReference<District> district = new AtomicReference<>(new District());
        if(!utilDistrict.checkDistrict(name)) {
            namedParameterJdbcTemplate.update(sqlInsertDistrict, Map.of("Name_district", name));
        }
        namedParameterJdbcTemplate.query(sqlSelectDistrictId, Map.of("Name_district", name), rs -> {
            district.set(utilDistrict.getDistrict(rs));
        });
        return district.get();
    }

    @Override
    public District getDistrict(int districtId) {
       AtomicReference<District> districtResultSet = new AtomicReference<>(new District());
        namedParameterJdbcTemplate.query(sqlSelectDistrict, Map.of("Id", districtId), rs -> {
            districtResultSet.set(utilDistrict.getDistrict(rs));
        });
        return districtResultSet.get();
    }
}
