package org.example.stablecoinchecker.scheduler.infra.cex;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonUtils {

    private final ObjectMapper mapper;

    public <T> String serialize(T data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> Optional<T> deserialize(String payload, Class<T> valueType) {
        try {
            T t = mapper.readValue(payload, valueType);

            return Optional.of(t);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public <T> Optional<T> deserialize(ByteBuffer payload, Class<T> valueType) {
        try {
            byte[] bytes = new byte[payload.remaining()];
            payload.get(bytes);
            String jsonString = new String(bytes, StandardCharsets.UTF_8);

            return deserialize(jsonString, valueType);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
