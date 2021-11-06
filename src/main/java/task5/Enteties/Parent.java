package task5.Enteties;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Entity
@Table (name = "Parent")
@Getter
@Setter
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private int idParent;

    @Column(name = "Full_name")
    @NotBlank // @NotBlank применяется только к строкам и проверяет, что строка не пуста; не пропустит строку,
              // состоящую из 6 пробелов и/или символов переноса строки
    private String fullName;

    @Column(name = "IdAddress")
    @NotBlank
    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "Id")
    private Address iDAddress;

    @ManyToMany
    @JoinTable(name = "child_parent",  joinColumns = @JoinColumn (name="parent_id"),
            inverseJoinColumns=@JoinColumn(name="child_id"))
    // Связь @ManyToMany с обоих сторон представлена коллекцией объектов. Так как напрямую в реляционных
    // базах данных такая связь не поддерживается, JPA реализует её с помощью промежуточной таблицы, которая описывается
    // аннотацией @JoinTable у объекта владельца. Параметр name задаёт имя промежуточной таблицы, joinColumns — имя
    // столбца, связывающего с классом владельцем, inverseJoinColumns — имя столбца, связывающего с владеемым классом.
    private Collection<Child> iDChild;

    @Override
    public String toString() {
        return "Родитель " +
                "ФИО = '" + fullName + '\'' +
                ", " + iDAddress +
                ", дети = " + iDChild +
                '}';
    }
}