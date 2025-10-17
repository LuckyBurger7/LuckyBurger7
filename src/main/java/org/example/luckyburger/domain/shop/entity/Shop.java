package org.example.luckyburger.domain.shop.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.common.entity.BaseEntity;
import org.example.luckyburger.domain.shop.dto.request.CreateShopRequest;
import org.example.luckyburger.domain.shop.dto.request.UpdateShopRequest;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;

@Getter
@Entity
@Table
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

//    @Builder
//    public static Shop of(String name, BusinessStatus status, String address, String street) {
//
//        return new Shop(name, status, address, street);
//    }

    public static Shop createFrom(CreateShopRequest shopRequest) {

        return Shop.builder()
                .name(shopRequest.getName())
                .status(shopRequest.getStatus())
                .address(shopRequest.getAddress())
                .street(shopRequest.getStreet())
                .build();
    }

    public Shop updateFrom(UpdateShopRequest shopRequest) {

        if (shopRequest.getName() != null) {
            this.name = shopRequest.getName();
        }
        if (shopRequest.getStatus() != null) {
            this.status = shopRequest.getStatus();
        }
        if (shopRequest.getAddress() != null) {
            this.address = shopRequest.getAddress();
        }
        if (shopRequest.getStreet() != null) {
            this.street = shopRequest.getStreet();
        }

        return this;
    }

    public void changeShop(BusinessStatus status) {
        this.status = status;
    }
}
