package com.github.t1.graphqloneofdemo;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Query;

@GraphQLApi
public class Users {
    @Query public User user(@NonNull UserBy by) {
        return new User(by.toSlug(), "Alice Brown");
    }
}
