package task5.Repository;

import task5.Enteties.District;

public interface DistrictRepository {

    /**
     * Заносим район в БД
     * @param name - название района
     * @return - получаем объект типа District
     */
    District createDistrict (String name);

    /**
     * Получаем информацию о районе по ID из БД
     * @param districtId - ID района из БД
     * @return информацию о районе (наименование, номер района)
     */
    public District getDistrict(int districtId);
}
