package io.graphoenix.rabbitmq.produces;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.graphoenix.rabbitmq.config.RabbitMQConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.*;

@ApplicationScoped
public class RabbitMQProducer {

    private final RabbitMQConfig rabbitMQConfig;

    @Inject
    public RabbitMQProducer(RabbitMQConfig rabbitMQConfig) {
        this.rabbitMQConfig = rabbitMQConfig;
    }

    @Produces
    @ApplicationScoped
    public Mono<Connection> connectionMono() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.useNio();
        connectionFactory.setHost(rabbitMQConfig.getHost());
        connectionFactory.setPort(rabbitMQConfig.getPort());
        connectionFactory.setUsername(rabbitMQConfig.getUsername());
        connectionFactory.setPassword(rabbitMQConfig.getPassword());
        connectionFactory.setVirtualHost(rabbitMQConfig.getVirtualHost());
        connectionFactory.setRequestedChannelMax(rabbitMQConfig.getRequestedChannelMax());
        connectionFactory.setRequestedFrameMax(rabbitMQConfig.getRequestedFrameMax());
        connectionFactory.setRequestedHeartbeat(rabbitMQConfig.getRequestedHeartbeat());
        connectionFactory.setConnectionTimeout(rabbitMQConfig.getConnectionTimeout());
        connectionFactory.setHandshakeTimeout(rabbitMQConfig.getHandshakeTimeout());
        connectionFactory.setShutdownTimeout(rabbitMQConfig.getShutdownTimeout());
        connectionFactory.setAutomaticRecoveryEnabled(rabbitMQConfig.getAutomaticRecovery());
        connectionFactory.setTopologyRecoveryEnabled(rabbitMQConfig.getTopologyRecovery());
        connectionFactory.setNetworkRecoveryInterval(rabbitMQConfig.getNetworkRecoveryInterval());
        connectionFactory.setChannelRpcTimeout(rabbitMQConfig.getChannelRpcTimeout());
        connectionFactory.setChannelShouldCheckRpcResponseType(rabbitMQConfig.getChannelShouldCheckRpcResponseType());
        connectionFactory.setWorkPoolTimeout(rabbitMQConfig.getWorkPoolTimeout());
        return Mono.fromCallable(() -> connectionFactory.newConnection()).cache();
    }

    @Produces
    @ApplicationScoped
    public Sender sender() {
        return RabbitFlux.createSender(new SenderOptions().connectionMono(connectionMono()));
    }

    @Produces
    @ApplicationScoped
    public Receiver receiver() {
        return RabbitFlux.createReceiver(new ReceiverOptions().connectionMono(connectionMono()));
    }
}
