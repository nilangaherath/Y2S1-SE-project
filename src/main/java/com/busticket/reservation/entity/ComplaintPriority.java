package com.busticket.reservation.entity;

public enum ComplaintPriority {
    URGENT("Urgent", "badge bg-danger text-white"),
    HIGH("High", "badge bg-warning text-dark"),
    MEDIUM("Medium", "badge bg-info text-dark"),
    LOW("Low", "badge bg-secondary text-white");

    private final String displayName;
    private final String badgeClass;

    ComplaintPriority(String displayName, String badgeClass) {
        this.displayName = displayName;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() { return displayName; }
    public String getBadgeClass() { return badgeClass; }
}
