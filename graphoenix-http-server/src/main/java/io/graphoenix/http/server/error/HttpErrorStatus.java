package io.graphoenix.http.server.error;

import java.util.Optional;

public interface HttpErrorStatus {

    Optional<Integer> getStatus(Class<? extends Throwable> type);
}
