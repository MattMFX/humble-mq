package br.edu.ufabc.mfmachado.humble_mq_client_java.services;

import br.edu.ufabc.mfmachado.humblemq.proto.Message;
import br.edu.ufabc.mfmachado.humblemq.proto.SendMessageGrpc;
import br.edu.ufabc.mfmachado.humblemq.proto.SendMessageRequest;
import io.grpc.Channel;

public class SendMessageClient {
    private final SendMessageGrpc.SendMessageBlockingStub sendMessageBlockingStub;
    private final SendMessageGrpc.SendMessageStub sendMessageStub;

    public SendMessageClient(Channel channel) {
        sendMessageBlockingStub = SendMessageGrpc.newBlockingStub(channel);
        sendMessageStub = SendMessageGrpc.newStub(channel);
    }

    public void sendMessage(String channelName) {
        System.out.println("Sending message to channel " + channelName);
        sendMessageBlockingStub.sendMessage(
                SendMessageRequest.newBuilder()
                        .setChannel(channelName)
                        .addMessage(Message.newBuilder().setContent("Message sent at " + System.currentTimeMillis()).build())
                        .build());
    }
}
