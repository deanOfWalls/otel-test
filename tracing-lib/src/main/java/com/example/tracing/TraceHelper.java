package com.example.tracing;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class TraceHelper {
    private static final Logger logger = LoggerFactory.getLogger(TraceHelper.class);
    private static final Tracer tracer = OtelConfig.getOpenTelemetry().getTracer("com.example.tracing");

    public static Span startHttpSpan(String name, String method, String url, String route) {
        logger.debug("Starting HTTP span: {}", name);
        return tracer.spanBuilder(name)
                .setSpanKind(SpanKind.SERVER)
                .startSpan()
                .setAttribute(SemanticAttributes.HTTP_METHOD, method)
                .setAttribute(SemanticAttributes.HTTP_ROUTE, route)
                .setAttribute(SemanticAttributes.HTTP_URL, url);
    }

    public static void withSpan(String name, Runnable task) {
        logger.debug("Starting span: {}", name);
        Span span = tracer.spanBuilder(name).startSpan();
        try (Scope scope = span.makeCurrent()) {
            task.run();
        } finally {
            logger.debug("Ending span: {}", name);
            span.end();
        }
    }

    public static <T> T withSpan(String name, Supplier<T> task) {
        logger.debug("Starting span: {}", name);
        Span span = tracer.spanBuilder(name).startSpan();
        try (Scope scope = span.makeCurrent()) {
            return task.get();
        } finally {
            logger.debug("Ending span: {}", name);
            span.end();
        }
    }

    public static void withHttpSpan(String name, String method, String url, String route, Runnable task) {
        logger.debug("Starting HTTP span with task: {}", name);
        Span span = startHttpSpan(name, method, url, route);
        try (Scope scope = span.makeCurrent()) {
            task.run();
        } finally {
            logger.debug("Ending HTTP span with task: {}", name);
            span.end();
        }
    }
}
