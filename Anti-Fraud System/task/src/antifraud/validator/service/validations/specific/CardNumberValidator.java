package antifraud.validator.service.validations.specific;

import antifraud.validator.endpoint.exception.validations.InvalidCardNumberException;
import antifraud.validator.service.validations.Validator;
import org.springframework.stereotype.Component;

@Component
public class CardNumberValidator implements Validator<String> {

    @Override
    public void validate(String number) {
        int total = 0;
        boolean alternate = false;

        // Iterar desde el último dígito hacia el primero
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(number.charAt(i));

            // Si la bandera 'alternate' está activada, duplicar el número
            if (alternate) {
                n *= 2;
                // Si el número es mayor que 9, restar 9
                if (n > 9) {
                    n -= 9;
                }
            }

            // Sumar el número al total
            total += n;

            // Cambiar la bandera en cada iteración
            alternate = !alternate;
        }

        // Si el total es divisible por 10, el número es válido
        var isValid = (total % 10 == 0);
        if (!isValid) {
            throw new InvalidCardNumberException("Invalid card number format: " + number);
        }
    }

}
