package ir.ac.ut.ece.ie.miobook.repository;

import ir.ac.ut.ece.ie.miobook.model.Admin;
import ir.ac.ut.ece.ie.miobook.model.Customer;
import ir.ac.ut.ece.ie.miobook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username = :username AND TYPE(u) = Admin")
    Optional<Admin> findAdminByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.username = :username AND TYPE(u) = Customer")
    Optional<Customer> findCustomerByUsername(@Param("username") String username);
}