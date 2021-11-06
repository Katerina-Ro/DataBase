package task5.Enteties;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Entity
@Table(name = "Educational_establishment")
@Getter
@Setter
public class EducationalEstablishment {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "Number", nullable=false)
    private int number;

    @Column(name = "IdAddress")
    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "IdAddress")
    @NotBlank
    private Address address;

    @ManyToMany
    @JoinTable(name =  "childee", joinColumns = @JoinColumn(name = "IdEducationalEstablishment"),
            inverseJoinColumns = @JoinColumn(name = "IdChildEE"))
    @NotBlank
    private Collection<Child> children;

    @Override
    public String toString() {
        return "Образовательное учреждение " +
                "№ " + number +
                ", адрес: " + address;
    }
}