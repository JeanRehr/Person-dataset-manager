package gb;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.ZoneId;

public class DateUtils {
    public static LocalDate parseDateInput(
        String input,
        DateTimeFormatter dateFormat
    ) throws DateTimeParseException {
            return LocalDate.parse(input, dateFormat);
    }

    public static long convertDateToUnixEpoch(LocalDate birthDateObj, String zoneId) {
        return birthDateObj
            .atStartOfDay(ZoneId.of(zoneId))
            .toInstant()
            .toEpochMilli();
    }
}