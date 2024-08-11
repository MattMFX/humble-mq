package br.edu.ufabc.mfmachado.humblemq.usecase.model;

import br.edu.ufabc.mfmachado.humblemq.proto.ChannelType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChannelModel {
    private String name;
    private ChannelType type;
    private Integer messageCount;
}
