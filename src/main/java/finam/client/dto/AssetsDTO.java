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
public class AssetsDTO {
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("id")
    private String id;
    @JsonProperty("ticker")
    private String ticker;
    @JsonProperty("mic")
    private String mic;
    @JsonProperty("isin")
    private String isin;
    @JsonProperty("type")
    private String type;
    @JsonProperty("name")
    private String name;
}