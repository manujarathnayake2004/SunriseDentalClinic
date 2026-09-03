package lk.sunrise.clinic.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "treatments")
public class Treatment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Treatment name is required.")
    @Size(max = 120, message = "Treatment name must be 120 characters or fewer.")
    @Column(nullable = false, length = 120)
    private String treatmentName;

    @NotNull(message = "Treatment cost is required.")
    @DecimalMin(value = "0.00", message = "Treatment cost cannot be negative.")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal treatmentCost;

    @NotNull(message = "Consultation fee is required.")
    @DecimalMin(value = "0.00", message = "Consultation fee cannot be negative.")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal consultationFee;

    @Min(value = 5, message = "Duration must be at least 5 minutes.")
    @Max(value = 240, message = "Duration must not exceed 240 minutes.")
    @Column(nullable = false)
    private int durationMinutes = 30;

    @Column(nullable = false)
    private boolean active = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTreatmentName() { return treatmentName; }
    public void setTreatmentName(String treatmentName) { this.treatmentName = treatmentName; }
    public BigDecimal getTreatmentCost() { return treatmentCost; }
    public void setTreatmentCost(BigDecimal treatmentCost) { this.treatmentCost = treatmentCost; }
    public BigDecimal getConsultationFee() { return consultationFee; }
    public void setConsultationFee(BigDecimal consultationFee) { this.consultationFee = consultationFee; }
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
