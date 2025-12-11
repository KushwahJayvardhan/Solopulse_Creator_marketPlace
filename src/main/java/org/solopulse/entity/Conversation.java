package org.solopulse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private  Integer id;

    private  Integer user1_id;

    private Integer user2_id;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
