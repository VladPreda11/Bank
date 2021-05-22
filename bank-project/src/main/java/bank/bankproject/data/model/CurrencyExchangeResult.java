package bank.bankproject.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyExchangeResult {
    private Double rate;
    private Double exchangedAmount;
}
