package bank.bankproject.dto.request;

import bank.bankproject.data.entities.Request;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestDto {
    private Long requestId;

    private String type;


    private Long requestingUserId;
    private Long sendingUserId;
    private String currency;
    private Double amount;

    public RequestDto(String type, Request request){
        this.type = type;

        this.requestId = request.getRequestId();
        this.currency = request.getCurrency();
        this.amount = request.getAmount();

        if(type.equals("SENT")){
            this.sendingUserId = request.getSendingUser().getUserId();
        }

        else {
            this.requestingUserId = request.getRequestingUser().getUserId();
        }
    }

}
