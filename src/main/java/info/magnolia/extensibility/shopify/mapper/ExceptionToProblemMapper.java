/*
 * This file Copyright (c) 2023 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Magnolia Network Agreement
 * which accompanies this distribution, and is available at
 * http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package info.magnolia.extensibility.shopify.mapper;

import info.magnolia.extensibility.shopify.exception.ShopifyExtensionException;

import java.util.Optional;

import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import com.tietoevry.quarkus.resteasy.problem.HttpProblem;

import io.opentelemetry.api.trace.Span;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class ExceptionToProblemMapper {

    private Span span;

    @Inject
    public ExceptionToProblemMapper(Span span) {
        this.span = span;
    }

    public ExceptionToProblemMapper() {
    }

    @ServerExceptionMapper
    protected Response mapException(ShopifyExtensionException exception) {
        Response.StatusType status = Optional.ofNullable(exception.getStatus())
                .map(problemStatus -> Response.Status.fromStatusCode(problemStatus.getStatusCode()))
                .orElse(Response.Status.INTERNAL_SERVER_ERROR);

        HttpProblem.Builder builder = HttpProblem.builder()
                .with("trace_id", span.getSpanContext().getTraceId())
                .with("span_id", span.getSpanContext().getSpanId())
                .withTitle(exception.getTitle())
                .withStatus(status)
                .withDetail(exception.getDetail());

        return builder.build().toResponse();
    }

    @ServerExceptionMapper
    protected Response mapException(Exception exception) {
        HttpProblem.Builder builder = HttpProblem.builder()
                .with("trace_id", span.getSpanContext().getTraceId())
                .with("span_id", span.getSpanContext().getSpanId())
                .withDetail(exception.getMessage());
        return builder.build().toResponse();
    }
}