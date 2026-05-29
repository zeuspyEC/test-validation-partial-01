package ec.edu.epn.skyroute.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para BaggageFeeCalculator.
 * Patrón AAA: Arrange → Act → Assert
 */
@ExtendWith(MockitoExtension.class)
class BaggageFeeCalculatorTest {

    @Mock
    private PassengerService passengerService;

    @InjectMocks
    private BaggageFeeCalculator calculator;

    private static final Long PASSENGER_ID = 1L;

    /*
    // Caso 1: Equipaje estándar — pasajero regular, sin exceso
    */ 
    @Test
    @DisplayName("Debería cobrar $30.00 por una maleta de 20 kg de un pasajero regular")
    void shouldCharge30_whenStandardBagAndRegularPassenger() {
        // Arrange
        when(passengerService.isVip(PASSENGER_ID)).thenReturn(false);

        // Act
        double fee = calculator.calculateFee(20.0, 1, PASSENGER_ID);

        // Assert
        assertEquals(30.0, fee, 0.001,
                "Una maleta estándar sin exceso de peso debe costar $30.00");
    }
    /*
    // Caso 2: Exceso de peso — pasajero regular, maleta > 23 kg
    */ 
    @Test
    @DisplayName("Debería cobrar $80.00 por una maleta de 25 kg de un pasajero regular")
    void shouldCharge80_whenOverweightBagAndRegularPassenger() {
        // Arrange
        when(passengerService.isVip(PASSENGER_ID)).thenReturn(false);

        // Act
        double fee = calculator.calculateFee(25.0, 1, PASSENGER_ID);

        // Assert
        assertEquals(80.0, fee, 0.001,
                "Tarifa base $30 + recargo $50 por exceso = $80.00");
    }
}