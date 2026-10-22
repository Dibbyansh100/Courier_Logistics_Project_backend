package com.logistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard API response wrapper.
 *
 * @param <T> type of the payload data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseStructure<T> {
    /**
     * HTTP status code (e.g., 200, 201, 400, 404).
     */
    private int statusCode;

    /**
     * Human‑readable message describing the result.
     */
    private String message;

    /**
     * The payload data. May be {@code null} for responses without a body.
     */
    private T data;
}
