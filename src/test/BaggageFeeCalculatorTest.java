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
    /*
    // Caso 3: Beneficio VIP — 1 maleta dentro del límite → gratis
    */ 
    @Test
    @DisplayName("Debería cobrar $0.00 por una maleta de 15 kg de un pasajero VIP")
    void shouldCharge0_whenSingleBagWithinLimitAndVipPassenger() {
        // Arrange
        when(passengerService.isVip(PASSENGER_ID)).thenReturn(true);

        // Act
        double fee = calculator.calculateFee(15.0, 1, PASSENGER_ID);

        // Assert
        assertEquals(0.0, fee, 0.001,
                "La primera maleta de un VIP sin exceso de peso debe ser gratis ($0.00)");
    }
    /*
    // Caso 4: Caso límite VIP — 2 maletas, 1ra gratis, 2da con cargo
    */ 
    @Test
    @DisplayName("Debería cobrar $30.00 por dos maletas de 15 kg de un pasajero VIP (primera gratis)")
    void shouldCharge30_whenTwoBagsWithinLimitAndVipPassenger() {
        // Arrange
        when(passengerService.isVip(PASSENGER_ID)).thenReturn(true);

        // Act
        double fee = calculator.calculateFee(15.0, 2, PASSENGER_ID);

        // Assert
        assertEquals(30.0, fee, 0.001,
                "VIP: primera maleta gratis ($0) + segunda maleta normal ($30) = $30.00");
    }
    /*
    // Caso 5 a: Excepción — peso igual a cero
    */ 
    @Test
    @DisplayName("Debería lanzar IllegalArgumentException cuando el peso es 0")
    void shouldThrowException_whenWeightIsZero() {
        // Arrange / Act / Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateFee(0.0, 1, PASSENGER_ID),
                "Peso igual a cero debe lanzar IllegalArgumentException"
        );
        assertEquals("Parámetros de equipaje inválidos", exception.getMessage());
    }
    /*
    // Caso 5 b: Excepción — peso negativo
    */ 
    @Test
    @DisplayName("Debería lanzar IllegalArgumentException cuando el peso es negativo")
    void shouldThrowException_whenWeightIsNegative() {
        // Arrange / Act / Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateFee(-5.0, 1, PASSENGER_ID),
                "Peso negativo debe lanzar IllegalArgumentException"
        );
        assertEquals("Parámetros de equipaje inválidos", exception.getMessage());
    }


}