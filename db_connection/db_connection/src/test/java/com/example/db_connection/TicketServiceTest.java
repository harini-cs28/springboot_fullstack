package com.example.db_connection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.db_connection.entity.Ticket;
import com.example.db_connection.repository.TicketRepository;
import com.example.db_connection.services.TicketService;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    private Ticket ticket;

    @BeforeEach
    void setUp() {
        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setUser("John Doe");
        ticket.setTrain("Coimbatore Express");
        ticket.setFinalPrice(500);
    }

    @Test
    void getAllTicket_ShouldReturnListOfTickets() {
        List<Ticket> tickets = Arrays.asList(ticket);
        when(ticketRepository.findAll()).thenReturn(tickets);

        List<Ticket> result = ticketService.getAllTickets();

        assertEquals(1, result.size());
        assertEquals(ticket.getUser(), result.get(0).getUser());
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void getTicketById_WhenTicketExists_ShouldReturnTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Optional<Ticket> result = ticketService.getTicketById(1L);

        assertTrue(result.isPresent());
        assertEquals(ticket.getTrain(), result.get().getTrain());
        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    void getTicketById_WhenTicketDoesNotExist_ShouldReturnEmpty() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Ticket> result = ticketService.getTicketById(1L);

        assertFalse(result.isPresent());
        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    void createTicket_ShouldSaveAndReturnTicket() {
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertEquals(ticket.getUser(), result.getUser());
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void updateUser_WhenUserExists_ShouldUpdateAndReturnUser() {
        Train updatedTrain = new Train();
        updatedTrain.setName("Madras");
        

        when(trainRepository.findById(1L)).thenReturn(Optional.of(train));
        when(trainRepository.save(any(Train.class))).thenReturn(train);

        Train result = trainService.updateTrain(1L, updatedTrain);

        assertEquals(updatedTrain.getName(), result.getName());
        verify(trainRepository, times(1)).findById(1L);
        verify(trainRepository, times(1)).save(train);
    }

    @Test
    void updateTrain_WhenTrainDoesNotExist_ShouldThrowException() {
        when(trainRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> trainService.updateTrain(1L, train));
        verify(trainRepository, times(1)).findById(1L);
        verify(trainRepository, never()).save(any(Train.class));
    }

    @Test
    void deleteUser_ShouldDeleteUser() {
        doNothing().when(ticketRepository).deleteById(1L);

        ticketService.deleteTicket(1L);

        verify(ticketRepository, times(1)).deleteById(1L);
    }
}

