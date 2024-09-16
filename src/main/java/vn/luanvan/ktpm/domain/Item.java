package vn.luanvan.ktpm.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double price;
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
