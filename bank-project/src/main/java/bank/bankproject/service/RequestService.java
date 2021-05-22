package bank.bankproject.service;

import bank.bankproject.exception.*;
import bank.bankproject.repository.AccountRepository;
import bank.bankproject.repository.RequestRepository;
import bank.bankproject.repository.TransactionRepository;
import bank.bankproject.repository.UserRepository;
import bank.bankproject.data.entities.Account;
import bank.bankproject.data.entities.Request;
import bank.bankproject.data.entities.Transaction;
import bank.bankproject.data.entities.User;
import bank.bankproject.dto.request.CreateRequestDto;
import bank.bankproject.dto.request.RequestDto;
import bank.bankproject.dto.request.ResponseToRequestDto;
import bank.bankproject.dto.transaction.TransactionDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public RequestService(RequestRepository requestRepository,
                          UserService userService,
                          UserRepository userRepository,
                          AccountRepository accountRepository,
                          TransactionRepository transactionRepository) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public RequestDto save(CreateRequestDto request) {
        User currentUser = userService.getCurrentUser();

        User requestFromUser = userRepository
                .findById(request.getRequestFromUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        try {
            Currency.getInstance(request.getCurrency());
        } catch (IllegalArgumentException ex) {
            throw new InvalidCurrencyException("Currency not found.");
        }

        if(request.getAmount() <= 0D){
            throw new InvalidAmountException("Amount cannot me negative or zero.");
        }

        Request requestEntity = new Request(currentUser, requestFromUser, request.getCurrency(), request.getAmount());

        Request requestSaved = requestRepository.save(requestEntity);

        return new RequestDto("SENT", requestSaved);

    }

    public RequestDto getById(Long id) {
        User currentUser = userService.getCurrentUser();

        return requestRepository.findById(id)
                .map(r -> r.getRequestingUser().getUserId().equals(currentUser.getUserId()) ?
                        (new RequestDto("SENT", r))
                        : (new RequestDto("RECEIVED", r)))
                .orElseThrow(() -> new RequestNotFoundException("Request not found."));

    }

    public List<RequestDto> getAll(Optional<String> type) {

        String typeString = type.orElse("");

        User currentUser = userService.getCurrentUser();

        Stream<RequestDto> sent = requestRepository
                .findByRequestingUser(currentUser)
                .stream()
                .map(r -> new RequestDto("SENT", r));

        if(typeString.equalsIgnoreCase("SENT")){
            return sent.collect(Collectors.toList());
        }

        Stream<RequestDto> received = requestRepository
                .findBySendingUser(currentUser)
                .stream()
                .map(r -> new RequestDto("RECEIVED", r));

        if(typeString.equalsIgnoreCase("RECEIVED")){
            return received.collect(Collectors.toList());
        }

        return Stream.concat(sent, received).collect(Collectors.toList());
    }

    @Transactional
    public Optional<TransactionDto> process(Long requestId, ResponseToRequestDto response){
        User currentUser = userService.getCurrentUser();

        Request request = requestRepository
                .findBySendingUser(currentUser)
                .stream()
                .filter(r -> r.getRequestId().equals(requestId))
                .findFirst()
                .orElseThrow(() -> new RequestNotFoundException("Request not found."));

        if(!response.getApproved()){
            requestRepository.delete(request);
            return Optional.empty();
        }

        User receivingUser = userRepository
                .findById(request.getRequestingUser().getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        List<Account> senderViableAccounts = currentUser
                .getAccounts()
                .stream()
                .filter(a -> a.getCurrency().equals(request.getCurrency()))
                .filter(a -> a.getBalance() >= request.getAmount())
                .collect(Collectors.toList());

        if(senderViableAccounts.isEmpty()){
            throw new InsufficientFundsException("Insufficient funds.");
        }

        Account senderAccount;

        if(response.getAccountId() != null){
            senderAccount = currentUser
                    .getAccounts()
                    .stream()
                    .filter(a -> a.getAccountId().equals(response.getAccountId()))
                    .findFirst()
                    .orElseThrow(() -> new AccountNotFoundException("Account not found."));
        }

        else if(senderViableAccounts.size() > 1){
            throw new MultipleAccountsException("Multiple accounts, please be more specific.");
        }

        else {
            senderAccount = senderViableAccounts.get(0);
        }

        Account receiverAccount;

        try {
            receiverAccount = receivingUser
                    .getAccounts()
                    .stream()
                    .filter(a -> a.getCurrency().equals(request.getCurrency()))
                    .findFirst()
                    .orElseThrow(() -> new AccountNotFoundException(""));
        } catch (AccountNotFoundException e) {
            Account accountEntity = new Account("RO0PLACEHOLDER49875",request.getCurrency(),0.0);
            accountEntity.setUser(receivingUser);

            receiverAccount = accountRepository.save(accountEntity);
        }

        Double senderNewBalance = senderAccount.getBalance() - request.getAmount();
        senderAccount.setBalance(senderNewBalance);
        accountRepository.save(senderAccount);

        receiverAccount.setBalance(request.getAmount());
        receiverAccount = accountRepository.save(receiverAccount);

        Transaction transaction = new Transaction();
        transaction.setDetails("Requested payment");
        transaction.setDateTime(LocalDateTime.now());
        transaction.setSender(senderAccount);
        transaction.setReceiver(receiverAccount);
        transaction.setAmountSent(request.getAmount());
        transaction.setAmountReceived(request.getAmount());
        transaction.setExchangeRate(1D);

        Transaction transactionSaved = transactionRepository.save(transaction);

        requestRepository.delete(request);

        return Optional.of(new TransactionDto("SENT", transactionSaved));
    }
}
