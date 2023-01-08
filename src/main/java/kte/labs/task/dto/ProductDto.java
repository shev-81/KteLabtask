package kte.labs.task.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String title;
    private Long price;
    private String description;
    private Long discount;

    public ProductDto(Long id, String title, Long price, String description, Long discount) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.discount = discount;
    }
}
