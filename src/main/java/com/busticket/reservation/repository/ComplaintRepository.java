package com.busticket.reservation.repository;

import com.busticket.reservation.entity.Complaint;
import com.busticket.reservation.entity.ComplaintCategory;
import com.busticket.reservation.entity.ComplaintPriority;
import com.busticket.reservation.entity.ComplaintStatus;
import com.busticket.reservation.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    Optional<Complaint> findByTicketId(String ticketId);

    List<Complaint> findByUserOrderByCreatedAtDesc(User user);

    /**
     * Advanced multi-criteria search and filter JPQL query.
     * Supports optional status, priority, category, and free-text keyword search.
     */
    @Query("SELECT c FROM Complaint c " +
           "LEFT JOIN c.user u " +
           "WHERE (:status IS NULL OR c.status = :status) " +
           "AND (:priority IS NULL OR c.priority = :priority) " +
           "AND (:category IS NULL OR c.category = :category) " +
           "AND (:keyword IS NULL OR :keyword = '' OR " +
           "     LOWER(c.ticketId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "     LOWER(c.subject) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "     LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "     LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "     LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY " +
           "CASE c.priority " +
           "  WHEN com.busticket.reservation.entity.ComplaintPriority.URGENT THEN 1 " +
           "  WHEN com.busticket.reservation.entity.ComplaintPriority.HIGH THEN 2 " +
           "  WHEN com.busticket.reservation.entity.ComplaintPriority.MEDIUM THEN 3 " +
           "  WHEN com.busticket.reservation.entity.ComplaintPriority.LOW THEN 4 " +
           "  ELSE 5 END ASC, c.createdAt DESC")
    Page<Complaint> searchAndFilterComplaints(
            @Param("status") ComplaintStatus status,
            @Param("priority") ComplaintPriority priority,
            @Param("category") ComplaintCategory category,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    long countByStatus(ComplaintStatus status);
}
