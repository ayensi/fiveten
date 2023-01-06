package com.altun.fiveten.model;

import com.altun.fiveten.enums.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email")
    @NotNull(message = "Please enter an email address")
    @Email(message = "Please enter an valid email address")
    private String email;
    @Column(name = "username")
    @NotNull(message = "Please enter an username")
    private String username;
    @JsonIgnore
    @Column(name = "password")
    @NotNull(message = "Please enter an password")
    private String password;
    @Column(name = "name")
    private String name;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "phoneNumber")
    private String phoneNumber;
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    @Column(name = "hairColor")
    private HairColor hairColor;
    @Enumerated(EnumType.STRING)
    @Column(name = "eyeColor")
    private EyeColor eyeColor;
    @Column(name = "height")
    private double height;
    @Column(name = "weight")
    private double weight;
    @Column(name = "profilePic")
    private String profilePic;
    @Enumerated(EnumType.STRING)
    @Column(name = "attractedGender")
    private Gender attractedGender;
    @Enumerated(EnumType.STRING)
    @Column(name = "relationshipStatus")
    private RelationshipStatus relationshipStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "lookingFor")
    private RelationshipType lookingFor;
    @Enumerated(EnumType.STRING)
    @Column(name = "occupation")
    private Occupation occupation;
    @Column(name = "lastLogin")
    private Date lastLogin;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="users_friends")
    @JsonIgnore
    List<User> friends = new ArrayList<>();

    @Column(name = "isEnabled")
    private boolean isEnabled;

    public User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
        this.isEnabled = false;
    }
}
