package io.graphoenix.r2dbc.utils;

import io.r2dbc.spi.Result;
import reactor.core.publisher.Mono;

import java.util.Objects;

public final class ResultUtil {

    public static Mono<String> getJsonStringFromResult(Result result) {
        return Mono.from(result.flatMap(ResultUtil::getJsonStringFormSegment));
    }

    public static Mono<Long> getUpdateCountFromResult(Result result) {
        return Mono.from(result.flatMap(ResultUtil::getUpdateCountFormSegment));
    }

    public static Mono<String> getJsonStringFormSegment(Result.Segment segment) {
        if (segment instanceof Result.Message) {
            return Mono.error(((Result.Message) segment).exception());
        } else if (segment instanceof Result.RowSegment) {
            return Mono.just(Objects.requireNonNull(((Result.RowSegment) segment).row().get(0, String.class)));
        } else {
            return Mono.empty();
        }
    }

    public static Mono<Long> getUpdateCountFormSegment(Result.Segment segment) {
        if (segment instanceof Result.Message) {
            return Mono.error(((Result.Message) segment).exception());
        } else if (segment instanceof Result.UpdateCount) {
            return Mono.just(((Result.UpdateCount) segment).value());
        } else {
            return Mono.empty();
        }
    }
}
