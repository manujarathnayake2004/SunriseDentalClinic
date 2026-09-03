package lk.sunrise.clinic.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "dentists")
public class Dentist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Dentist name is required.")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String dentistName;

    @NotBlank(message = "Specialization is required.")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String specialization;

    @Column(nullable = false)
    private boolean active = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDentistName() { return dentistName; }
    public void setDentistName(String dentistName) { this.dentistName = dentistName; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
