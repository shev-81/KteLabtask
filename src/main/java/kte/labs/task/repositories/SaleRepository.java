package kte.labs.task.repositories;

import kte.labs.task.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("select s from Sale s where s.clientId = ?1")
    List<Sale> findAllByClientId(long clientId);

    @Query(nativeQuery = true, value = "SELECT MAX(receipt_number) FROM sales")
    long findByMaxId();
}
