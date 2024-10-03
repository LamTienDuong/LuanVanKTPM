package vn.luanvan.ktpm.domain;

import jakarta.persistence.*;

@Entity
@Table(name="silders")
public class Slider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
