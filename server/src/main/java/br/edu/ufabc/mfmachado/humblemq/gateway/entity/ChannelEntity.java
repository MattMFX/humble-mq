package br.edu.ufabc.mfmachado.humblemq.gateway.entity;

import br.edu.ufabc.mfmachado.humblemq.proto.ChannelType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity
@Table(name = "channel")
@NoArgsConstructor
public class ChannelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    @Column
    private ChannelType type;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "channel_id", referencedColumnName = "id")
    private Set<MessageEntity> messages;

    public ChannelEntity(String name, ChannelType type) {
        this.name = name;
        this.type = type;
    }
}
