package com.example.tracing;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;

import java.util.function.Supplier;

public class TraceHelper {
    private static final Tracer tracer = OtelConfig.getOpenTelemetry().getTracer("com.example.tracing");

    public static Span startHttpSpan(String name, String method, String url, String route) {
        return tracer.spanBuilder(name)
                .setSpanKind(SpanKind.SERVER)
                .startSpan()
                .setAttribute(SemanticAttributes.HTTP_METHOD, method)
                .setAttribute(SemanticAttributes.HTTP_ROUTE, route)
                .setAttribute(SemanticAttributes.HTTP_URL, url);
    }

    public static void withSpan(String name, Runnable task) {
        Span span = tracer.spanBuilder(name).startSpan();
        try (Scope scope = span.makeCurrent()) {
            task.run();
        } finally {
            span.end();
        }
    }

    public static <T> T withSpan(String name, Supplier<T> task) {
        Span span = tracer.spanBuilder(name).startSpan();
        try (Scope scope = span.makeCurrent()) {
            return task.get();
        } finally {
            span.end();
        }
    }

    public static void withHttpSpan(String name, String method, String url, String route, Runnable task) {
        Span span = startHttpSpan(name, method, url, route);
        try (Scope scope = span.makeCurrent()) {
            task.run();
        } finally {
            span.end();
        }
    }
}
