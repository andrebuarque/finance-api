package br.com.andrebuarque.financeapi.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {
    @Id
    private String id;
    private String username;
    private String name;
    private String lastname;
    private String email;

    public static class Builder {
        private User user;

        Builder() {
            user = new User();
        }

        public Builder id(String id) {
            user.setId(id);
            return this;
        }

        public Builder username(String username) {
            user.setUsername(username);
            return this;
        }

        public Builder name(String name) {
            user.setName(name);
            return this;
        }

        public Builder lastname(String lastname) {
            user.setLastname(lastname);
            return this;
        }

        public Builder email(String email) {
            user.setEmail(email);
            return this;
        }

        public User build() {
            return user;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
