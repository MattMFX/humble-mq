package br.edu.ufabc.mfmachado.humble_mq_client_java.services;

import br.edu.ufabc.mfmachado.humblemq.proto.Message;
import br.edu.ufabc.mfmachado.humblemq.proto.SubscribeGrpc;
import br.edu.ufabc.mfmachado.humblemq.proto.SubscribeRequest;
import io.grpc.Channel;

public class SubscribeClient {
    private final SubscribeGrpc.SubscribeBlockingStub subscribeBlockingStub;
    private final SubscribeGrpc.SubscribeStub subscribeStub;

    public SubscribeClient(Channel channel) {
        subscribeBlockingStub = SubscribeGrpc.newBlockingStub(channel);
        subscribeStub = SubscribeGrpc.newStub(channel);
    }

    public void fetchMessage(String channelName) {
        Message message = subscribeBlockingStub.fetchMessage(
                SubscribeRequest.newBuilder()
                        .setChannel(channelName)
                        .build());
        System.out.println("Received message: " + message.getContent() + " from channel " + channelName);
    }
}
