package kte.labs.task.entities;

import kte.labs.task.dto.ProductDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Entity
@NoArgsConstructor
@Table(name="products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "description")
    private String description;

    @Column(name = "discount")
    private long discount;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Rating> rating;

    public static List<ProductDto> listDto(List<Product> list){
        return list.stream()
                .map(P -> new ProductDto(P.id, P.title, P.price.longValue(), P.description, P.discount))
                .collect(Collectors.toList());
    }

    public static Map<Long, Product> makeMap(List<Product> list){
        Map<Long, Product> map = new HashMap<>();
        list.forEach(P -> map.put(P.id, P));
        return map;
    }
}
