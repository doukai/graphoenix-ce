package io.graphoenix.showcase.user.api;

import io.graphoenix.showcase.user.dto.objectType.Role;
import io.graphoenix.showcase.user.dto.objectType.User;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@GraphQLApi
public class UserApi {

    public String getNickName(@Source User user) {
        return "Killer";
    }

    public Mono<Integer> getAge(@Source User user) {
        return Mono.just(18);
    }

    public Flux<String> getAddress(@Source User user) {
        return Flux.just("Shandong", "Guangdong");
    }

    @Query
    public LocalDateTime currentTime(String local) {
        return LocalDateTime.now();
    }

    @Query
    public Mono<String> appName(Role role) {
        return Mono.just("Graphoenix");
    }

    @Query
    public Flux<String> getLoginName() {
        return Flux.just("Zhang San", "Li Si");
    }
}
