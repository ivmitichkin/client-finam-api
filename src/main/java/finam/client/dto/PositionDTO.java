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
public class PositionDTO {
    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("quantity")
    private ValueObjectDTO quantity;

    @JsonProperty("average_price")
    private ValueObjectDTO averagePrice;

    @JsonProperty("current_price")
    private ValueObjectDTO currentPrice;

    @JsonProperty("daily_pnl")
    private ValueObjectDTO dailyPnl;

    @JsonProperty("unrealized_pnl")
    private ValueObjectDTO unrealizedPnl;
}