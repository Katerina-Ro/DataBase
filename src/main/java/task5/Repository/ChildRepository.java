package task5.Repository;

import task5.Enteties.Child;
import task5.Enteties.EducationalEstablishment;
import task5.Enteties.Parent;

import java.util.Collection;

public interface ChildRepository {

    /**
     * Заносим информацию о ребенке в БД
     * @param fullName - ФИО
     * @param parents - родители в формате коллекции
     * @param age - возраст
     */
    void insertChild (String fullName, Collection<Parent> parents, int age);

    /**
     * Заносим в базу список детей
     * @param fullName - ФИО родителя
     * @param nameDistrict - наименование района, в котором живет родитель
     * @param nameStreet - улица
     * @param numberHouse - номер дома
     * @param children - список детей
     */
    void insertChildren(String fullName, String nameDistrict, String nameStreet, int numberHouse,
                       Collection<Child> children);

    /**
     * Заносим ребенка в БД и получаем список учреждений, куда ребенок может поступить
     * @param fullName - ФИО
     * @param parents - родители в формате коллекции
     * @param age - возраст
     * @return - список образовательных учреждений
     */
    Collection<EducationalEstablishment> createChild(String fullName, Collection<Parent> parents, int age);

    /**
     * Получаем информацию о ребенка по его ID в БД
     * @param childId - ID ребенка из БД
     * @return - информация о ребенке в виде объекта Child
     */
    Child getChild(int childId);
}
