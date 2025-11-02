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
public class TradeDTO {
    @JsonProperty("trade_id") private String tradeId;
    @JsonProperty("mpid") private String mpid;
    @JsonProperty("timestamp") private OffsetDateTime timestamp;
    @JsonProperty("price") private ValueObjectDTO price;
    @JsonProperty("size") private ValueObjectDTO size;
    @JsonProperty("side") private String side;
}