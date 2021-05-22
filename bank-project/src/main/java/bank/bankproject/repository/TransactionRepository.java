package bank.bankproject.repository;

import bank.bankproject.data.entities.Account;
import bank.bankproject.data.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySender(Account sender);
    List<Transaction> findByReceiver(Account receiver);
}
