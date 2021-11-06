package task5.Enteties;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Table(name = "Child")
@Getter
@Setter
public class Child {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private int idChild;

    @Column(name = "Full_name")
    @NotBlank
    private String fullName;

    //@Column(name = "IdParent")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "child_parent",  joinColumns = @JoinColumn (name="child_id"),
            inverseJoinColumns=@JoinColumn(name="parent_id"))
    // Связь @ManyToMany с обоих сторон представлена коллекцией объектов. Так как напрямую в реляционных
    // базах данных такая связь не поддерживается, JPA реализует её с помощью промежуточной таблицы, которая описывается
    // аннотацией @JoinTable у объекта владельца. Параметр name задаёт имя промежуточной таблицы, joinColumns — имя
    // столбца, связывающего с классом владельцем, inverseJoinColumns — имя столбца, связывающего с владеемым классом.
    private Collection<Parent> parents;

    @Column(name = "Age")
    @NotNull
    private int age;

    //@Column(name = "NumberEducational_establishment")
    @ManyToMany
    @JoinTable(name =  "childee", joinColumns = @JoinColumn(name = "IdChildEE"),
            inverseJoinColumns = @JoinColumn(name = "IdEducationalEstablishment"))
    @NotBlank
    private Collection<EducationalEstablishment> educational_establishment;

    public Child(){}

    public Child(@NotBlank String fullName, @NotNull int age) {
        this.fullName = fullName;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Ребенок: " +
                "ФИО: '" + fullName + '\'' +
                ", родители: " + parents +
                ", возраст: " + age +
                ", Образовательные учреждения, куда может поступить: " + educational_establishment;
    }
}