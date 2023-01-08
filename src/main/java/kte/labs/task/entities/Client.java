package kte.labs.task.entities;

import kte.labs.task.dto.ClientDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Entity
@Table(name="clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "individual_discount_1")
    private long individualDiscount1;

    @Column(name = "individual_discount_2")
    private long individualDiscount2;

    public static List<ClientDto> listDto(List<Client> list){
        return list.stream()
                .map(C -> new ClientDto(C.id, C.name, C.individualDiscount1, C.individualDiscount2))
                .collect(Collectors.toList());
    }
}
