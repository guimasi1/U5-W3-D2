package com.example.U5W3D2.device;

import com.example.U5W3D2.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "devices")
public class Device {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue
    private UUID id;
    private String type;
    private String status;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
