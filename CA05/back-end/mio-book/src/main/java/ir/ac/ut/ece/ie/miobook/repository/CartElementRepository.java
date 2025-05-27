package ir.ac.ut.ece.ie.miobook.repository;

import ir.ac.ut.ece.ie.miobook.model.CartElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartElementRepository extends JpaRepository<CartElement, Long> {
    List<CartElement> findByUserUsername(String userUsername);
    void deleteByUserUsername(String userUsername);

}