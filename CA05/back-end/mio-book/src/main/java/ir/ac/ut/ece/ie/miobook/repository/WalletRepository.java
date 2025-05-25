package ir.ac.ut.ece.ie.miobook.repository;

import ir.ac.ut.ece.ie.miobook.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {

    // Find wallet by customer username
    Optional<Wallet> findByUsername(String username);

    // Check if wallet exists for customer username
    boolean existsByUsername(String username);
}