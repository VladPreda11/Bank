package bank.bankproject.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyExchangeResponse {
    private Double amount;
    private String base_currency_code;
    private String base_currency_name;
    private HashMap<String, Rate> rates;
    private String status;
    private String updated_date;
}
