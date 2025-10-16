package org.example.luckyburger.domain.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.common.entity.BaseEntity;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.review.dto.request.ReviewUpdateRequest;
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

    private double rating;

    @Column(length = 255)
    private String comment;


    private Review(User user, Shop shop, Order order, String content, double rating, String comment) {
        this.user = user;
        this.shop = shop;
        this.order = order;
        this.content = content;
        this.rating = rating;
        this.comment = comment;
    }

    @Builder
    public static Review of(User user, Shop shop, Order order, String content, double rating, String comment) {
        return new Review(user, shop, order, content, rating, comment);
    }

    public void update(ReviewUpdateRequest request) {
        this.content = request.content();
        this.rating = request.rating();
    }
}
