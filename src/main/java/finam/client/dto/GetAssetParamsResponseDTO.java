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
public class GetAssetParamsResponseDTO {
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("account_id")
    private String accountId;
    @JsonProperty("tradeable")
    private boolean tradeable;
    @JsonProperty("longable")
    private LongableDTO longable;
    @JsonProperty("shortable")
    private ShortableDTO shortable;
    @JsonProperty("long_risk_rate")
    private RiskRateDTO longRiskRate;
    @JsonProperty("long_collateral")
    private MoneyDTO longCollateral;
    @JsonProperty("short_risk_rate")
    private RiskRateDTO shortRiskRate;
    @JsonProperty("long_initial_margin")
    private MoneyDTO longInitialMargin;
}