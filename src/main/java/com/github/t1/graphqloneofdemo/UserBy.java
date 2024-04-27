package com.github.t1.graphqloneofdemo;

import io.smallrye.graphql.api.OneOf;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.graphql.Id;

import static java.util.Locale.ROOT;

@OneOf @Getter @Setter
public class UserBy {
    @Id String id;
    String email;

    public String toSlug() {
        return id != null ? "u" + id : email.toLowerCase(ROOT).replaceAll("@.*", "");
    }
}
