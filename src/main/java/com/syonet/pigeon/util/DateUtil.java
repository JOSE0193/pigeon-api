package com.syonet.pigeon.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class DateUtil {

    private static final DateTimeFormatter BRAZILIAN_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static LocalDate parseDateFormaterBr(String date){
        if (date == null || date.isEmpty()) {
            throw new IllegalArgumentException("Date cannot be null or empty. Use the format dd/MM/yyyy.");
        }
        try{
            return LocalDate.parse(date, BRAZILIAN_FORMATTER);
        } catch (DateTimeParseException e){
            throw new IllegalArgumentException("Date in invalid format. Use the format dd/MM/yyyy.");
        }
    }

    public static String formatBrazilianDate(LocalDate date) {
        return date.format(BRAZILIAN_FORMATTER);
    }

    public static void validateBrazilianDateNotInFuture(String dateString) {
        LocalDate date = parseDateFormaterBr(dateString);
        validateDateNotInFuture(date);
    }

    public static void validateDateNotInFuture(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth can't be in the future.");
        }
    }

}
