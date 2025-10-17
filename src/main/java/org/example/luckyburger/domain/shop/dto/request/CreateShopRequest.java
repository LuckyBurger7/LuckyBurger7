package org.example.luckyburger.domain.shop.dto.request;

import lombok.Getter;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;

@Getter
public class CreateShopRequest {

    String name;
    BusinessStatus status;
    String address;
    String street;

}
