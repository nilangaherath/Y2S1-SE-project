package com.busticket.reservation.service;

import com.busticket.reservation.dto.ComplaintRequestDTO;
import com.busticket.reservation.dto.StaffResolutionDTO;
import com.busticket.reservation.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ComplaintService {

    Complaint createComplaint(ComplaintRequestDTO dto, Long userId);

    Complaint updateComplaint(String ticketId, ComplaintRequestDTO dto, Long userId);

    void deleteComplaintByCustomer(String ticketId, Long userId);

    void deleteComplaintByStaff(String ticketId);

    Complaint getComplaintByTicketId(String ticketId);

    List<Complaint> getComplaintsByUser(Long userId);

    Page<Complaint> searchComplaints(ComplaintStatus status, ComplaintPriority priority,
                                     ComplaintCategory category, String keyword, Pageable pageable);

    Complaint resolveComplaint(StaffResolutionDTO dto, Long staffId);

    long getCountByStatus(ComplaintStatus status);
}
