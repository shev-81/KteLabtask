package kte.labs.task.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductInfoDto {
    private String description;
    private long currentRating;

    public ProductInfoDto(String description, long currentRating) {
        this.description = description;
        this.currentRating = currentRating;
    }
}
