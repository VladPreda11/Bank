package bank.bankproject.resource;

import bank.bankproject.exception.*;
import bank.bankproject.dto.RestErrorObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<RestErrorObject> handleUserNotFound(Exception ex, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestErrorObject("User not found"));
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<RestErrorObject> handleAccountNotFound(Exception ex, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestErrorObject("Account not found"));
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<RestErrorObject> handleTransactionNotFound(Exception ex, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestErrorObject("Transaction not found"));
    }

    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<RestErrorObject> handleRequestNotFound(Exception ex, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestErrorObject("Request not found"));
    }

    @ExceptionHandler(SenderAccountNotFound.class)
    public ResponseEntity<RestErrorObject> handleSenderAccountNotFound(Exception ex, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestErrorObject("Sender account not found."));
    }

    @ExceptionHandler(ReceiverAccountNotFound.class)
    public ResponseEntity<RestErrorObject> handleReceiverAccountNotFound(Exception ex, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestErrorObject("Receiver account not found."));
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<RestErrorObject> handleInsufficientFunds(Exception ex, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new RestErrorObject("Insufficient funds."));
    }

    @ExceptionHandler(MultipleAccountsException.class)
    public ResponseEntity<RestErrorObject> handleMultipleAccounts(Exception ex, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestErrorObject("There are multiple viable " +
                "accounts. Please include the desired account's Id in the request."));
    }

    @ExceptionHandler(InvalidAccountException.class)
    public ResponseEntity<RestErrorObject> handleInvalidAccount(Exception ex, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestErrorObject("Cannot send to same account."));
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<RestErrorObject> handleInvalidAmount(Exception ex, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestErrorObject("Amount cannot be negative or zero."));
    }

    @ExceptionHandler(UsernameTakenException.class)
    public ResponseEntity<RestErrorObject> handleUsernameTaken(Exception ex, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new RestErrorObject("There is already an account with that username."));
    }

    @ExceptionHandler(InvalidCurrencyException.class)
    public ResponseEntity<RestErrorObject> handleInvalidCurrency(Exception ex, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestErrorObject("No such currency found."));
    }
}
