package com.busticket.reservation.controller;

import com.busticket.reservation.dto.ComplaintRequestDTO;
import com.busticket.reservation.entity.Complaint;
import com.busticket.reservation.entity.ComplaintCategory;
import com.busticket.reservation.entity.User;
import com.busticket.reservation.repository.BookingRepository;
import com.busticket.reservation.repository.UserRepository;
import com.busticket.reservation.service.ComplaintService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/complaints")
public class CustomerComplaintController {

    private static final Logger log = LoggerFactory.getLogger(CustomerComplaintController.class);

    private final ComplaintService complaintService;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    private static final Long DEFAULT_CUSTOMER_ID = 1L;

    public CustomerComplaintController(ComplaintService complaintService,
                                       UserRepository userRepository,
                                       BookingRepository bookingRepository) {
        this.complaintService = complaintService;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("complaintDTO")) {
            model.addAttribute("complaintDTO", new ComplaintRequestDTO());
        }
        populateCommonModelAttributes(model);
        return "complaints/create-complaint";
    }

    @PostMapping("/new")
    public String submitComplaint(
            @Valid @ModelAttribute("complaintDTO") ComplaintRequestDTO dto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        log.info("Received complaint submission request with subject: {}", dto.getSubject());

        if (bindingResult.hasErrors()) {
            populateCommonModelAttributes(model);
            return "complaints/create-complaint";
        }

        try {
            Complaint created = complaintService.createComplaint(dto, DEFAULT_CUSTOMER_ID);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Your complaint has been submitted successfully! Ticket Reference: " + created.getTicketId() +
                    " (Priority: " + created.getPriority().getDisplayName() + ", SLA Target: " + created.getSlaDeadline() + ")");
            return "redirect:/complaints/my";
        } catch (IllegalArgumentException ex) {
            bindingResult.rejectValue("bookingReference", "error.bookingReference", ex.getMessage());
            populateCommonModelAttributes(model);
            return "complaints/create-complaint";
        }
    }

    @GetMapping("/my")
    public String listMyComplaints(Model model) {
        List<Complaint> complaints = complaintService.getComplaintsByUser(DEFAULT_CUSTOMER_ID);
        model.addAttribute("complaints", complaints);
        return "complaints/my-complaints";
    }

    @GetMapping("/view/{ticketId}")
    public String viewComplaintDetails(@PathVariable String ticketId, Model model) {
        Complaint complaint = complaintService.getComplaintByTicketId(ticketId);
        model.addAttribute("complaint", complaint);
        return "complaints/view-complaint";
    }

    @GetMapping("/edit/{ticketId}")
    public String showEditForm(@PathVariable String ticketId, Model model, RedirectAttributes redirectAttributes) {
        Complaint complaint = complaintService.getComplaintByTicketId(ticketId);

        if (!complaint.isEditableByCustomer()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Ticket " + ticketId + " cannot be edited because it is currently " + complaint.getStatus().getDisplayName());
            return "redirect:/complaints/my";
        }

        ComplaintRequestDTO dto = ComplaintRequestDTO.builder()
                .id(complaint.getId())
                .category(complaint.getCategory())
                .bookingReference(complaint.getBooking() != null ? complaint.getBooking().getBookingReference() : null)
                .subject(complaint.getSubject())
                .description(complaint.getDescription())
                .build();

        model.addAttribute("complaintDTO", dto);
        model.addAttribute("ticketId", ticketId);
        populateCommonModelAttributes(model);
        return "complaints/create-complaint";
    }

    @PostMapping("/edit/{ticketId}")
    public String updateComplaint(
            @PathVariable String ticketId,
            @Valid @ModelAttribute("complaintDTO") ComplaintRequestDTO dto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("ticketId", ticketId);
            populateCommonModelAttributes(model);
            return "complaints/create-complaint";
        }

        try {
            complaintService.updateComplaint(ticketId, dto, DEFAULT_CUSTOMER_ID);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Ticket " + ticketId + " was updated successfully!");
            return "redirect:/complaints/my";
        } catch (Exception ex) {
            model.addAttribute("ticketId", ticketId);
            model.addAttribute("errorMessage", ex.getMessage());
            populateCommonModelAttributes(model);
            return "complaints/create-complaint";
        }
    }

    @PostMapping("/delete/{ticketId}")
    public String cancelComplaint(@PathVariable String ticketId, RedirectAttributes redirectAttributes) {
        try {
            complaintService.deleteComplaintByCustomer(ticketId, DEFAULT_CUSTOMER_ID);
            redirectAttributes.addFlashAttribute("successMessage", "Ticket " + ticketId + " was cancelled and removed.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/complaints/my";
    }

    private void populateCommonModelAttributes(Model model) {
        model.addAttribute("categories", ComplaintCategory.values());
        User customer = userRepository.findById(DEFAULT_CUSTOMER_ID).orElse(null);
        if (customer != null) {
            model.addAttribute("userBookings", bookingRepository.findByCustomerOrderByCreatedAtDesc(customer));
        }
    }
}
