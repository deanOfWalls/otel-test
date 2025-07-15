package com.example.tracing;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;

public class OtelConfig {
    private static final OpenTelemetry openTelemetry = init();

    private static OpenTelemetry init() {
        String endpoint = System.getenv().getOrDefault("OTEL_EXPORTER_OTLP_ENDPOINT", "http://localhost:4317");

        OtlpGrpcSpanExporter exporter = OtlpGrpcSpanExporter.builder()
                .setEndpoint(endpoint)
                .build();

        SdkTracerProvider provider = SdkTracerProvider.builder()
                .addSpanProcessor(BatchSpanProcessor.builder(exporter).build())
                .build();

        return OpenTelemetrySdk.builder()
                .setTracerProvider(provider)
                .build();
    }

    public static OpenTelemetry getOpenTelemetry() {
        return openTelemetry;
    }
}
