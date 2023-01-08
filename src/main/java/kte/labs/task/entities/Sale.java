package kte.labs.task.entities;

import kte.labs.task.dto.PositionDto;
import kte.labs.task.dto.SaleDto;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@NoArgsConstructor
@Table(name="sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "client_id")
    private long clientId;

    @Column(name = "date_sale")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSale;

    @Column(name = "receipt_number")
    private long receiptNumber;

    @OneToMany(mappedBy = "sale", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Position> positions;


    public Sale(long clientId, Date dateSale, long receiptNumber) {
        this.clientId = clientId;
        this.dateSale = dateSale;
        this.positions = new ArrayList<>();
        this.receiptNumber = receiptNumber;
    }

    public static List<SaleDto> listDto(List<Sale> list){
        return list.stream()
                .map(S -> new SaleDto(S.id, S.clientId, S.dateSale, S.receiptNumber, Position.listDto(S.positions)))
                .collect(Collectors.toList());
    }

    public static List<PositionDto> listPositions(List<Sale> sales){
        List<PositionDto> list = new ArrayList<>();
        for(Sale S: sales){
            list.addAll(Position.listDto(S.getPositions()));
        }
        return list;
    }
}
