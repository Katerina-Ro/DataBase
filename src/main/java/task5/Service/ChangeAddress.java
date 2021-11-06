package task5.Service;

public interface ChangeAddress {
    /**
     * Обновление/ изменение информации о родителе: смена адреса
     * @param fullname - ФИО
     * @param nameDistrict - название района
     * @param nameStreet - улица
     * @param numberHouse - номер дома
     * @param newNameDistrict - название нового района
     * @param newNameStreet - название новой улицы
     * @param newNumberHouse - номер нового дома
     */
    void changeAddressAndEducationalEstablishment (String fullname, String nameDistrict, String nameStreet, int numberHouse,
                        String newNameDistrict, String newNameStreet, int newNumberHouse);
}
