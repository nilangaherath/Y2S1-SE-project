package com.busticket.reservation.entity;

public enum ComplaintStatus {
    OPEN("Open", "badge bg-primary"),
    IN_PROGRESS("In Progress", "badge bg-warning text-dark"),
    RESOLVED("Resolved", "badge bg-success"),
    CLOSED("Closed", "badge bg-secondary");

    private final String displayName;
    private final String badgeClass;

    ComplaintStatus(String displayName, String badgeClass) {
        this.displayName = displayName;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() { return displayName; }
    public String getBadgeClass() { return badgeClass; }
}
