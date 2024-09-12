package antifraud.validator.service.ips;

import antifraud.validator.endpoint.exception.validations.InvalidIpAddressException;
import antifraud.validator.model.IpAddress;
import antifraud.validator.repository.IpAddressRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Service
public class SuspiciousIpServiceImpl implements SuspiciousIpService {
    
    private final IpAddressRepository repository;

    public SuspiciousIpServiceImpl(IpAddressRepository repository) {
        this.repository = repository;
    }

    @Override
    public IpAddress saveSuspiciousIp(String ip) {
        validateIpAddress(ip);
        if (repository.existsByIp(ip)) {
            throw new EntityExistsException("IP address already exists in the database: " + ip);
        }
        IpAddress ipAddress = new IpAddress();
        ipAddress.setIp(ip);
        return repository.save(ipAddress);
    }

    @Override
    public void deleteSuspiciousIp(String ip) {
        validateIpAddress(ip);
        repository.findByIp(ip).ifPresentOrElse(repository::delete, () -> {
            throw new EntityNotFoundException("Entity with ip " + ip + " not found.");
        });
    }

    @Override
    public List<IpAddress> listAllSuspiciousIp() {
        return repository.findAllByOrderByIdAsc();
    }

    @Override
    public boolean isInTheBlackList(String ip) {
        return repository.existsByIp(ip);
    }

    @Override
    public void validateIpAddress(String ip) {
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
