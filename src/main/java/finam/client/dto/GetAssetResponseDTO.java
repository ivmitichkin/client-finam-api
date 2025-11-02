package finam.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GetAssetResponseDTO extends AssetsDTO {
    @JsonProperty("board")
    private String board;
    @JsonProperty("decimals")
    private Integer decimals;
    @JsonProperty("min_step")
    private Long minStep;
    @JsonProperty("lot_size")
    private ValueObjectDTO lotSize;
    @JsonProperty("expiration_date")
    private String expirationDate;
    @JsonProperty("quote_currency")
    private String quoteCurrency;
}
