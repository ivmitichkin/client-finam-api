package finam.client.enums;

import lombok.Getter;

@Getter
public enum StopConditionEnum {
    STOP_CONDITION_UNSPECIFIED(0),
    STOP_CONDITION_LAST_UP(1),
    STOP_CONDITION_LAST_DOWN(2);

    private final int number;

    StopConditionEnum(int number) {
        this.number = number;
    }
}
