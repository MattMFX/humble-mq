package br.edu.ufabc.mfmachado.humble_mq_client_java.services;

import br.edu.ufabc.mfmachado.humblemq.proto.ListChannelGrpc;
import br.edu.ufabc.mfmachado.humblemq.proto.ListChannelRequest;
import br.edu.ufabc.mfmachado.humblemq.proto.ListChannelResponse;
import io.grpc.Channel;

public class ListChannelClient {
    private final ListChannelGrpc.ListChannelBlockingStub listChannelBlockingStub;
    private final ListChannelGrpc.ListChannelStub listChannelStub;

    public ListChannelClient(Channel channel) {
        listChannelBlockingStub = ListChannelGrpc.newBlockingStub(channel);
        listChannelStub = ListChannelGrpc.newStub(channel);
    }

    public void listChannel() {
        ListChannelResponse response = listChannelBlockingStub.listChannel(ListChannelRequest.newBuilder().build());
        response.getChannelsList().forEach(
                channel -> System.out.println(
                        "Found channel " + channel.getName() + " of type " + channel.getType() + " with " + channel.getMessageCount() + " messages"
                )
        );
    }
}
