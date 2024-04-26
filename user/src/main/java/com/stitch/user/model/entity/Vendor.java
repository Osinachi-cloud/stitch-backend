package com.stitch.user.model.entity;


import com.stitch.user.enums.Tier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "vendor")
@AllArgsConstructor
@NoArgsConstructor
public class Vendor extends User{

    @Column(name = "business_name", unique = true)
    private String businessName;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "vendor_id", unique = true, nullable = false)
    private String vendorId;

    @Column(name = "pin")
    private String pin;

    @Column(name = "pin_attempts")
    protected Integer pinAttempts = 0;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tier")
    private Tier tier = Tier.ONE;

    @Column(name = "country")
    private String country;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "vendor_device", joinColumns = @JoinColumn(name = "vendor_id"), inverseJoinColumns = @JoinColumn(name = "device_id"))
    private Device device;

}
