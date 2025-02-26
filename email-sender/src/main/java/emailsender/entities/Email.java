package emailsender.entities;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "emails")
@Getter
@Setter
@Data

public class Email {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long emailId;

    private String firstName;
    private String lastName;
    private String recipientEmail;
    private String subject;
    private String body;
}
