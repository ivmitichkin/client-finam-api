package finam.client.dto;

import finam.client.enums.TimeFrameEnum;
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
public class BarsRequestDTO {
    @JsonProperty("symbol") private String symbol;
    @JsonProperty("timeframe") private TimeFrameEnum timeframe;
    @JsonProperty("interval") private String interval;
}
