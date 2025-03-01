package com.me.globetrotter.entity.attributeconverter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Converter
public class ListToStringConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (CollectionUtils.isNotEmpty(attribute)) {
            return String.join("|", attribute);
        }
        return null;
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (StringUtils.isNotBlank(dbData)) {
            return Arrays.asList(StringUtils.splitPreserveAllTokens(dbData, "|"));
        }
        return new ArrayList<>();
    }
}
