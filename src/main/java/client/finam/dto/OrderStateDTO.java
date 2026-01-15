package client.finam.dto;

import client.finam.enums.OrderStatusEnum;
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
public class OrderStateDTO {

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("exec_id")
    private String execId;

    @JsonProperty("status")
    private OrderStatusEnum status;

    @JsonProperty("order")
    private OrderDTO order;

    @JsonProperty("transact_at")
    private OffsetDateTime transactAt;

    @JsonProperty("accept_at")
    private OffsetDateTime acceptAt;

    @JsonProperty("withdraw_at")
    private OffsetDateTime withdrawAt;

    @JsonProperty("initial_quantity")
    private ValueObjectDTO initialQuantity;

    @JsonProperty("executed_quantity")
    private ValueObjectDTO executedQuantity;

    @JsonProperty("remaining_quantity")
    private ValueObjectDTO remainingQuantity;

}