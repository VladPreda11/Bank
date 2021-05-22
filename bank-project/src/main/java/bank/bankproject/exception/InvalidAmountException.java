package bank.bankproject.exception;

public class InvalidAmountException extends RuntimeException{
    public InvalidAmountException (String message) {
        super(message);
    }
}
