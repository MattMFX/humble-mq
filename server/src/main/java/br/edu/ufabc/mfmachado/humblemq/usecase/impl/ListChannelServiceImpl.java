package br.edu.ufabc.mfmachado.humblemq.usecase.impl;

import br.edu.ufabc.mfmachado.humblemq.proto.ChannelMessage;
import br.edu.ufabc.mfmachado.humblemq.proto.ListChannelGrpc;
import br.edu.ufabc.mfmachado.humblemq.proto.ListChannelRequest;
import br.edu.ufabc.mfmachado.humblemq.proto.ListChannelResponse;
import br.edu.ufabc.mfmachado.humblemq.usecase.ChannelHandler;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListChannelServiceImpl extends ListChannelGrpc.ListChannelImplBase {
    private final ChannelHandler channelHandler;

    @Override
    public void listChannel(ListChannelRequest request, StreamObserver<ListChannelResponse> responseObserver) {
        try {
            List<ChannelMessage> channels = channelHandler.listChannels().stream()
                    .map(channel -> ChannelMessage.newBuilder()
                            .setName(channel.getName())
                            .setType(channel.getType())
                            .setMessageCount(channel.getPendingMessages())
                            .build())
                    .toList();
            responseObserver.onNext(ListChannelResponse.newBuilder().addAllChannels(channels).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            // TODO handle exceptions
        }
    }
}