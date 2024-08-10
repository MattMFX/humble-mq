package br.edu.ufabc.mfmachado.humblemq.usecase.impl;

import br.edu.ufabc.mfmachado.humblemq.entity.Subscriber;
import br.edu.ufabc.mfmachado.humblemq.proto.Message;
import br.edu.ufabc.mfmachado.humblemq.proto.SubscribeGrpc;
import br.edu.ufabc.mfmachado.humblemq.proto.SubscribeRequest;
import br.edu.ufabc.mfmachado.humblemq.usecase.ChannelHandler;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscribeServiceImpl extends SubscribeGrpc.SubscribeImplBase {
    private final ChannelHandler channelHandler;

    @Override
    public void subscribe(SubscribeRequest request, StreamObserver<Message> responseObserver) {
        try {
            channelHandler.subscribeToChannel(request.getChannel(), new Subscriber(Subscriber.SubscriptionType.STREAM, responseObserver));
        } catch (Exception e) {
            // TODO handle exceptions
        }
    }

    @Override
    public void fetchMessage(SubscribeRequest request, StreamObserver<Message> responseObserver) {
        try {
            channelHandler.subscribeToChannel(request.getChannel(), new Subscriber(Subscriber.SubscriptionType.FETCH, responseObserver));
        } catch (Exception e) {
            // TODO handle exceptions
        }
    }
}
