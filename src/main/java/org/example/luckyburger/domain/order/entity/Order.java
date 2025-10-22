package org.example.luckyburger.domain.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.common.entity.BaseIdEntity;
import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.example.luckyburger.domain.order.enums.OrderStatus;
import org.example.luckyburger.domain.order.exception.OrderNotCancelableException;
import org.example.luckyburger.domain.order.exception.OrderStatusInvalidUpdateException;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.user.entity.User;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Order extends BaseIdEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 50, nullable = false)
    private String receiver;

    @Column(length = 50, nullable = false)
    private String phone;

    @Column(length = 255, nullable = false)
    private String address;

    @Column(length = 100, nullable = false)
    private String street;

    @Column(length = 255)
    private String request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    private Integer point;

    private long totalPrice;

    private long pay;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    private Order(
            Shop shop,
            User user,
            String receiver,
            String phone,
            String address,
            String street,
            String request,
            Coupon coupon,
            Integer point,
            long totalPrice,
            long pay,
            LocalDateTime orderDate,
            OrderStatus status) {
        this.shop = shop;
        this.user = user;
        this.receiver = receiver;
        this.phone = phone;
        this.address = address;
        this.street = street;
        this.request = request;
        this.coupon = coupon;
        this.point = point;
        this.totalPrice = totalPrice;
        this.pay = pay;
        this.orderDate = orderDate;
        this.status = status;
    }

    public static Order of(
            Shop shop,
            User user,
            String receiver,
            String phone,
            String address,
            String street,
            String request,
            Coupon coupon,
            Integer point,
            long totalPrice,
            long pay,
            LocalDateTime orderDate,
            OrderStatus status) {
        return new Order(
                shop,
                user,
                receiver,
                phone,
                address,
                street,
                request,
                coupon,
                point,
                totalPrice,
                pay,
                orderDate,
                status
        );
    }

    public void cancelByUser() {
        if (this.status == OrderStatus.WAITING) this.status = OrderStatus.CANCEL;
        else throw new OrderNotCancelableException();
    }

    public void updateStatusByOwner(OrderStatus status) {
        if (this.status == status) return;

        switch (this.status) {
            case WAITING -> {
                if (status == OrderStatus.COOKING || status == OrderStatus.CANCEL) this.status = status;
                else throw new OrderStatusInvalidUpdateException();
            }
            case COOKING -> {
                if (status == OrderStatus.ON_DELIVERY) this.status = status;
                else throw new OrderStatusInvalidUpdateException();
            }
            case ON_DELIVERY -> {
                if (status == OrderStatus.COMPLETED) this.status = status;
                else throw new OrderStatusInvalidUpdateException();
            }
            default -> throw new OrderStatusInvalidUpdateException();
        }
    }
}
