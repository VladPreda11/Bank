package bank.bankproject.resource;

import bank.bankproject.service.TransactionService;
import bank.bankproject.data.model.CurrencyExchangeResponse;
import bank.bankproject.dto.transaction.TransactionDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionResource {
    private TransactionService transactionService;


    @GetMapping
    public List<TransactionDto> getAll(@PathParam("type") Optional<String> type) {
        return transactionService.getAll(type);
    }

    @GetMapping("/{id}")
    public TransactionDto getById(@PathVariable long id) {
        return transactionService.getById(id);
    }

    @GetMapping("/exchangeinfo")
    public CurrencyExchangeResponse getExchangeInfo(@PathParam("from") Optional<String> from,
                                                    @PathParam("to") Optional<String> to,
                                                    @PathParam("amount") Optional<Double> amount){
        return transactionService.getExchangeInfo(from, to, amount);
    }
}
