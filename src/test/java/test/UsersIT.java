package test;

import io.smallrye.graphql.client.typesafe.api.GraphQLClientApi;
import io.smallrye.graphql.client.typesafe.api.TypesafeGraphQLClientBuilder;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.Input;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Query;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class UsersIT {
    @GraphQLClientApi
    interface Api {
        @Query User user(@NonNull By by);

        @Mutation String add(UserOrComment oneOf);
    }

    record User(String slug) {}

    record Comment(String text) {}

    @Input("UserByInput")
    @SuppressWarnings("SameParameterValue")
    record By(@Id String id, String email) {
        static By id(String id) {return new By(id, null);}

        static By email(String email) {return new By(null, email);}
    }

    record UserOrComment(User user, Comment comment) {
        static UserOrComment of(User user) {return new UserOrComment(user, null);}

        static UserOrComment of(Comment comment) {return new UserOrComment(null, comment);}
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

    @Test void shouldAddComment() {
        var added = api.add(UserOrComment.of(new Comment("foo")));

        then(added).isEqualTo("added Comment(text=foo)");
    }

    @Test void shouldAddUser() {
        var added = api.add(UserOrComment.of(new User("slug")));

        then(added).isEqualTo("added User(slug=slug, name=null)");
    }
}
