package com.syonet.pigeon.domain.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
@ToString
@AllArgsConstructor
public enum StatusNews {

    PROCESSED("processed"),
    NO_PROCESSED("no_processed");

    String value;

    public static StatusNews get(String value){
        return Arrays.stream(values())
                .filter(statusNews -> statusNews.value.equals(value))
                .findFirst()
                .orElse(null);
    }

    @Converter(autoApply = true)
    public static class StatusNewsConverter implements AttributeConverter<StatusNews, String> {

        @Override
        public String convertToDatabaseColumn(StatusNews statusNews) {
            return Optional.ofNullable(statusNews)
                    .map(StatusNews::getValue)
                    .orElse(null);
        }

        @Override
        public StatusNews convertToEntityAttribute(String valor) {
            return StatusNews.get(valor);
        }

    }

}
