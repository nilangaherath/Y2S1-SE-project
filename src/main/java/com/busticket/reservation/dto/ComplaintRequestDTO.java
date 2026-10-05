package com.busticket.reservation.dto;

import com.busticket.reservation.entity.ComplaintCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ComplaintRequestDTO {

    private Long id;

    @NotNull(message = "Please select a valid complaint category.")
    private ComplaintCategory category;

    private String bookingReference;

    @NotBlank(message = "Subject is required.")
    @Size(min = 5, max = 150, message = "Subject must be between 5 and 150 characters.")
    private String subject;

    @NotBlank(message = "Description cannot be blank.")
    @Size(min = 15, max = 2000, message = "Please provide detailed description (minimum 15 characters).")
    private String description;

    public ComplaintRequestDTO() {}

    public ComplaintRequestDTO(Long id, ComplaintCategory category, String bookingReference, String subject, String description) {
        this.id = id;
        this.category = category;
        this.bookingReference = bookingReference;
        this.subject = subject;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ComplaintCategory getCategory() { return category; }
    public void setCategory(ComplaintCategory category) { this.category = category; }
    public String getBookingReference() { return bookingReference; }
    public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private ComplaintCategory category;
        private String bookingReference;
        private String subject;
        private String description;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder category(ComplaintCategory category) { this.category = category; return this; }
        public Builder bookingReference(String bookingReference) { this.bookingReference = bookingReference; return this; }
        public Builder subject(String subject) { this.subject = subject; return this; }
        public Builder description(String description) { this.description = description; return this; }

        public ComplaintRequestDTO build() {
            return new ComplaintRequestDTO(id, category, bookingReference, subject, description);
        }
    }
}
