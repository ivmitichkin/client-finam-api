package client.finam.dto;

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
public class QuotaUsageDTO {

    @JsonProperty("name")
    private String name;

    @JsonProperty("limit")
    private long limit;

    @JsonProperty("remaining")
    private long remaining;

    @JsonProperty("reset_time")
    private String resetTime;

}
