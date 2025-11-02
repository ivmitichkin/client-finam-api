package finam.client.enums;

import lombok.Getter;

@Getter
public enum TimeFrameEnum {
    TIME_FRAME_UNSPECIFIED(0),
    TIME_FRAME_M1(1),
    TIME_FRAME_M5(5),
    TIME_FRAME_M15(9),
    TIME_FRAME_M30(11),
    TIME_FRAME_H1(12),
    TIME_FRAME_H2(13),
    TIME_FRAME_H4(15),
    TIME_FRAME_H8(17),
    TIME_FRAME_D(19),
    TIME_FRAME_W(20),
    TIME_FRAME_MN(21),
    TIME_FRAME_QR(22);

    public final int value;

    TimeFrameEnum(int value) {
        this.value = value;
    }

}
