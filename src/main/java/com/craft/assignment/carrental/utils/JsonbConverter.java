package com.craft.assignment.carrental.utils;


import com.craft.assignment.carrental.models.Address;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter
public class JsonbConverter implements AttributeConverter<Address, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Address attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            // Handle the exception or log an error
            return null;
        }
    }

    @Override
    public Address convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, Address.class);
        } catch (Exception e) {
            // Handle the exception or log an error
            return null;
        }
    }
}
