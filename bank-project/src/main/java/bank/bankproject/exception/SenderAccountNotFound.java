package bank.bankproject.exception;

public class SenderAccountNotFound extends AccountNotFoundException{
    public SenderAccountNotFound(String message) {
        super(message);
    }
}
