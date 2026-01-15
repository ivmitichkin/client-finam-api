package client.finam.dto;

import client.finam.enums.TypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionDTO {
    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("type")
    private TypeEnum type;

    @JsonProperty("contract_size")
    private ValueObjectDTO contractSize;

    @JsonProperty("trade_first_day")
    private YearMonthDayDTO tradeFirstDay;

    @JsonProperty("trade_last_day")
    private YearMonthDayDTO tradeLastDay;

    @JsonProperty("strike")
    private ValueObjectDTO strike;

    @JsonProperty("multiplier")
    private ValueObjectDTO multiplier;

    @JsonProperty("expiration_first_day")
    private YearMonthDayDTO expirationFirstDay;

    @JsonProperty("expiration_last_day")
    private YearMonthDayDTO expirationLastDay;

}