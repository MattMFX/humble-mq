package br.edu.ufabc.mfmachado.humblemq.entity;

import br.edu.ufabc.mfmachado.humblemq.proto.ChannelType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "channel")
public class ChannelEntity {
    @Id
    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    @Column
    private ChannelType type;
}
