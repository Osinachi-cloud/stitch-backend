package com.stitch.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import com.stitch.user.model.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "vendor_like")
public class VendorLike extends BaseEntity {

    @Column(name = "vendor_email_address")
    private String vendorEmailAddress;

    @ManyToOne
    @JoinColumn(name = "email_address", referencedColumnName = "email_address")
    private UserEntity userEntity;
}
