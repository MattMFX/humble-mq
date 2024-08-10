package br.edu.ufabc.mfmachado.humblemq.entity;

import br.edu.ufabc.mfmachado.humblemq.proto.Message;
import io.grpc.stub.StreamObserver;

public record Subscriber(
        SubscriptionType subscriptionType,
        StreamObserver<Message> connection
) {
    public enum SubscriptionType {
        FETCH, STREAM
    }
}