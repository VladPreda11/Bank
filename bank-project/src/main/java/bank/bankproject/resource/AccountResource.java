package bank.bankproject.resource;

import bank.bankproject.dto.account.AccountDto;
import bank.bankproject.dto.account.AccountResponseDto;
import bank.bankproject.dto.transaction.TransactionDto;
import bank.bankproject.dto.transaction.TransactionRequestDto;
import bank.bankproject.service.AccountService;
import bank.bankproject.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountResource {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @PostMapping
    public AccountDto save(@RequestBody AccountDto account) {
        return accountService.save(account);
    }

    @GetMapping
    public List<AccountResponseDto> getAll(@PathParam("currency") Optional<String> currency) {
        return accountService.getAll(currency);
    }

    @GetMapping("/{id}")
    public AccountDto getById(@PathVariable Long id) {
        return accountService.getById(id);
    }

    @PostMapping("/{id}/transactions")
    public TransactionDto send(@PathVariable Long id, @RequestBody TransactionRequestDto transaction){
        return transactionService.save(id, transaction);
    }

    @GetMapping("/{id}/transactions")
    public List<TransactionDto> getAll(@PathVariable Long id, @PathParam("type") Optional<String> type) {
        return transactionService.getAllForAccount(id, type)
                .stream()
                .sorted((t1,t2) -> t2.getDateTime().compareTo(t1.getDateTime()))
                .collect(Collectors.toList());

    }
}
