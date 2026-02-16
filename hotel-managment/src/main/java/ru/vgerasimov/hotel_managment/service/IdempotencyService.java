package ru.vgerasimov.hotel_managment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vgerasimov.hotel_managment.entity.ProcessedRequest;
import ru.vgerasimov.hotel_managment.repository.ProcessedRequestRepository;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdempotencyService {
    private final ProcessedRequestRepository repository;
    private final ObjectMapper objectMapper;

    public <T> T process(String correlationId, String endpoint, Supplier<T> action, Class<T> responseType) {
        Optional<ProcessedRequest> existing = repository.findByCorrelationId(correlationId);
        if (existing.isPresent()) {
            return objectMapper.readValue(existing.get().getResponseBody(), responseType);
        }
        T result = action.get();
        ProcessedRequest processedRequest = new ProcessedRequest();
        processedRequest.setCorrelationId(correlationId);
        processedRequest.setEndpoint(endpoint);
        processedRequest.setResponseBody(objectMapper.writeValueAsString(result));
        processedRequest.setProcessedAt(LocalDateTime.now());
        repository.save(processedRequest);
        return result;
    }
}
