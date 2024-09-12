package antifraud.validator.service.validations.specific;

import antifraud.validator.endpoint.exception.validations.InvalidIpAddressException;
import antifraud.validator.service.validations.Validator;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class IpValidator implements Validator<String> {

    @Override
    public void validate(String ip) {
        if (!isValidIPv4(ip)) {
            throw new InvalidIpAddressException("Invalid IP address format: " + ip);
        }
    }

    private boolean isValidIPv4(String ip) {
        try {
            InetAddress inet = InetAddress.getByName(ip);
            return inet.getHostAddress().equals(ip) && ip.contains(".");
        } catch (UnknownHostException e) {
            return false;
        }
    }

}
