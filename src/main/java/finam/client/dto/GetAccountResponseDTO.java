package finam.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAccountResponseDTO {
    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("type")
    private String type;

    @JsonProperty("status")
    private String status;

    @JsonProperty("equity")
    private ValueObjectDTO equity;

    @JsonProperty("unrealized_profit")
    private ValueObjectDTO unrealizedProfit;

    @JsonProperty("positions")
    private List<PositionDTO> positions;

    @JsonProperty("cash")
    private List<CashItemDTO> cash;

    @JsonProperty("portfolio_mc")
    private MCDTO portfolioMc;

    @JsonProperty("portfolio_mct")
    private MCTDTO portfolioMct;

    @JsonProperty("portfolio_forts")
    private FortsDTO portfolioForts;
}
