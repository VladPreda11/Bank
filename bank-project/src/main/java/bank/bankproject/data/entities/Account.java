package bank.bankproject.data.entities;

import bank.bankproject.dto.account.AccountDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "account")
@Data
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(name = "iban", nullable = false)
    private String iban;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "balance", nullable = false)
    private Double balance;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Transaction> sentTransactions = new HashSet<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Transaction> receivedTransactions = new HashSet<>();

    public Account(String iban, String currency, Double balance) {
        this.iban = iban;
        this.currency = currency;
        this.balance = balance;
    }

    public Account(AccountDto accountDto){
        this.iban = accountDto.getIban();
        this.currency = accountDto.getCurrency();
        this.balance = accountDto.getBalance();
    }
}
