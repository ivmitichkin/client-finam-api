package finam.client.enums;

import lombok.Getter;

@Getter
public enum ValidBeforeEnum {
    VALID_BEFORE_UNSPECIFIED(0),
    VALID_BEFORE_END_OF_DAY(1),
    VALID_BEFORE_GOOD_TILL_CANCEL(2),
    VALID_BEFORE_GOOD_TILL_DATE(3);

    private final int number;

    ValidBeforeEnum(int number) {
        this.number = number;
    }
}