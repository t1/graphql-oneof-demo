package com.github.t1.graphqloneofdemo;

import io.smallrye.graphql.api.OneOf;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Query;

import static java.util.Locale.ROOT;

@GraphQLApi
public class Users {
    @Query public User user(@NonNull UserBy by) {
        return new User(by.toSlug(), "Alice Brown");
    }

    @OneOf public record UserBy(@Id String id, String email) {
        public String toSlug() {
            return id != null ? "u" + id : email.toLowerCase(ROOT).replaceAll("@.*", "");
        }
    }


    @Mutation public String addUser(User user) {
        return "added " + user;
    }

    @Mutation public String addComment(Comment comment) {
        return "added " + comment;
    }

    @Mutation public String add(UserOrComment oneOf) {
        return oneOf.user != null ? addUser(oneOf.user) : addComment(oneOf.comment);
    }

    @OneOf public record UserOrComment(User user, Comment comment) {}
}
