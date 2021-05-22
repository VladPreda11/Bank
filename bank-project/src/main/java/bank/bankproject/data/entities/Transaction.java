package bank.bankproject.data.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "transaction")
@Data
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(name = "details", nullable = false)
    private String details;

    @Column(name = "date", nullable = false)
    private LocalDateTime dateTime;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name="senderId")
    private Account sender;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name="receiverId")
    private Account receiver;

    @Column(name = "amount_sent", nullable = false)
    private Double amountSent;

    @Column(name = "amount_received", nullable = false)
    private Double amountReceived;

    @Column(name = "exchange_rate", nullable = false)
    private Double exchangeRate;

    public Transaction(String details, LocalDateTime dateTime, Double amountSent, Double amountReceived, Double exchangeRate) {
        this.details = details;
        this.dateTime = dateTime;
        this.amountSent = amountSent;
        this.amountReceived = amountReceived;
        this.exchangeRate = exchangeRate;
    }

}
