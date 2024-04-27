package test;

import io.smallrye.graphql.client.typesafe.api.GraphQLClientApi;
import io.smallrye.graphql.client.typesafe.api.TypesafeGraphQLClientBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.Input;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Query;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class UsersIT {
    @GraphQLClientApi
    interface Api {
        @Query User user(@NonNull By by);
    }

    @Data public static class User {
        String slug;
    }

    @Input("UserByInput")
    @Data @AllArgsConstructor public static class By {
        public static By id(String id) {return new By(id, null);}

        public static By email(String email) {return new By(null, email);}

        @Id String id;
        String email;
    }

    Api api = TypesafeGraphQLClientBuilder.newBuilder().endpoint("http://localhost:8080/graphql").build(Api.class);

    @Test void shouldGetUserById() {
        var user = api.user(By.id("42"));

        then(user.slug).isEqualTo("u42");
    }

    @Test void shouldGetUserByEmail() {
        var user = api.user(By.email("alice@nowhere.com"));

        then(user.slug).isEqualTo("alice");
    }
}
