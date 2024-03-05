package io.graphoenix.showcase.user.api;

import io.graphoenix.showcase.user.dto.objectType.Role;
import io.graphoenix.showcase.user.dto.objectType.User;
import io.nozdormu.spi.async.Async;
import io.nozdormu.spi.async.Asyncable;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@ApplicationScoped
@GraphQLApi
public class UserApi implements Asyncable {

    public String getNickName(@Source User user) {
        return "Killer";
    }

    public Mono<Integer> getAge(@Source User user) {
        return Mono.just(18);
    }

    @Async
    public Integer getAge2(@Source User user) {
        Integer age = await(getAge(user));
        return age;
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
    @Async
    public String appName2(Role role) {
        String appName = await(appName(role));
        return appName;
    }

    @Query
    public Flux<String> getLoginName() {
        return Flux.just("Zhang San", "Li Si");
    }
}
