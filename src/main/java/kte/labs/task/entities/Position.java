package kte.labs.task.entities;

import kte.labs.task.dto.PositionDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Data
@Entity
@NoArgsConstructor
@Table(name="positions")
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "quantity")
    private long quantity;

    @Column(name = "original_price")
    private BigDecimal originalPrice;

    @Column(name = "final_price")
    private BigDecimal finalPrice;

    @Column(name = "final_discount")
    private long finalDiscount;

    @ManyToOne
    @JoinColumn(name = "sales_id")
    private Sale sale;

    public Position(long productId, long quantity, BigDecimal originalPrice, BigDecimal finalPrice, long finalDiscount) {
        this.productId = productId;
        this.quantity = quantity;
        this.originalPrice = originalPrice;
        this.finalPrice = finalPrice;
        this.finalDiscount = finalDiscount;
    }

    public static List<PositionDto> listDto(List<Position> list){
        return list.stream()
                .map(P -> new PositionDto(
                        P.id,
                        P.productId,
                        P.quantity,
                        P.originalPrice.longValue(),
                        P.finalPrice.longValue(),
                        P.finalDiscount))
                .collect(Collectors.toList());
    }
}
