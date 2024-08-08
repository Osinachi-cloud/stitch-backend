package com.stitch.user.model.entity;


import com.stitch.commons.model.entity.BaseEntity;
import com.stitch.user.enums.IdentityType;
import lombok.Data;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;


@Data
@Audited
@Entity
@Table(name = "identity_document")
public class IdentityDocument extends BaseEntity {

    @Column(name = "citizenship")
    private String citizenship;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "identity_type")
    private IdentityType identityType;

    @Column(name = "identity_number")
    private String IdentityNumber;

    @Lob
    @Column(name = "document")
    private String document; // base64 image
}
