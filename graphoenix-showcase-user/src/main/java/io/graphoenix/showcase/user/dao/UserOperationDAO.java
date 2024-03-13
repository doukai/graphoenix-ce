package io.graphoenix.showcase.user.dao;

import io.graphoenix.core.dto.annotation.StringExpression1;
import io.graphoenix.showcase.user.dto.annotation.Query;
import io.graphoenix.showcase.user.dto.annotation.UserQueryArguments;
import io.graphoenix.showcase.user.dto.objectType.User;
import io.graphoenix.spi.annotation.GraphQLOperation;
import io.graphoenix.spi.annotation.SelectionSet;
import reactor.core.publisher.Mono;

@GraphQLOperation
public interface UserOperationDAO {

    @Query(
            user = @UserQueryArguments(login = @StringExpression1($val = "login"))
    )
    @SelectionSet("{id password}")
    Mono<User> getUserByLogin(String login);
}
