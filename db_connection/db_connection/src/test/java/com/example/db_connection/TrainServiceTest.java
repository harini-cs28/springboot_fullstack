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

import com.example.db_connection.entity.Train;
import com.example.db_connection.repository.TrainRepository;
import com.example.db_connection.services.TrainService;

@ExtendWith(MockitoExtension.class)
public class TrainServiceTest {

    @Mock
    private TrainRepository trainRepository;

    @InjectMocks
    private TrainService trainService;

    private Train train;

    @BeforeEach
    void setUp() {
        train = new Train();
        train.setId(1L);
        train.setName("John Doe");
        train.setSource("Coimbatore");
        train.setDestination("Chennai");
        train.setBasePrice(500);
        train.setDiscountPercentage(10);
    }

    @Test
    void getAllTrains_ShouldReturnListOfTrains() {
        List<Train> trains = Arrays.asList(train);
        when(trainRepository.findAll()).thenReturn(trains);

        List<Train> result = trainService.getAllTrains();

        assertEquals(1, result.size());
        assertEquals(train.getName(), result.get(0).getName());
        verify(trainRepository, times(1)).findAll();
    }

    @Test
    void getTrainById_WhenTrainExists_ShouldReturnTrain() {
        when(trainRepository.findById(1L)).thenReturn(Optional.of(train));

        Optional<Train> result = trainService.getTrainById(1L);

        assertTrue(result.isPresent());
        assertEquals(train.getSource(), result.get().getSource());
        verify(trainRepository, times(1)).findById(1L);
    }

    @Test
    void getTrainById_WhenTrainDoesNotExist_ShouldReturnEmpty() {
        when(trainRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Train> result = trainService.getTrainById(1L);

        assertFalse(result.isPresent());
        verify(trainRepository, times(1)).findById(1L);
    }

    @Test
    void createTrain_ShouldSaveAndReturnTrain() {
        when(trainRepository.save(any(Train.class))).thenReturn(train);

        Train result = trainService.createTrain(train);

        assertNotNull(result);
        assertEquals(train.getName(), result.getName());
        verify(trainRepository, times(1)).save(train);
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
        doNothing().when(trainRepository).deleteById(1L);

        trainService.deleteTrain(1L);

        verify(trainRepository, times(1)).deleteById(1L);
    }
}
