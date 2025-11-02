package finam.client.dto;

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
public class AccountTradeDTO {
    @JsonProperty("trade_id")
    private String tradeId;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("price")
    private ValueObjectDTO price;

    @JsonProperty("size")
    private ValueObjectDTO size;

    @JsonProperty("side")
    private String side;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("account_id")
    private String accountId;
}
