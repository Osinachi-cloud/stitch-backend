package com.stitch.user.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
//import org.hibernate.envers.Audited;

import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@Audited
@Table(name = "permission")
@ToString
public class Permission extends BaseEntity {

    @Column(name = "name", unique=true, nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "category", nullable = false)
    private String category;

    @ManyToMany(mappedBy = "permissions",fetch = FetchType.EAGER)
    private Collection<Role> roles;

    public Permission(String name, String description, String category){
        this.name = name;
        this.description = description;
        this.category = category;
    }
}


