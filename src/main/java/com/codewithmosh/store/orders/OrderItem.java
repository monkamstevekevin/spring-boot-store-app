package com.codewithmosh.store.orders;

import com.codewithmosh.store.produts.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "order_id")
    private Order order;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @NotNull
    @Column(name = "quantity")
    private Integer quantity;

    @NotNull
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    public OrderItem(Order order, Product product, Integer quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
        this.totalPrice = unitPrice.multiply(new BigDecimal(quantity));
    }
}