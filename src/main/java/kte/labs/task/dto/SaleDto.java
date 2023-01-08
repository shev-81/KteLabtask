package kte.labs.task.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class SaleDto {
    private long id;
    private long clientId;
    private Date dateSale;
    private long receiptNumber;
    private List<PositionDto> positions;

    public SaleDto(long id, long clientId, Date dateSale, long receiptNumber, List<PositionDto> positions) {
        this.id = id;
        this.clientId = clientId;
        this.dateSale = dateSale;
        this.receiptNumber = receiptNumber;
        this.positions = positions;
    }
}
