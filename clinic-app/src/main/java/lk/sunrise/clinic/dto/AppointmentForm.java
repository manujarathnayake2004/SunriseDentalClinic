package lk.sunrise.clinic.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentForm {
    private Long id;

    @NotNull(message = "Patient is required.")
    private Long patientId;

    @NotNull(message = "Dentist is required.")
    private Long dentistId;

    @NotNull(message = "Treatment is required.")
    private Long treatmentId;

    @NotNull(message = "Appointment date is required.")
    @FutureOrPresent(message = "Appointment date cannot be in the past.")
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment time is required.")
    private LocalTime appointmentTime;

    @Size(max = 500, message = "Notes must be under 500 characters.")
    private String notes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public Long getDentistId() { return dentistId; }
    public void setDentistId(Long dentistId) { this.dentistId = dentistId; }
    public Long getTreatmentId() { return treatmentId; }
    public void setTreatmentId(Long treatmentId) { this.treatmentId = treatmentId; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
