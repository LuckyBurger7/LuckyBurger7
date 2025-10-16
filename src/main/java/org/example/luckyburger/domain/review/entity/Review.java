package org.example.luckyburger.domain.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.common.entity.BaseEntity;

@Getter
@Entity
@Table(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    // 유저, 점포, 주문 정보를 받아올 수 있을때 활성화
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "shop_id", nullable = false)
//    private Shop shop;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id", nullable = false)
//    private Order order;

    @Column(length = 255)
    private String content;

    private double rating;

    @Column(length = 255)
    private String comment;

    // 유저, 점포, 주문 정보를 받아올 수 있을때 활성화
//    private Review(User user, Shop shop, Order order, String content, double rating, String comment) {
//        this.user = user;
//        this.shop = shop;
//        this.order = order;
//        this.content = content;
//        this.rating = rating;
//        this.comment = comment;
//    }
//
//    @Builder
//    public static Review of(User user, Shop shop, Order order, String content, double rating, String comment) {
//        return new Review(user, shop, order, content, rating, comment);
//    }

    // 임시로 리뷰만 작성 가능
    private Review(String content, double rating, String comment) {
        this.content = content;
        this.rating = rating;
        this.comment = comment;
    }

    @Builder
    public static Review of(String content, double rating, String comment) {
        return new Review(content, rating, comment);
    }
}
