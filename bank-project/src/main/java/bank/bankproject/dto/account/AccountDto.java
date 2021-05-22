package bank.bankproject.dto.account;

import bank.bankproject.data.entities.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private String iban;
    private String currency;
    private Double balance;

    public AccountDto(Account account){
        this.iban = account.getIban();
        this.currency = account.getCurrency();
        this.balance = account.getBalance();
    }
}
