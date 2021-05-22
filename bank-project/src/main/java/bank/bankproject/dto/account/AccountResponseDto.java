package bank.bankproject.dto.account;

import bank.bankproject.data.entities.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDto extends AccountDto{
    private Long accountId;

    public AccountResponseDto(Account account) {
        super(account);
        this.accountId = account.getAccountId();
    }
}
