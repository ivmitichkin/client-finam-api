package finam.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public abstract class MarginDTO {
    @JsonProperty("available_cash")
    private ValueObjectDTO availableCash;

    @JsonProperty("initial_margin")
    private ValueObjectDTO initialMargin;

    @JsonProperty("maintenance_margin")
    private ValueObjectDTO maintenanceMargin;
}
