package kte.labs.task.entities;

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

@Data
@Entity
@NoArgsConstructor
@Table(name="ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "client_id")
    private long clientId;

    @Column(name = "evaluation")
    private long evaluation;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Rating(long clientId, long evaluation, Product product) {
        this.clientId = clientId;
        this.evaluation = evaluation;
        this.product = product;
    }
}
