package com.codewithmosh.store.orders;

import com.codewithmosh.store.carts.Cart;
import com.codewithmosh.store.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;


    @Column(name = "created_at",insertable = false,updatable = false)
    private LocalDateTime createdAt;


    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order",
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Set<OrderItem> items = new LinkedHashSet<>();
    public static  Order fromCart(Cart cart, User customer) {
        var order  =  new Order();
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(customer);
        cart.getItems().forEach(
                item -> {
                    var orderItem  =  new OrderItem(order, item.getProduct(), item.getQuantity());

                    order.items.add(orderItem);
                }
        );
        return order;
    }
    public boolean isPlacedBy(User customer) {
        return this.customer.equals(customer);
    }

}