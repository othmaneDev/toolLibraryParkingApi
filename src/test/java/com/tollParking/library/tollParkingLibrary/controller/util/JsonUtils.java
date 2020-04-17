package com.tollParking.library.tollParkingLibrary.controller.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor
public final class JsonUtils {

    public static byte[] convertObjetToJsonAsByte(Object object) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper.writeValueAsBytes(object);
    }

}
