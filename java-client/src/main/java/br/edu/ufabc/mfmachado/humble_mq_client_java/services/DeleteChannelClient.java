package br.edu.ufabc.mfmachado.humble_mq_client_java.services;

import br.edu.ufabc.mfmachado.humblemq.proto.DeleteChannelGrpc;
import br.edu.ufabc.mfmachado.humblemq.proto.DeleteChannelRequest;
import io.grpc.Channel;

public class DeleteChannelClient {
    private final DeleteChannelGrpc.DeleteChannelBlockingStub deleteChannelBlockingStub;
    private final DeleteChannelGrpc.DeleteChannelStub deleteChannelStub;

    public DeleteChannelClient(Channel channel) {
        deleteChannelBlockingStub = DeleteChannelGrpc.newBlockingStub(channel);
        deleteChannelStub = DeleteChannelGrpc.newStub(channel);
    }

    public void deleteChannel(String channelName) {
        deleteChannelBlockingStub.deleteChannel(DeleteChannelRequest.newBuilder().setName(channelName).build());
        System.out.println("Channel " + channelName + " deleted");
    }
}
