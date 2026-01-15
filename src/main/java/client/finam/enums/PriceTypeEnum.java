package client.finam.enums;

import lombok.Getter;

@Getter
public enum PriceTypeEnum {
    UNKNOWN(0),
    POSITIVE(1),
    NON_NEGATIVE(2),
    ANY(3);

    private final int number;

    PriceTypeEnum(int number) { this.number = number; }
}