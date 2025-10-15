package org.example.luckyburger.domain.review.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.common.entity.BaseEntity;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.user.entity.User;

@Getter
@Entity
@Table(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(length = 255)
    private String content;

    private int rating;

    @Column(length = 255)
    private String comment;

    private Review(User user, Shop shop, Order order, String content, int rating, String comment) {
        this.user = user;
        this.shop = shop;
        this.order = order;
        this.content = content;
        this.rating = rating;
        this.comment = comment;
    }

    @Builder
    public static Review of(User user, Shop shop, Order order, String content, int rating, String comment) {
        return new Review(user, shop, order, content, rating, comment);
    }
}
