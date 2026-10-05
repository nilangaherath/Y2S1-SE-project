package com.busticket.reservation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "complaints", indexes = {
    @Index(name = "idx_complaints_ticket_id", columnList = "ticket_id"),
    @Index(name = "idx_complaints_status", columnList = "status"),
    @Index(name = "idx_complaints_priority", columnList = "priority"),
    @Index(name = "idx_complaints_category", columnList = "category"),
    @Index(name = "idx_complaints_created_at", columnList = "created_at")
})
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_id", nullable = false, unique = true, length = 30)
    private String ticketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ComplaintCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ComplaintPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ComplaintStatus status;

    @Column(nullable = false, length = 150)
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "staff_remarks", columnDefinition = "TEXT")
    private String staffRemarks;

    @Column(name = "sla_deadline", nullable = false)
    private LocalDateTime slaDeadline;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by_id")
    private User resolvedBy;

    public Complaint() {}

    public Complaint(Long id, String ticketId, User user, Booking booking, ComplaintCategory category,
                     ComplaintPriority priority, ComplaintStatus status, String subject, String description,
                     String staffRemarks, LocalDateTime slaDeadline, LocalDateTime createdAt, LocalDateTime updatedAt,
                     LocalDateTime resolvedAt, User resolvedBy) {
        this.id = id;
        this.ticketId = ticketId;
        this.user = user;
        this.booking = booking;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.subject = subject;
        this.description = description;
        this.staffRemarks = staffRemarks;
        this.slaDeadline = slaDeadline;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.resolvedAt = resolvedAt;
        this.resolvedBy = resolvedBy;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.status == null) {
            this.status = ComplaintStatus.OPEN;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isSlaBreached() {
        if (status == ComplaintStatus.RESOLVED || status == ComplaintStatus.CLOSED) {
            return resolvedAt != null && resolvedAt.isAfter(slaDeadline);
        }
        return LocalDateTime.now().isAfter(slaDeadline);
    }

    public boolean isEditableByCustomer() {
        return this.status == ComplaintStatus.OPEN;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
    public ComplaintCategory getCategory() { return category; }
    public void setCategory(ComplaintCategory category) { this.category = category; }
    public ComplaintPriority getPriority() { return priority; }
    public void setPriority(ComplaintPriority priority) { this.priority = priority; }
    public ComplaintStatus getStatus() { return status; }
    public void setStatus(ComplaintStatus status) { this.status = status; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStaffRemarks() { return staffRemarks; }
    public void setStaffRemarks(String staffRemarks) { this.staffRemarks = staffRemarks; }
    public LocalDateTime getSlaDeadline() { return slaDeadline; }
    public void setSlaDeadline(LocalDateTime slaDeadline) { this.slaDeadline = slaDeadline; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
    public User getResolvedBy() { return resolvedBy; }
    public void setResolvedBy(User resolvedBy) { this.resolvedBy = resolvedBy; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String ticketId;
        private User user;
        private Booking booking;
        private ComplaintCategory category;
        private ComplaintPriority priority;
        private ComplaintStatus status;
        private String subject;
        private String description;
        private String staffRemarks;
        private LocalDateTime slaDeadline;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime resolvedAt;
        private User resolvedBy;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder ticketId(String ticketId) { this.ticketId = ticketId; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder booking(Booking booking) { this.booking = booking; return this; }
        public Builder category(ComplaintCategory category) { this.category = category; return this; }
        public Builder priority(ComplaintPriority priority) { this.priority = priority; return this; }
        public Builder status(ComplaintStatus status) { this.status = status; return this; }
        public Builder subject(String subject) { this.subject = subject; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder staffRemarks(String staffRemarks) { this.staffRemarks = staffRemarks; return this; }
        public Builder slaDeadline(LocalDateTime slaDeadline) { this.slaDeadline = slaDeadline; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public Builder resolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; return this; }
        public Builder resolvedBy(User resolvedBy) { this.resolvedBy = resolvedBy; return this; }

        public Complaint build() {
            return new Complaint(id, ticketId, user, booking, category, priority, status, subject, description, staffRemarks, slaDeadline, createdAt, updatedAt, resolvedAt, resolvedBy);
        }
    }
}
