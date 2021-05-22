package bank.bankproject.restclient;

import bank.bankproject.data.model.CurrencyExchangeResponse;
import bank.bankproject.data.model.CurrencyExchangeResult;
import bank.bankproject.data.model.Rate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

@Component
public class CurrencyExchangeRestClient {
    @Value("${rapidapi.key.name}")
    String apiKeyName;

    @Value("${rapidapi.key.value}")
    String apiKeyValue;

    @Value("${rapidapi.host.name")
    String hostName;

    @Value("${rapidapi.host.value}")
    String hostValue;

    RestTemplate restTemplate;

    public CurrencyExchangeRestClient(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    public CurrencyExchangeResponse exchangeInfo(String from, String to, String amount){
        try {
            String uriString = String
                    .format("https://currency-converter5.p.rapidapi.com/currency/convert?format=json&from=%s&to=%s&amount=%s",
                            from, to, amount);
            URI uri = new URI(uriString);
            HttpHeaders headers = new HttpHeaders();
            headers.set(apiKeyName, apiKeyValue);
            headers.set(hostName, hostValue);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> request = new HttpEntity<String>(headers);
            ResponseEntity<CurrencyExchangeResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request,
                    CurrencyExchangeResponse.class);

            return responseEntity.getBody();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Wrong URI syntax for API call.");
        }
    }

    public CurrencyExchangeResult exchange(String from, String to, String amount){
        Rate rate = exchangeInfo(from, to, amount).getRates().get(to);
        return new CurrencyExchangeResult(rate.getRate(), rate.getRate_for_amount());
    }

}
