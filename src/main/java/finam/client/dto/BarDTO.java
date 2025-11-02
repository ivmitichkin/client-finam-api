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
public class BarDTO {
    @JsonProperty("timestamp") private OffsetDateTime timestamp;
    @JsonProperty("open") private ValueObjectDTO open;
    @JsonProperty("high") private ValueObjectDTO high;
    @JsonProperty("low") private ValueObjectDTO low;
    @JsonProperty("close") private ValueObjectDTO close;
    @JsonProperty("volume") private ValueObjectDTO volume;
}
