package antifraud.validator.service.ips;

import antifraud.validator.model.IpAddress;

import java.util.List;

public interface SuspiciousIpService {

    IpAddress saveSuspiciousIp(String ip);

    void deleteSuspiciousIp(String ip);

    List<IpAddress> listAllSuspiciousIp();

    boolean isInTheBlackList(String ip);

    void validateIpAddress(String ip);
}
