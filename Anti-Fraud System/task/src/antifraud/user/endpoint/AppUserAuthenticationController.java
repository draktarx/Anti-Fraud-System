package antifraud.user.endpoint;

import antifraud.user.endpoint.request.ChangeUserAccessRequest;
import antifraud.user.endpoint.request.ChangeUserRoleRequest;
import antifraud.user.endpoint.request.NewAppUserRequest;
import antifraud.user.endpoint.response.AppUserAccessInfo;
import antifraud.user.endpoint.response.AppUserSuccessfullyCreatedResponseDto;
import antifraud.user.endpoint.response.AppUserSuccessfullyDeletedResponseDto;
import antifraud.user.model.AppUser;
import antifraud.user.repository.projections.AppUserBasicInfo;
import antifraud.user.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AppUserAuthenticationController {

    private final AppUserService service;

    public AppUserAuthenticationController(AppUserService service) {
        this.service = service;
    }

    @PostMapping("user")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody NewAppUserRequest request) {
        AppUser registeredUser = service.register(request);
        var response = new AppUserSuccessfullyCreatedResponseDto(registeredUser);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("list")
    public ResponseEntity<List<AppUserBasicInfo>> getUser() {
        var response = service.list(AppUserBasicInfo.class);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("user/{username}")
    public ResponseEntity<Object> deleteUser(@PathVariable String username) {
        service.delete(username);
        var response = new AppUserSuccessfullyDeletedResponseDto(username);
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("role")
    public ResponseEntity<Object> changeUserRole(
            @Valid @RequestBody ChangeUserRoleRequest request) {
        AppUser updatedUser = service.changeRole(request);
        var response = new AppUserBasicInfo(updatedUser.getId(),
                                            updatedUser.getName(),
                                            updatedUser.getUsername(),
                                            updatedUser.getRole());
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("access")
    public ResponseEntity<Object> changeUserAccess(
            @Valid @RequestBody ChangeUserAccessRequest request) {
        AppUser appUser = service.changeAccess(request);
        var response = new AppUserAccessInfo(appUser.getUsername(), appUser.getLocked());
        return ResponseEntity.ok().body(response);
    }

}
