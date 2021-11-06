package task5.Enteties;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Address")
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "Id")
    private int iDAddress;

    @Column (name = "IdDistrict")
    @NotNull
    private int district;

    @Column (name = "Street")
    @NotBlank
    private String street;

    @Column (name = "Number_house")
    @NotNull
    private int number_house;

    @OneToOne (optional = false, mappedBy="Address")
    private EducationalEstablishment educationalEstablishment;

    @Override
    public String toString() {
        return "Адрес: " +
                "номер района = " + district +
                ", улица = '" + street + '\'' +
                ", номер дома = " + number_house;
    }
}
