package bank.bankproject.exception;

public class ReceiverAccountNotFound extends AccountNotFoundException{
    public ReceiverAccountNotFound(String message) {
        super(message);
    }
}
