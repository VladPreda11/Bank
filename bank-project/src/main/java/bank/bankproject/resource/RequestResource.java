package bank.bankproject.resource;

import bank.bankproject.service.RequestService;
import bank.bankproject.dto.request.CreateRequestDto;
import bank.bankproject.dto.request.RequestDto;
import bank.bankproject.dto.request.ResponseToRequestDto;
import bank.bankproject.dto.transaction.TransactionDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/requests")
@AllArgsConstructor
public class RequestResource {
    private final RequestService requestService;

    @PostMapping
    public RequestDto save(@RequestBody CreateRequestDto request) {
        return requestService.save(request);
    }

    @GetMapping
    public List<RequestDto> getAll(@PathParam("type") Optional<String> type) {
        return requestService.getAll(type);
    }

    @GetMapping("/{id}")
    public RequestDto getById(@PathVariable Long id) {
        return requestService.getById(id);
    }

    @PostMapping("/{id}")
    public Optional<TransactionDto> process(@PathVariable Long id, @RequestBody ResponseToRequestDto response){
        return requestService.process(id, response);
    }
}
