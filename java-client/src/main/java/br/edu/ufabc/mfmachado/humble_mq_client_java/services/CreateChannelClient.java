package br.edu.ufabc.mfmachado.humble_mq_client_java.services;

import br.edu.ufabc.mfmachado.humblemq.proto.ChannelType;
import br.edu.ufabc.mfmachado.humblemq.proto.CreateChannelGrpc;
import br.edu.ufabc.mfmachado.humblemq.proto.CreateChannelRequest;
import io.grpc.Channel;

public class CreateChannelClient {
    private final CreateChannelGrpc.CreateChannelBlockingStub createChannelBlockingStub;
    private final CreateChannelGrpc.CreateChannelStub createChannelStub;

    public CreateChannelClient(Channel channel) {
        createChannelBlockingStub = CreateChannelGrpc.newBlockingStub(channel);
        createChannelStub = CreateChannelGrpc.newStub(channel);
    }

    public void createChannel(String channelName, ChannelType channelType) {
        createChannelBlockingStub.createChannel(CreateChannelRequest.newBuilder().setName(channelName).setType(channelType).build());
        System.out.println("Channel " + channelName + " created with type " + channelType);
    }
}
