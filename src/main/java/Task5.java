import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import task5.Enteties.Child;
import task5.Enteties.Parent;
import task5.Repository.*;
import task5.Service.ChangeAddress;
import task5.Service.ChangeAddressImpl;

import java.util.HashSet;
import java.util.Set;

public class Task5 {
    public static void main (String [] args){

        ApplicationContext context =
                new AnnotationConfigApplicationContext("task5");

        ParentRepository parent = (ParentRepositoryImpl)context.getBean("parentRepositoryImpl");
        ChildRepository child = (ChildRepositoryImpl) context.getBean("childRepositoryImpl");
        Educational_establishmentRepository ee =
                (Educational_establishmentRepositoryImpl)context.getBean("educational_establishmentRepositoryImpl");
        ChangeAddress changeAddress = (ChangeAddressImpl) context.getBean("changeAddressImpl");

        // добавление взрослого без ребенка
        parent.createParent("Чешко Вика", "Октябрьский", "Чкалова", 20);

        Set<Parent> setParents = new HashSet<>();
        setParents.add(parent.getParent(34));
        child.createChild("Чешко Артем", setParents,10);

        parent.createParent("Коркина Светлана", "Кировский", "Мичурина", 49);

        // добавление взрослого с ребенком
        Set<Child> setChildren = new HashSet<>();
        setChildren.add(new Child("Коркина Елена", 3));
        parent.createParentChild("Коркин Игорь", "Кировский", "Мичурина",
                49, setChildren);

        // обновление информации о родителе: добавление ребенка
        setChildren.add(new Child("Коркин Иван", 1));
        parent.updateParentChild("Коркина Светлана", "Кировский", "Мичурина",
                49, setChildren);

        // смена адреса и смена списка учебных учреждений
        ee.createEducationalEstablishment("Кировский",113, "Шарташская", 11);
        ee.createEducationalEstablishment("Ленинский",215, "Васильковая", 30);

        changeAddress.changeAddressAndEducationalEstablishment("Коркина Светлана", "Кировский",
                "Мичурина", 49,"Ленинский", "Первомайская",
                60);
    }
}
