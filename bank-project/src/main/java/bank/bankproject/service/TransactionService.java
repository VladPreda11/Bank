package bank.bankproject.service;

import bank.bankproject.exception.*;
import bank.bankproject.repository.AccountRepository;
import bank.bankproject.repository.TransactionRepository;
import bank.bankproject.restclient.CurrencyExchangeRestClient;
import bank.bankproject.data.entities.Account;
import bank.bankproject.data.entities.User;
import bank.bankproject.data.model.CurrencyExchangeResponse;
import bank.bankproject.data.model.CurrencyExchangeResult;
import bank.bankproject.dto.transaction.TransactionDto;
import bank.bankproject.dto.transaction.TransactionRequestDto;
import bank.bankproject.data.entities.Transaction;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final CurrencyExchangeRestClient currencyExchangeRestClient;


    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              UserService userService,
                              CurrencyExchangeRestClient currencyExchangeRestClient) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userService = userService;
        this.currencyExchangeRestClient = currencyExchangeRestClient;
    }

    @Transactional
    public TransactionDto save(Long senderAccountId, TransactionRequestDto transaction) {
        User currentUser = userService.getCurrentUser();

        if(transaction.getAmount() <= 0D){
            throw new InvalidAmountException("Amount cannot be less than or equal to zero.");
        }

        Account senderAccount = currentUser
                .getAccounts()
                .stream()
                .filter (a -> a.getAccountId().equals(senderAccountId))
                .findFirst()
                .orElseThrow(() -> new SenderAccountNotFound("Sender account not found."));

        if(transaction.getAmount() > senderAccount.getBalance()){
            throw new InsufficientFundsException("Insufficient funds.");
        }

        Account receiverAccount = accountRepository
                .findById(transaction.getReceiverAccountId())
                .orElseThrow(() -> new ReceiverAccountNotFound("Receiver account not found."));

        if(senderAccount.getAccountId().equals(receiverAccount.getAccountId())){
            throw new InvalidAccountException("Cannot transfer into same account.");
        }

        Double senderNewBalance = senderAccount.getBalance() - transaction.getAmount();
        senderAccount.setBalance(senderNewBalance);
        accountRepository.save(senderAccount);

        CurrencyExchangeResult currencyExchangeResult = currencyExchangeRestClient
                        .exchange(senderAccount.getCurrency(),
                                 receiverAccount.getCurrency(),
                                String.valueOf(transaction.getAmount()));

        Double amountReceived = currencyExchangeResult.getExchangedAmount();
        Double exchangeRate = currencyExchangeResult.getRate();

        Double receiverNewBalance = receiverAccount.getBalance() + amountReceived;
        receiverAccount.setBalance(receiverNewBalance);
        accountRepository.save(receiverAccount);

        Transaction transactionEntity = new Transaction();
        transactionEntity.setDetails(transaction.getDetails());
        transactionEntity.setDateTime(LocalDateTime.now());
        transactionEntity.setSender(senderAccount);
        transactionEntity.setReceiver(receiverAccount);
        transactionEntity.setAmountSent(transaction.getAmount());
        transactionEntity.setAmountReceived(amountReceived);
        transactionEntity.setExchangeRate(exchangeRate);

        Transaction transactionSaved = transactionRepository.save(transactionEntity);
        return new TransactionDto("SENT", transactionSaved);

    }


    public TransactionDto getById(Long id) {
        return transactionRepository
                .findById(id)
                .map(TransactionDto::new)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
    }


    public List<TransactionDto> getAllForAccount(Long accountId, Optional<String> type){

        String typeString = type.orElse("");

        Account account = userService.getCurrentUser()
                .getAccounts()
                .stream()
                .filter(a -> a.getAccountId().equals(accountId))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException("Account not found."));

        Stream<TransactionDto> sent = transactionRepository
                .findBySender(account)
                .stream()
                .map(t -> new TransactionDto("SENT", t));

        if(typeString.equalsIgnoreCase("SENT")){
            return sent.collect(Collectors.toList());
        }

        Stream<TransactionDto> received =  transactionRepository
                .findByReceiver(account)
                .stream()
                .map(t -> new TransactionDto("RECEIVED", t));

        if(typeString.equalsIgnoreCase("RECEIVED")){
            return received.collect(Collectors.toList());
        }

        return Stream.concat(sent, received).collect(Collectors.toList());

    }

    public List<TransactionDto> getAll(Optional<String> type) {
        return userService.getCurrentUser()
                .getAccounts()
                .stream()
                .flatMap(
                        acc -> getAllForAccount(acc.getAccountId(), type)
                                .stream()
                )
                .collect(Collectors.toList());
    }

    public CurrencyExchangeResponse getExchangeInfo(Optional<String> from, Optional<String> to, Optional<Double> amount){
        return currencyExchangeRestClient
                .exchangeInfo(from.orElse(""),
                        to.orElse(""),
                        amount.map(a -> String.valueOf(a)).orElse(""));
    }
}
