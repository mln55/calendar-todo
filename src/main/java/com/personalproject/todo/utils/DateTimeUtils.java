package com.personalproject.todo.utils;

import static com.google.common.base.Preconditions.checkArgument;

import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class DateTimeUtils {

    public static Timestamp timestampOf(LocalDateTime localDateTime) {
        return localDateTime == null ? null : Timestamp.valueOf(localDateTime);
    }

    public static LocalDateTime localDateTimeOf(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    public static LocalDate stringDateToLocalDate(String stringDate) {
        if (stringDate == null) return LocalDate.now();
        
        String errorMessage = "입력 날짜 오류. stringDate:";
        checkArgument(Pattern.matches("\\d{4}-\\d{2}-\\d{2}", stringDate), errorMessage + stringDate);
        String[] split = stringDate.split("-");
        int year = Integer.parseInt(split[0]);
        LocalDate localDate = null;
        try {
            if (year < 1970 || year > 2100) throw new DateTimeException("");
            localDate = LocalDate.parse(stringDate, DateTimeFormatter.ofPattern("yyyy-M-d"));
        } catch (DateTimeException dte) {
            checkArgument(false, errorMessage + stringDate);
        }
        return localDate;
    }
}
