package bank.bankproject.service;

import bank.bankproject.exception.AccountNotFoundException;
import bank.bankproject.exception.InvalidCurrencyException;
import bank.bankproject.repository.AccountRepository;
import bank.bankproject.dto.account.AccountDto;
import bank.bankproject.dto.account.AccountResponseDto;
import bank.bankproject.data.entities.Account;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserService userService;

    public AccountService(AccountRepository accountRepository,
                          UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }

    public AccountDto save(AccountDto account) {

        try {
            Currency.getInstance(account.getCurrency());
        } catch (IllegalArgumentException ex){
            throw new InvalidCurrencyException("No such currency found.");
        }

        Account accountEntity = new Account(account);
        accountEntity.setUser(userService.getCurrentUser());
        Account savedEntity = accountRepository.save(accountEntity);

        return new AccountDto(savedEntity);
    }

    public AccountResponseDto getById(Long id) {
        return userService.getCurrentUser()
                .getAccounts()
                .stream()
                .filter(a -> a.getAccountId().equals(id))
                .findFirst()
                .map(a -> new AccountResponseDto(a))
                .orElseThrow(() -> new AccountNotFoundException("Account not found."));
    }

    public List<AccountResponseDto> getAll(Optional<String> currency) {

        return userService.getCurrentUser()
                .getAccounts()
                .stream()
                .filter(a -> currency
                        .map(c -> c.equals(a.getCurrency()))
                        .orElse(true))
                .map(a -> new AccountResponseDto(a))
                .collect(Collectors.toList());
    }
}
