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
public class CashItemDTO {
    @JsonProperty("currency_code")
    private String currencyCode;

    @JsonProperty("units")
    private Long units;

    @JsonProperty("nanos")
    private Integer nanos;
}
