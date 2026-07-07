package com.abs.user.api;

import com.abs.user.api.dto.MilesRequest;
import com.abs.user.api.dto.SubmitPassportRequest;
import com.abs.user.api.dto.UpdateProfileRequest;
import com.abs.user.application.command.AuthenticateUserCommand;
import com.abs.user.application.command.DeleteAccountCommand;
import com.abs.user.application.command.EarnMilesCommand;
import com.abs.user.application.command.RedeemMilesCommand;
import com.abs.user.application.command.RegisterUserCommand;
import com.abs.user.application.command.SubmitPassportCommand;
import com.abs.user.application.command.UpdatePassengerProfileCommand;
import com.abs.user.application.dto.AuthResult;
import com.abs.user.application.dto.UserView;
import com.abs.user.application.exception.UserNotFoundException;
import com.abs.user.application.usecase.AuthenticateUserUseCase;
import com.abs.user.application.usecase.DeleteUserAccountUseCase;
import com.abs.user.application.usecase.GetUserUseCase;
import com.abs.user.application.usecase.ManageFrequentFlyerUseCase;
import com.abs.user.application.usecase.RegisterUserUseCase;
import com.abs.user.application.usecase.UpdatePassengerProfileUseCase;
import com.abs.user.application.usecase.VerifyPassportUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Driving (inbound) adapter exposing the user-service use cases over HTTP. Effective base path is
 * {@code /api/v1/users} (the {@code /api/v1} prefix is applied by {@code WebConfig}). The controller
 * is thin: it adapts HTTP to commands, delegates to a use case, and returns a {@link UserView}.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final GetUserUseCase getUserUseCase;
    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final UpdatePassengerProfileUseCase updatePassengerProfileUseCase;
    private final ManageFrequentFlyerUseCase manageFrequentFlyerUseCase;
    private final VerifyPassportUseCase verifyPassportUseCase;
    private final DeleteUserAccountUseCase deleteUserAccountUseCase;

    /**
     * Legacy integration contract consumed by booking-service: returns a flat map with
     * {@code id, email, fullName, phone}, falling back to a stub when the user is not found so
     * downstream demos work without a seeded DB.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable("id") Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<UserView> user = getUserUseCase.findById(id);
        if (user.isPresent()) {
            UserView view = user.get();
            response.put("id", view.id());
            response.put("email", view.email());
            response.put("fullName", view.fullName());
            response.put("phone", view.phone());
        } else {
            response.put("id", id);
            response.put("email", "user" + id + "@example.com");
            response.put("fullName", "User " + id);
            response.put("phone", "0123456789");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/profile")
    public UserView getProfile(@PathVariable("id") Long id) {
        return getUserUseCase.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResult register(@RequestBody RegisterUserCommand command) {
        return registerUserUseCase.handle(command);
    }

    @PostMapping("/login")
    public AuthResult login(@RequestBody AuthenticateUserCommand command) {
        return authenticateUserUseCase.handle(command);
    }

    @PutMapping("/{id}/profile")
    public UserView updateProfile(@PathVariable("id") Long id, @RequestBody UpdateProfileRequest request) {
        return updatePassengerProfileUseCase.handle(new UpdatePassengerProfileCommand(
                id, request.fullName(), request.phone(), request.dateOfBirth(),
                request.gender(), request.nationality()));
    }

    @PostMapping("/{id}/miles/earn")
    public UserView earnMiles(@PathVariable("id") Long id, @RequestBody MilesRequest request) {
        return manageFrequentFlyerUseCase.earnMiles(
                new EarnMilesCommand(id, request.miles(), request.reason()));
    }

    @PostMapping("/{id}/miles/redeem")
    public UserView redeemMiles(@PathVariable("id") Long id, @RequestBody MilesRequest request) {
        return manageFrequentFlyerUseCase.redeemMiles(
                new RedeemMilesCommand(id, request.miles(), request.reason()));
    }

    @PostMapping("/{id}/passport")
    public UserView submitPassport(@PathVariable("id") Long id, @RequestBody SubmitPassportRequest request) {
        return verifyPassportUseCase.submitPassport(new SubmitPassportCommand(
                id, request.passportNumber(), request.issuingCountry(), request.expiryDate()));
    }

    @PostMapping("/{id}/passport/verify")
    public UserView verifyPassport(@PathVariable("id") Long id) {
        return verifyPassportUseCase.approve(id);
    }

    @PostMapping("/{id}/passport/reject")
    public UserView rejectPassport(@PathVariable("id") Long id) {
        return verifyPassportUseCase.reject(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable("id") Long id,
                              @RequestParam(name = "reason", required = false) String reason) {
        deleteUserAccountUseCase.handle(new DeleteAccountCommand(id, reason));
    }
}
