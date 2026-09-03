package lk.sunrise.clinic.controller;

import lk.sunrise.clinic.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/reports")
    public String reports(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model) {

        DateRange range = normalizeRange(from, to);
        model.addAttribute("from", range.from());
        model.addAttribute("to", range.to());

        if (range.from().isAfter(range.to())) {
            model.addAttribute("error", "The From date cannot be after the To date.");
            model.addAttribute("appointments", List.of());
            model.addAttribute("bills", List.of());
            model.addAttribute("revenue", BigDecimal.ZERO);
            model.addAttribute("workload", List.of());
            model.addAttribute("dailySummary", List.of());
            model.addAttribute("metrics", new ReportService.ReportMetrics(0, 0, 0, BigDecimal.ZERO));
            return "reports";
        }

        model.addAttribute("appointments", reportService.appointments(range.from(), range.to()));
        model.addAttribute("bills", reportService.bills(range.from(), range.to()));
        model.addAttribute("revenue", reportService.revenue(range.from(), range.to()));
        model.addAttribute("workload", reportService.dentistWorkload(range.from(), range.to()));
        model.addAttribute("dailySummary", reportService.dailySummary(range.from(), range.to()));
        model.addAttribute("metrics", reportService.metrics(range.from(), range.to()));
        return "reports";
    }

    @GetMapping(value = "/reports/appointments.csv", produces = "text/csv")
    public ResponseEntity<byte[]> exportAppointments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        if (from.isAfter(to)) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("The From date cannot be after the To date.".getBytes(StandardCharsets.UTF_8));
        }

        StringBuilder csv = new StringBuilder();
        csv.append("Appointment Number,Patient,Dentist,Treatment,Date,Time,Status\n");
        for (var row : reportService.appointmentRowsForExport(from, to)) {
            csv.append(csv(row.appointmentNumber())).append(',')
                    .append(csv(row.patientName())).append(',')
                    .append(csv(row.dentistName())).append(',')
                    .append(csv(row.treatmentName())).append(',')
                    .append(row.appointmentDate()).append(',')
                    .append(row.appointmentTime()).append(',')
                    .append(csv(row.status())).append('\n');
        }

        String filename = "appointments-" + from + "-to-" + to + ".csv";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
        headers.setContentType(new MediaType("text", "csv", StandardCharsets.UTF_8));
        return ResponseEntity.ok().headers(headers).body(csv.toString().getBytes(StandardCharsets.UTF_8));
    }

    private DateRange normalizeRange(LocalDate from, LocalDate to) {
        LocalDate effectiveFrom = from == null ? LocalDate.now().withDayOfMonth(1) : from;
        LocalDate effectiveTo = to == null ? LocalDate.now() : to;
        return new DateRange(effectiveFrom, effectiveTo);
    }

    private String csv(String value) {
        if (value == null) return "";
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }

    private record DateRange(LocalDate from, LocalDate to) {}
}
