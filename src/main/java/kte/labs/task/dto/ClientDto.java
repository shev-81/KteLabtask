package kte.labs.task.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientDto {
    private Long id;
    private String name;
    private long individualDiscount1;
    private long individualDiscount2;

    public ClientDto(Long id, String name, long individualDiscount1, long individualDiscount2) {
        this.id = id;
        this.name = name;
        this.individualDiscount1 = individualDiscount1;
        this.individualDiscount2 = individualDiscount2;
    }
}
