package io.graphoenix.r2dbc.transaction;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import io.graphoenix.r2dbc.connection.ConnectionProvider;
import io.r2dbc.spi.Connection;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.transaction.InvalidTransactionException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.TransactionRequiredException;
import jakarta.transaction.Transactional;
import org.reactivestreams.Publisher;
import org.tinylog.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Arrays;

import static io.graphoenix.r2dbc.context.TransactionScopeInstanceFactory.TRANSACTION_ID;
import static io.graphoenix.spi.constant.Hammurabi.*;

@ApplicationScoped
@Named("r2dbc")
@Transactional
@Priority(0)
@Interceptor
public class R2DBCTransactionInterceptor {

    private final ConnectionProvider connectionProvider;

    @Inject
    public R2DBCTransactionInterceptor(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @AroundInvoke
    @SuppressWarnings({"unchecked"})
    public Object aroundInvoke(InvocationContext invocationContext) {
        Transactional.TxType txType;
        Class<? extends Exception>[] rollbackOn;
        Class<? extends Exception>[] dontRollbackOn;

        try {
            if (invocationContext.getContextData().containsKey("value")) {
                txType = (Transactional.TxType) invocationContext.getContextData().get("value");
            } else {
                txType = (Transactional.TxType) Transactional.class.getDeclaredMethod("value").getDefaultValue();
            }
            if (invocationContext.getContextData().containsKey("rollbackOn")) {
                rollbackOn = (Class<? extends Exception>[]) invocationContext.getContextData().get("rollbackOn");
            } else {
                rollbackOn = (Class<? extends Exception>[]) Transactional.class.getDeclaredMethod("rollbackOn").getDefaultValue();
            }
            if (invocationContext.getContextData().containsKey("dontRollbackOn")) {
                dontRollbackOn = (Class<? extends Exception>[]) invocationContext.getContextData().get("dontRollbackOn");
            } else {
                dontRollbackOn = (Class<? extends Exception>[]) Transactional.class.getDeclaredMethod("dontRollbackOn").getDefaultValue();
            }

            String transactionId = NanoIdUtils.randomNanoId();
            if (invocationContext.getMethod().getReturnType().isAssignableFrom(Mono.class)) {
                switch (txType) {
                    case REQUIRED:
                        return connectionProvider.inTransaction()
                                .flatMap(inTransaction ->
                                        inTransaction ?
                                                Mono.from(proceed(invocationContext)) :
                                                Mono
                                                        .usingWhen(
                                                                connectionProvider.get(),
                                                                connection -> Mono.from(connection.setAutoCommit(false))
                                                                        .then(Mono.from(connection.beginTransaction()))
                                                                        .then(Mono.from(proceed(invocationContext))),
                                                                connection -> Mono.from(connection.commitTransaction())
                                                                        .thenEmpty(connection.close()),
                                                                (connection, throwable) -> Mono.from(errorProcess(connection, throwable, rollbackOn, dontRollbackOn))
                                                                        .thenEmpty(connection.close())
                                                                        .thenEmpty(Mono.error(throwable)),
                                                                connection -> Mono.from(connection.rollbackTransaction())
                                                                        .thenEmpty(connection.close())
                                                        )
                                                        .contextWrite(Context.of(TRANSACTION_ID, transactionId, IN_TRANSACTION, true, ROLLBACK_ON, rollbackOn, DONT_ROLLBACK_ON, dontRollbackOn))
                                );
                    case REQUIRES_NEW:
                        return Mono
                                .usingWhen(
                                        connectionProvider.get(),
                                        connection -> Mono.from(connection.setAutoCommit(false))
                                                .then(Mono.from(connection.beginTransaction()))
                                                .then(Mono.from(proceed(invocationContext))),
                                        connection -> Mono.from(connection.commitTransaction())
                                                .thenEmpty(connection.close()),
                                        (connection, throwable) -> Mono.from(errorProcess(connection, throwable, rollbackOn, dontRollbackOn))
                                                .thenEmpty(connection.close())
                                                .thenEmpty(Mono.error(throwable)),
                                        connection -> Mono.from(connection.rollbackTransaction())
                                                .thenEmpty(connection.close())
                                )
                                .contextWrite(Context.of(TRANSACTION_ID, transactionId, IN_TRANSACTION, true, ROLLBACK_ON, rollbackOn, DONT_ROLLBACK_ON, dontRollbackOn));
                    case MANDATORY:
                        return connectionProvider.inTransaction()
                                .flatMap(inTransaction ->
                                        inTransaction ?
                                                Mono.from(proceed(invocationContext)) :
                                                Mono.error(new TransactionRequiredException())
                                );
                    case SUPPORTS:
                        return Mono
                                .usingWhen(
                                        connectionProvider.get(),
                                        connection -> Mono.from(connection.setAutoCommit(true))
                                                .then(Mono.from(proceed(invocationContext))),
                                        Connection::close
                                )
                                .contextWrite(Context.of(TRANSACTION_ID, transactionId, IN_TRANSACTION, false));
                    case NOT_SUPPORTED:
                        return connectionProvider.inTransaction()
                                .flatMap(inTransaction ->
                                        inTransaction ?
                                                Mono.from(proceed(invocationContext)) :
                                                Mono
                                                        .usingWhen(
                                                                connectionProvider.get(),
                                                                connection -> Mono.from(connection.setAutoCommit(true))
                                                                        .then(Mono.from(proceed(invocationContext))),
                                                                Connection::close
                                                        )
                                                        .contextWrite(Context.of(TRANSACTION_ID, transactionId, IN_TRANSACTION, false))

                                );
                    case NEVER:
                        return connectionProvider.inTransaction()
                                .flatMap(inTransaction ->
                                        inTransaction ?
                                                Mono.error(new InvalidTransactionException()) :
                                                Mono
                                                        .usingWhen(
                                                                connectionProvider.get(),
                                                                connection -> Mono.from(connection.setAutoCommit(true))
                                                                        .then(Mono.from(proceed(invocationContext))),
                                                                Connection::close
                                                        )
                                                        .contextWrite(Context.of(TRANSACTION_ID, transactionId, IN_TRANSACTION, false))
                                );
                    default:
                        throw new NotSupportedException();
                }
            } else if (invocationContext.getMethod().getReturnType().isAssignableFrom(Flux.class)) {
                switch (txType) {
                    case REQUIRED:
                        return connectionProvider.inTransaction()
                                .flatMapMany(inTransaction ->
                                        inTransaction ?
                                                Flux.from(proceed(invocationContext)) :
                                                Flux
                                                        .usingWhen(
                                                                connectionProvider.get(),
                                                                connection -> Flux.from(connection.setAutoCommit(false))
                                                                        .thenMany(Flux.from(connection.beginTransaction()))
                                                                        .thenMany(Flux.from(proceed(invocationContext))),
                                                                connection -> Flux.from(connection.commitTransaction())
                                                                        .thenEmpty(connection.close()),
                                                                (connection, throwable) -> Flux.from(errorProcess(connection, throwable, rollbackOn, dontRollbackOn))
                                                                        .thenEmpty(connection.close())
                                                                        .thenEmpty(Flux.error(throwable)),
                                                                connection -> Flux.from(connection.rollbackTransaction())
                                                                        .thenEmpty(connection.close())
                                                        )
                                                        .contextWrite(Context.of(TRANSACTION_ID, transactionId, IN_TRANSACTION, true, ROLLBACK_ON, rollbackOn, DONT_ROLLBACK_ON, dontRollbackOn))
                                );
                    case REQUIRES_NEW:
                        return Flux
                                .usingWhen(
                                        connectionProvider.get(),
                                        connection -> Flux.from(connection.setAutoCommit(false))
                                                .thenMany(Flux.from(connection.beginTransaction()))
                                                .thenMany(Flux.from(proceed(invocationContext))),
                                        connection -> Flux.from(connection.commitTransaction())
                                                .thenEmpty(connection.close()),
                                        (connection, throwable) -> Flux.from(errorProcess(connection, throwable, rollbackOn, dontRollbackOn))
                                                .thenEmpty(connection.close())
                                                .thenEmpty(Flux.error(throwable)),
                                        connection -> Flux.from(connection.rollbackTransaction())
                                                .thenEmpty(connection.close())
                                )
                                .contextWrite(Context.of(TRANSACTION_ID, transactionId, IN_TRANSACTION, true, ROLLBACK_ON, rollbackOn, DONT_ROLLBACK_ON, dontRollbackOn));
                    case MANDATORY:
                        return connectionProvider.inTransaction()
                                .flatMapMany(inTransaction ->
                                        inTransaction ?
                                                Flux.from(proceed(invocationContext)) :
                                                Flux.error(new TransactionRequiredException())
                                );
                    case SUPPORTS:
                        return Flux
                                .usingWhen(
                                        connectionProvider.get(),
                                        connection -> Flux.from(connection.setAutoCommit(true))
                                                .thenMany(Flux.from(proceed(invocationContext))),
                                        Connection::close
                                )
                                .contextWrite(Context.of(TRANSACTION_ID, transactionId, IN_TRANSACTION, false));
                    case NOT_SUPPORTED:
                        return connectionProvider.inTransaction()
                                .flatMapMany(inTransaction ->
                                        inTransaction ?
                                                Flux.from(proceed(invocationContext)) :
                                                Flux
                                                        .usingWhen(
                                                                connectionProvider.get(),
                                                                connection -> Flux.from(connection.setAutoCommit(true))
                                                                        .thenMany(Flux.from(proceed(invocationContext))),
                                                                Connection::close
                                                        )
                                                        .contextWrite(Context.of(TRANSACTION_ID, transactionId, IN_TRANSACTION, false))
                                );
                    case NEVER:
                        return connectionProvider.inTransaction()
                                .flatMapMany(inTransaction ->
                                        inTransaction ?
                                                Flux.error(new InvalidTransactionException()) :
                                                Flux
                                                        .usingWhen(
                                                                connectionProvider.get(),
                                                                connection -> Flux.from(connection.setAutoCommit(true))
                                                                        .thenMany(Flux.from(proceed(invocationContext))),
                                                                Connection::close
                                                        )
                                                        .contextWrite(Context.of(TRANSACTION_ID, transactionId, IN_TRANSACTION, false))
                                );
                    default:
                        throw new NotSupportedException();
                }
            }
            throw new NotSupportedException();
        } catch (Exception exception) {
            if (invocationContext.getMethod().getReturnType().isAssignableFrom(Mono.class)) {
                return Mono.error(exception);
            } else if (invocationContext.getMethod().getReturnType().isAssignableFrom(Flux.class)) {
                return Flux.error(exception);
            } else {
                throw new RuntimeException(exception);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Publisher<Object> proceed(InvocationContext invocationContext) {
        try {
            return (Publisher<Object>) invocationContext.proceed();
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    private Publisher<Void> errorProcess(Connection connection, Throwable throwable, Class<? extends Exception>[] rollbackOn, Class<? extends Exception>[] dontRollbackOn) {
        Logger.error(throwable);
        if (dontRollbackOn != null && dontRollbackOn.length > 0) {
            if (Arrays.stream(dontRollbackOn).anyMatch(exception -> exception.equals(throwable.getClass()))) {
                return connection.commitTransaction();
            } else {
                return connection.rollbackTransaction();
            }
        } else if (rollbackOn != null && rollbackOn.length > 0) {
            if (Arrays.stream(rollbackOn).anyMatch(exception -> exception.equals(throwable.getClass()))) {
                return connection.rollbackTransaction();
            } else {
                return connection.commitTransaction();
            }
        }
        return connection.rollbackTransaction();
    }
}
