package vn.luanvan.ktpm.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private double percent;
    private Instant dateStart;
    private Instant dateEnd;
}
