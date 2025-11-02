package finam.client.dto;

import finam.client.enums.TransactionCategoryEnum;
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
public class TransactionDTO {
    @JsonProperty("id")
    private String id;

    @JsonProperty("category")
    private String category;

    @JsonProperty("timestamp")
    private OffsetDateTime timestamp;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("change")
    private CashItemDTO change;

    @JsonProperty("trade")
    private TradeDTO trade;

    @JsonProperty("transaction_category")
    private TransactionCategoryEnum transactionCategory;

    @JsonProperty("transaction_name")
    private String transactionName;
}
