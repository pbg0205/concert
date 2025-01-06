package com.cooper.concert.common.jpa.converter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LocalTimeToLongConverter implements AttributeConverter<LocalDateTime, Long> {

	@Override
	public Long convertToDatabaseColumn(final LocalDateTime attribute) {
		if (attribute == null) {
			return null;
		}
		return attribute.toInstant(ZoneOffset.UTC).toEpochMilli();
	}

	@Override
	public LocalDateTime convertToEntityAttribute(final Long dbData) {
		// Epoch milli를 LocalDateTime으로 변환
		return LocalDateTime.ofEpochSecond(dbData / 1000, (int)(dbData % 1000) * 1_000_000, ZoneOffset.UTC);
	}
}
