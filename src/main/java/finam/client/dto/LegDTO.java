package finam.client.dto;

import finam.client.enums.SideEnum;
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
public class LegDTO {
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("quantity")
    private ValueObjectDTO quantity;
    @JsonProperty("side")
    private SideEnum side;
}
