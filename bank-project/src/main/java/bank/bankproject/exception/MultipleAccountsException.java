package bank.bankproject.exception;

public class MultipleAccountsException extends RuntimeException{
    public MultipleAccountsException(String message){
        super(message);
    }
}
