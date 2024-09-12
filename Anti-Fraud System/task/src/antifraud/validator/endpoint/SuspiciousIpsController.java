package antifraud.validator.endpoint;

import antifraud.validator.endpoint.req.NewSuspiciousIpRequest;
import antifraud.validator.model.IpAddress;
import antifraud.validator.service.ips.SuspiciousIpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/antifraud")
public class SuspiciousIpsController {

    private final SuspiciousIpService suspiciousIpService;

    public SuspiciousIpsController(SuspiciousIpService suspiciousIpService) {
        this.suspiciousIpService = suspiciousIpService;
    }

    @PostMapping("/suspicious-ip")
    public ResponseEntity<Object> addSuspiciousIp(@RequestBody NewSuspiciousIpRequest request) {
        var savedIp = suspiciousIpService.saveSuspiciousIp(request.ip());
        return ResponseEntity.ok().body(savedIp);
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    public ResponseEntity<Object> deleteSuspiciousIp(@PathVariable String ip) {
        suspiciousIpService.deleteSuspiciousIp(ip);
        return ResponseEntity.ok().body(Map.of("status", "IP " + ip + " successfully removed!"));
    }

    @GetMapping("/suspicious-ip")
    public ResponseEntity<List<IpAddress>> getSuspiciousIpList() {
        var ipList = suspiciousIpService.listAllSuspiciousIp();
        return ResponseEntity.ok().body(ipList);
    }

}
