package ec.edu.epn.skyroute.service;

import org.springframework.stereotype.Service;

/**
 * Calcula las tarifas de equipaje para la aerolínea SkyRoute Airlines.
 * <p>
 * Reglas de negocio:
 * <ol>
 *   <li>Tarifa base: $30.0 por maleta.</li>
 *   <li>Exceso de peso: +$50.0 si una maleta pesa más de 23 kg.</li>
 *   <li>Beneficio VIP: primera maleta gratis si el pasajero es VIP
 *       y la maleta no excede 23 kg.</li>
 *   <li>Excepciones: weight ≤ 0, bagCount < 1, o passengerId nulo
 *       lanzan IllegalArgumentException.</li>
 * </ol>
 */
@Service
public class BaggageFeeCalculator {

    private final PassengerService passengerService;

    public BaggageFeeCalculator(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    /**
     * Calcula la tarifa total de equipaje.
     *
     * @param weight       peso de cada maleta (kg)
     * @param bagCount     cantidad de maletas
     * @param passengerId  identificador del pasajero
     * @return costo total en dólares
     * @throws IllegalArgumentException si los parámetros no cumplen las restricciones
     */
    public double calculateFee(double weight, int bagCount, Long passengerId) {
        if (passengerId == null || weight <= 0 || bagCount < 1) {
            throw new IllegalArgumentException("Parámetros de equipaje inválidos");
        }
        /*#	Caso	Entrada	Resultado esperado:
        1	Equipaje estándar	1 maleta, 20 kg, pasajero regular	$30.00
        2	Exceso de peso	1 maleta, 25 kg, pasajero regular	$80.00
        3	Beneficio VIP	1 maleta, 15 kg, pasajero VIP	$0.00 (requiere Mockito)
        4	Caso límite VIP	2 maletas, 15 kg c/u, pasajero VIP	$30.00 (1ra gratis, 2da cobro normal)
        5	Validación de excepción	weight = 0 o negativo	IllegalArgumentException */
        
        final double baseFeePerBag = 30.0;
        final double overweightFeePerBag = 50.0;
        final double overweightLimitKg = 23.0;

        boolean vipPassenger = passengerService.isVip(passengerId);
        double totalFee = 0.0;

        for (int bagIndex = 0; bagIndex < bagCount; bagIndex++) {
            double bagFee = baseFeePerBag;

            if (weight > overweightLimitKg) {
                bagFee += overweightFeePerBag;
            }

            if (vipPassenger && bagIndex == 0 && weight <= overweightLimitKg) {
                bagFee = 0.0;
            }

            totalFee += bagFee;
        }

        return totalFee;

    }
}