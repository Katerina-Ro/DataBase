package task5.Repository;

import task5.Enteties.EducationalEstablishment;

import java.util.Collection;

public interface Educational_establishmentRepository {

    /**
     * Получаем список учреждений, прикрепленных к конкретному району
     * @param nameDistrict - название района, по которому нужно получить список учреждений
     * @return - список учреждений
     */
    Collection<EducationalEstablishment> getEducational_establishment(String nameDistrict);

    /**
     * Получаем учреждение по его ID в БД
     * @param idEducational_establishment - Id учреждений из БД
     * @return описание учреждения в формате объекта EducationalEstablishment
     */
    EducationalEstablishment  getEducational_establishment(int idEducational_establishment);

    /**
     * Заносим учреждение в БД
     * @param nameDistrict - наименование района
     * @param numberEE - номер учреждения
     * @param nameStreet - название улицы
     * @param numberHouse - номер дома
     */
    void createEducationalEstablishment (String nameDistrict, int numberEE, String nameStreet, int numberHouse);
}
