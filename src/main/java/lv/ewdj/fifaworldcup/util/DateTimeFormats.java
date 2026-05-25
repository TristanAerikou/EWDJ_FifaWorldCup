package lv.ewdj.fifaworldcup.util;

import java.time.format.DateTimeFormatter;

public final class DateTimeFormats {

    private DateTimeFormats() {}

    public static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyy-MM-dd");

    public static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm");

}
