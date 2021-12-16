package com.uptosmth.chronos.api;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.HttpHeaders;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.common.ResponseHeaders;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.annotation.RequestConverterFunction;
import com.linecorp.armeria.server.annotation.ResponseConverterFunction;

import com.uptosmth.chronos.api.exception.BadRequestException;
import com.uptosmth.chronos.infra.DefaultObjectMapper;

public class JsonConverter implements RequestConverterFunction, ResponseConverterFunction {
    private static final Logger log = LogManager.getLogger(JsonConverter.class);
    private static final ObjectMapper objectMapper = DefaultObjectMapper.get();

    @Override
    public Object convertRequest(
            ServiceRequestContext ctx,
            AggregatedHttpRequest request,
            Class<?> expectedResultType,
            ParameterizedType parameterizedType) {

        String schemaPath =
                String.format(
                        "/api/schema/%s.json",
                        camelToSnake(expectedResultType.getSimpleName().replace("[]", "")));

        try {
            JsonNode json = objectMapper.readTree(request.content().toInputStream());

            JsonSchemaValidator.validate(json, schemaPath);

            return objectMapper.treeToValue(json, expectedResultType);
        } catch (IOException e) {
            log.error("Json validation failed", e);
            throw new BadRequestException();
        }
    }

    @Override
    public HttpResponse convertResponse(
            ServiceRequestContext ctx, ResponseHeaders headers, Object result, HttpHeaders trailers)
            throws Exception {

        if (result instanceof HttpResponse) {
            return ResponseConverterFunction.fallthrough();
        }

        ObjectMapper objectMapper = DefaultObjectMapper.get();
        String asJson = objectMapper.writeValueAsString(result);

        return HttpResponse.of(HttpStatus.OK, MediaType.JSON_UTF_8, asJson);
    }

    private static String camelToSnake(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z]+)", "$1-$2").toLowerCase();
    }
}
