package bank.bankproject.repository;

import bank.bankproject.data.entities.Request;
import bank.bankproject.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequestingUser(User requestingUser);
    List<Request> findBySendingUser(User sendingUser);
}
