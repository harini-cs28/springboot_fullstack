package com.example.db_connection.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.db_connection.entity.Ticket;
import com.example.db_connection.entity.Train;
import com.example.db_connection.entity.User;
import com.example.db_connection.repository.TicketRepository;
import com.example.db_connection.repository.UserRepository;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TrainService trainService;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public Ticket createTicket(Long userId, Long trainId) {
        User user = userService.getUserById(userId).orElseThrow();
        Train train = trainService.getTrainById(trainId).orElseThrow();

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setTrain(train);
        ticket.setBookingDate(LocalDateTime.now());
        ticket.setFinalPrice(calculateTicketPrice(train.getBasePrice(), train.getDiscountPercentage()));

        return ticketRepository.save(ticket);
    }

    public Ticket updateTicket(Long id, Ticket ticketDetails) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        if (ticketDetails.getUser() != null) {
            ticket.setUser(ticketDetails.getUser());
        }
        if (ticketDetails.getTrain() != null) {
            ticket.setTrain(ticketDetails.getTrain());
            ticket.setFinalPrice(calculateTicketPrice(ticketDetails.getTrain().getBasePrice(), ticketDetails.getTrain().getDiscountPercentage()));
        }
        ticket.setBookingDate(LocalDateTime.now()); // Update booking date
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    public double calculateTicketPrice(double basePrice, double discountPercentage) {
        return basePrice - (basePrice * discountPercentage / 100);
    }
}
