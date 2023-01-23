package com.altun.fiveten.controller;


import com.altun.fiveten.dto.*;
import com.altun.fiveten.enums.ERole;
import com.altun.fiveten.model.ConfirmationToken;
import com.altun.fiveten.model.PasswordRenewalToken;
import com.altun.fiveten.model.Role;
import com.altun.fiveten.model.User;
import com.altun.fiveten.security.JWTGenerator;
import com.altun.fiveten.service.*;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServlet;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;
    private OnlineService onlineService;
    private ConfirmationTokenService confirmationTokenService;
    private EmailService emailService;
    private PasswordRenewalTokenService renewalTokenService;
    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService,
                          RoleService roleService, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator, OnlineService onlineService, ConfirmationTokenService confirmationTokenService, EmailService emailService, PasswordRenewalTokenService renewalTokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.onlineService = onlineService;
        this.confirmationTokenService = confirmationTokenService;
        this.emailService = emailService;
        this.renewalTokenService = renewalTokenService;
    }

    @PostMapping("login")

    public ResponseEntity<Object> login(@Valid @RequestBody LoginDto loginDto) throws BadCredentialsException {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()));
        }
        catch (BadCredentialsException e){
            return ResponseEntity.status(401).body(e.getMessage());
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtGenerator.generateToken(authentication);

        onlineService.setOnlineUser(loginDto.getUsername(),new Point(loginDto.getLatitude(),loginDto.getLongitude()));

        return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
    }

    @PostMapping("register")

    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto) {
        if (userService.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode((registerDto.getPassword())));

        Role roles = roleService.findByName(ERole.USER).get();
        user.setRoles(Collections.singletonList(roles));

        userService.save(user);

        ConfirmationToken token = new ConfirmationToken(user);

        confirmationTokenService.save(token);

        emailService.createConfirmationMail(user,token);

        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
    }

    @PostMapping("confirm-account")

    public ResponseEntity<Object> confirmUserAccount(@RequestParam("token") String token){

        ConfirmationToken confirmationToken = confirmationTokenService.findByConfirmationToken(token);

        if(confirmationToken != null)
        {
            User user = userService.findByEmail(confirmationToken.getUser().getEmail());
            user.setEnabled(true);
            userService.save(user);

            return new ResponseEntity<>("The user is confirmed",HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("The link is wrong or expired",HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("forgot-my-password")

    public ResponseEntity<Object> forgotUserPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO){

        User user = userService.findByEmail(forgotPasswordDTO.getEmail());

        PasswordRenewalToken token = new PasswordRenewalToken(user);

        renewalTokenService.save(token);

        emailService.createForgotPasswordMail(forgotPasswordDTO.getEmail(),token);

        return new ResponseEntity<>("Password reset email is sent!", HttpStatus.OK);
    }
    @PostMapping("password-renewal")

    public ResponseEntity<Object> renewUserPassword(@RequestParam("token") String token, @RequestBody PasswordRenewalDTO renewalDTO){
        PasswordRenewalToken passwordRenewalToken = renewalTokenService.findByRenewalToken(token);

        if(token != null)
        {
            User user = userService.findByEmail(passwordRenewalToken.getUser().getEmail());
            if(renewalDTO.getNewPassword().equals(renewalDTO.getPasswordConfirmation())){
                user.setPassword(passwordEncoder.encode(renewalDTO.getNewPassword()));
            }
            else{
                return new ResponseEntity<>("Passwords are not matched!",HttpStatus.BAD_REQUEST);
            }
            userService.save(user);

            return new ResponseEntity<>("User's password is changed",HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("The link is wrong or expired",HttpStatus.BAD_REQUEST);
        }
    }

}
