package br.edu.ufabc.mfmachado.humblemq.exceptions.channel;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ChannelAlreadyExistsException extends RuntimeException {
    public ChannelAlreadyExistsException(String message) {
        super(message);
    }
}
