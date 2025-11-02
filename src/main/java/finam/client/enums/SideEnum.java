package finam.client.enums;

import lombok.Getter;

@Getter
public enum SideEnum {
    SIDE_UNSPECIFIED(0),
    SIDE_BUY(1),
    SIDE_SELL(2);

    public final int value;

    SideEnum(int value) {
        this.value = value;
    }
}