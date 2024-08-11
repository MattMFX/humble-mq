package br.edu.ufabc.mfmachado.humblemq.exceptions.channel;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ChannelDoesNotExist extends RuntimeException {
    public ChannelDoesNotExist(String message) {
        super(message);
    }
}
