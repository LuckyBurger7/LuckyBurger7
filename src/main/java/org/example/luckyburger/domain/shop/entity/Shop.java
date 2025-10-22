package org.example.luckyburger.domain.shop.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.common.entity.BaseEntity;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;

@Getter
@Entity
@Table(name = "shops")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shop extends BaseEntity {

    @Column(length = 100, nullable = false)
    private String name;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private BusinessStatus status;

    @Column(length = 255, nullable = false)
    private String address;

    @Column(length = 100, nullable = false)
    private String street;

    @Builder
    private Shop(String name, BusinessStatus status, String address, String street) {
        this.name = name;
        this.status = status;
        this.address = address;
        this.street = street;
    }

    public static Shop of(String name, BusinessStatus status, String address, String street) {

        return new Shop(name, status, address, street);
    }

    public void updateShop(String name, String address, String street) {

        this.name = name;
        this.address = address;
        this.street = street;
    }

    public void updateShopStatus(BusinessStatus status) {
        this.status = status;
    }

}
