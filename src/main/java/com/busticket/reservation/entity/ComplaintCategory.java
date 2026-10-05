package com.busticket.reservation.entity;

public enum ComplaintCategory {
    PAYMENT_ISSUE("Payment & Refund Issue", "Charges, double debit, refund delay"),
    BUS_DELAY("Bus Delay & Schedule", "Departure/arrival delay, unscheduled stops"),
    STAFF_BEHAVIOR("Staff & Crew Behavior", "Rude behavior, unprofessional conduct"),
    BUS_CONDITION("Bus Condition & Amenities", "AC breakdown, dirty seats, mechanical issues"),
    GENERAL_FEEDBACK("General Feedback & Inquiry", "Suggestions, route inquiries, portal feedback");

    private final String displayName;
    private final String description;

    ComplaintCategory(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
}
