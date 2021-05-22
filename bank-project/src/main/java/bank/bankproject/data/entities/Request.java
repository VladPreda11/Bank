package bank.bankproject.data.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "request")
@Data
@NoArgsConstructor
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name="requesting_user_id")
    private User requestingUser;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name="sending_user_id")
    private User sendingUser;

    public Request(User requestingUser, User sendingUser, String currency, Double amount) {
        this.requestingUser = requestingUser;
        this.sendingUser = sendingUser;
        this.currency = currency;
        this.amount = amount;
    }
}
