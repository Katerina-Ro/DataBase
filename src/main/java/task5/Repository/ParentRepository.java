package task5.Repository;

import task5.Enteties.Child;
import task5.Enteties.Parent;

import java.util.Collection;

public interface ParentRepository {

    /**
     * Занесение родителя в БД без указания детей
     * @param fullName - ФИО
     * @param nameDistrict - название района
     * @param nameStreet - улица
     * @param numberHouse - номер дома
     */
    void createParent(String fullName, String nameDistrict, String nameStreet, int numberHouse);

    /**
     * Занесение родителя в БД сразу с детьми
     * @param fullName - ФИО
     * @param nameDistrict - название района
     * @param nameStreet - улица
     * @param numberHouse - номер дома
     * @param children - список детей
     */
    void createParentChild(String fullName, String nameDistrict, String nameStreet, int numberHouse,
                      Collection<Child> children);

    /**
     * Обновление информации о родителе: внесение в базу детей
     * @param fullname - ФИО родителя
     * @param nameDistrict - наименование района, в котором проживает родитель
     * @param nameStreet - улица
     * @param numberHouse - номер дома
     * @param children - список детей
     */
    void updateParentChild (String fullname, String nameDistrict, String nameStreet, int numberHouse,
                            Collection<Child> children);

    /**
     * Получить информацию о родиетелй по его Id
     * @param parentId - Id родителя из БД
     * @return - объект Parent (информация о родиетеле)
     */
    Parent getParent(int parentId);
}
