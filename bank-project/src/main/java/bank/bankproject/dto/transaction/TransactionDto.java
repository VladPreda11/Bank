package bank.bankproject.dto.transaction;

import bank.bankproject.data.entities.Transaction;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDto {
    private Long transactionId;
    private Long senderAccountId;
    private Long receiverAccountId;
    private String type;
    private String details;
    private LocalDateTime dateTime;
    private Double amountSent;
    private Double amountReceived;
    private String currency;
    private String exchangedTo;
    private Double exchangeRate;

    public TransactionDto(String type, Transaction transaction){
        this.transactionId = transaction.getTransactionId();
        this.type = type;
        this.details = transaction.getDetails();
        this.dateTime = transaction.getDateTime();
        this.senderAccountId = transaction.getSender().getAccountId();
        this.receiverAccountId = transaction.getReceiver().getAccountId();

        if(type.equals("SENT")){
            this.amountSent = transaction.getAmountSent();
            this.currency = transaction.getSender().getCurrency();
            if(transaction.getExchangeRate() != 1D){
                this.exchangedTo = transaction.getReceiver().getCurrency();
                this.exchangeRate = transaction.getExchangeRate();
            }
        }

        else {
            this.amountReceived = transaction.getAmountReceived();
            this.currency = transaction.getReceiver().getCurrency();
        }
    }

    public TransactionDto(Transaction transaction) {
        this.transactionId = transaction.getTransactionId();
        this.details = transaction.getDetails();
        this.dateTime = transaction.getDateTime();
        this.amountSent = transaction.getAmountSent();
        this.amountReceived = transaction.getAmountReceived();
        this.currency = transaction.getReceiver().getCurrency();
        this.senderAccountId = transaction.getSender().getAccountId();
        this.receiverAccountId = transaction.getReceiver().getAccountId();
    }
}
