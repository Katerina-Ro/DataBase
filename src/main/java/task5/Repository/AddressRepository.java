package task5.Repository;

import task5.Enteties.Address;

import java.util.Collection;

public interface AddressRepository {

    /**
     * Занесение адреса в БД
     * @param nameDistrict - название района, к которому относится адрес
     * @param Street - улица
     * @param Number_house - номер дома
     * @return - возвращается объект Address (описание адреса в виде объекта)
     */
    Address createAddress (String nameDistrict, String Street, int Number_house);

    /**
     * Получаем список адресов, относящихся к конкретному району
     * @param nameDictrict - название района, по которому нужно получить список адресов
     * @return - получаем список адресов
     */
    Collection<Address> getListAddress (String nameDictrict);

    /**
     * Получаем конкретный адрес по его номеру из БД
     * @param idAddress - номер адреса в БД
     * @return - получаем адрес в формате объекта Address
     */
    Address getAddress(int idAddress);
}
