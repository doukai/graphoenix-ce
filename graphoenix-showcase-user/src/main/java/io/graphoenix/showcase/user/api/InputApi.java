package io.graphoenix.showcase.user.api;

import io.graphoenix.showcase.user.dto.inputObjectType.RoleExpression;
import io.graphoenix.showcase.user.dto.inputObjectType.RoleInput;
import io.graphoenix.showcase.user.dto.inputObjectType.UserQueryArguments;
import io.nozdormu.spi.async.Async;
import io.nozdormu.spi.async.Asyncable;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Source;
import reactor.core.publisher.Mono;

@ApplicationScoped
@GraphQLApi
public class InputApi implements Asyncable {

    public UserQueryArguments userQueryArguments1(@Source UserQueryArguments userQueryArguments) {
        return userQueryArguments;
    }

    public Mono<UserQueryArguments> userQueryArguments2(@Source UserQueryArguments userQueryArguments) {
        return Mono.justOrEmpty(userQueryArguments);
    }

    public RoleInput roleInput1(@Source RoleInput roleInput) {
        return roleInput;
    }

    public Mono<RoleInput> roleInput2(@Source RoleInput roleInput) {
        return Mono.justOrEmpty(roleInput);
    }

    @Async
    public RoleInput roleInput3(@Source RoleInput roleInput) {
        RoleInput roleInput2 = await(roleInput2(roleInput));
        return roleInput2;
    }

    public RoleExpression roleExpression1(@Source RoleExpression roleExpression) {
        return roleExpression;
    }

    public Mono<RoleExpression> roleExpression2(@Source RoleExpression roleExpression) {
        return Mono.justOrEmpty(roleExpression);
    }
}
