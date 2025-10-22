package ru.iot.message;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Setter
@Getter
@Entity
@ToString
@Table(name = "iot_messages")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", nullable = false, columnDefinition = "uuid")
    private UUID uuid;

    @Column(name = "text", nullable = false, length = 256)
    private String text;

}
