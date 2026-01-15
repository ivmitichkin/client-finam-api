package client.finam.dto;

import client.finam.enums.OrderTypeEnum;
import client.finam.enums.SideEnum;
import client.finam.enums.StopConditionEnum;
import client.finam.enums.TimeInForceEnum;
import client.finam.enums.ValidBeforeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("quantity")
    private ValueObjectDTO quantity;

    @JsonProperty("type")
    private OrderTypeEnum type;

    @JsonProperty("side")
    private SideEnum side;

    @JsonProperty("time_in_force")
    private TimeInForceEnum timeInForce;

    @JsonProperty("limit_price")
    private ValueObjectDTO limitPrice;

    @JsonProperty("stop_price")
    private ValueObjectDTO stopPrice;

    @JsonProperty("stop_condition")
    private StopConditionEnum stopCondition;

    @JsonProperty("legs")
    private List<LegDTO> legs;

    @JsonProperty("client_order_id")
    private String clientOrderId;

    @JsonProperty("valid_before")
    private ValidBeforeEnum validBefore;

    @JsonProperty("comment")
    private String comment;

}