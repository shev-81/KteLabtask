package kte.labs.task.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PositionDto {
    private Long id;
    private long productId;
    private long quantity;
    private long originalPrice;
    private long finalPrice;
    private long finalDiscount;

    public PositionDto(Long id, long productId, long quantity, long originalPrice, long finalPrice, long finalDiscount) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.originalPrice = originalPrice;
        this.finalPrice = finalPrice;
        this.finalDiscount = finalDiscount;
    }
}
