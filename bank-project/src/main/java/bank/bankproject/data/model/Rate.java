package bank.bankproject.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rate {
    private String currency_name;
    private Double rate;
    private Double rate_for_amount;
}
