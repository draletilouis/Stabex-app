package com.demo.banking_app.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

@Component
@Converter(autoApply = false)
public class EncryptedStringConverter implements AttributeConverter<String, String> {

    private static CryptoService cryptoService;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        ensureCrypto();
        return cryptoService.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        ensureCrypto();
        return cryptoService.decrypt(dbData);
    }

    private void ensureCrypto() {
        if (cryptoService == null) {
            cryptoService = ApplicationContextProvider.getBean(CryptoService.class);
        }
    }
}


