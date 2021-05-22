package bank.bankproject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRequestDto {
    private Long requestFromUserId;
    private String currency;
    private Double amount;
}
