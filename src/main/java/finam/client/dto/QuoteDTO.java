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
public class QuoteDTO {
    @JsonProperty("symbol") private String symbol;
    @JsonProperty("timestamp") private OffsetDateTime timestamp;
    @JsonProperty("ask") private ValueObjectDTO ask;
    @JsonProperty("ask_size") private ValueObjectDTO askSize;
    @JsonProperty("bid") private ValueObjectDTO bid;
    @JsonProperty("bid_size") private ValueObjectDTO bidSize;
    @JsonProperty("last") private ValueObjectDTO last;
    @JsonProperty("last_size") private ValueObjectDTO lastSize;
    @JsonProperty("volume") private ValueObjectDTO volume;
    @JsonProperty("turnover") private ValueObjectDTO turnover;
    @JsonProperty("open") private ValueObjectDTO open;
    @JsonProperty("high") private ValueObjectDTO high;
    @JsonProperty("low") private ValueObjectDTO low;
    @JsonProperty("close") private ValueObjectDTO close;
    @JsonProperty("change") private ValueObjectDTO change;
    @JsonProperty("option") private OptionDTO option;
}
