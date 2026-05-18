package lt.techin.shiftpilot.feature.auth.model;

import jakarta.persistence.*;
import lombok.*;
import lt.techin.shiftpilot.feature.user.model.User;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 500)
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Instant expiryDate;
}
