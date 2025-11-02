package finam.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderBookRowDTO {
    @JsonProperty("price") private ValueObjectDTO price;
    @JsonProperty("sell_size") private ValueObjectDTO sellSize;
    @JsonProperty("buy_size") private ValueObjectDTO buySize;
    @JsonProperty("action") private String action;
    @JsonProperty("mpid") private String mpid;
    @JsonProperty("timestamp") private OffsetDateTime timestamp;
}
