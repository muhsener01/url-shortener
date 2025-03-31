package demo.muhsener01.urlshortener.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Table(name = "users")
public class MyUser extends BaseEntity<UUID> {


    private String username;
    private String email;
    private String password;

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            uniqueConstraints = @UniqueConstraint(name = "primary_key_userId_roleId", columnNames = {"user_id", "role_id"}))
    private List<Role> roles;


    private MyUser(Builder builder) {
        id = builder.id;
        createdAt = builder.createdAt;
        updateAt = builder.updateAt;
        username = builder.username;
        email = builder.email;
        password = builder.password;
        this.roles = builder.roles;


        if (id == null) {
            initialize();
        }
    }


    private void initialize() {
        validate();
        this.id = UUID.randomUUID();
    }

    private void validate() {
        //TODO: implement here!!
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private UUID id;
        private LocalDateTime createdAt;
        private LocalDateTime updateAt;
        private String username;
        private String email;
        private String password;
        private List<Role> roles;

        private Builder() {
        }

        public Builder id(UUID val) {
            id = val;
            return this;
        }

        public Builder createdAt(LocalDateTime val) {
            createdAt = val;
            return this;
        }

        public Builder updateAt(LocalDateTime val) {
            updateAt = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public Builder password(String val) {
            password = val;
            return this;
        }

        public Builder roles(List<Role> val) {
            roles = val;
            return this;
        }


        public MyUser build() {
            return new MyUser(this);
        }
    }
}
