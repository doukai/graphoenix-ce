package io.graphoenix.spi.handler;

import reactor.core.publisher.Mono;

import java.util.Collection;

public interface TypeEmptyHandler {

    Mono<Void> empty(String... typeNames);

    Mono<Void> empty(Collection<String> typeNames);
}
