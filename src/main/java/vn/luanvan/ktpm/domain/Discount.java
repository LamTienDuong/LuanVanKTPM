package vn.luanvan.ktpm.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Table(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "name khong duoc de trong")
    private String name;

    @NotNull(message = "percent khong duoc de trong")
    @Min(value = 1, message = "percent phai lon hon hoac bang 1")
    @Max(value = 100, message = "percent phai nho hon hoac bang 100")
    private Double percent;
    private Instant dateStart;
    private Instant dateEnd;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public Instant getDateStart() {
        return dateStart;
    }

    public void setDateStart(Instant dateStart) {
        this.dateStart = dateStart;
    }

    public Instant getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Instant dateEnd) {
        this.dateEnd = dateEnd;
    }
}
