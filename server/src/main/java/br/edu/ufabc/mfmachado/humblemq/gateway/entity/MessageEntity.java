package br.edu.ufabc.mfmachado.humblemq.gateway.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "message")
@NoArgsConstructor
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private UUID uuid;

    @Column
    private String content;

    public MessageEntity(UUID uuid, String content) {
        this.uuid = uuid;
        this.content = content;
    }
}
