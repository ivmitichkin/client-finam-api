package finam.client.dto;

import finam.client.enums.QuoteLevelEnum;
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
public class MdPermissionDTO {
    @JsonProperty("quote_level")
    private QuoteLevelEnum quoteLevel;

    @JsonProperty("delay_minutes")
    private int delayMinutes;

    @JsonProperty("mic")
    private String mic;

    @JsonProperty("country")
    private String country;

    @JsonProperty("continent")
    private String continent;

    @JsonProperty("worldwide")
    private boolean worldwide;
}
