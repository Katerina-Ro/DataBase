package task5.Enteties;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Entity
@Table(name = "District")
@Getter
@Setter
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int idDistrict;

    @Column(name = "Name_district")
    @NotBlank
    private String nameDistrict;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "Id")
    private Collection<Address> listAddress;

    @Override
    public String toString() {
        return " номер района = " + idDistrict +
                ", название района = '" + nameDistrict + '\'' +
                ", список адресов, прикрепленных к району, = " + listAddress +
                '}';
    }
}
