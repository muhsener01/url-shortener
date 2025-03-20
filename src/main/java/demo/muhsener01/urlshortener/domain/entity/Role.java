package demo.muhsener01.urlshortener.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity<String> {


    protected Role() {
    }

    public Role(RoleName roleName) {
        this.id = roleName.name();
    }

    public String getName() {
        return id;
    }
}
