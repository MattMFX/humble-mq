package br.edu.ufabc.mfmachado.humblemq.usecase.impl;

import br.edu.ufabc.mfmachado.humblemq.entity.Subscriber;
import br.edu.ufabc.mfmachado.humblemq.exceptions.channel.ChannelDoesNotExist;
import br.edu.ufabc.mfmachado.humblemq.proto.Message;
import br.edu.ufabc.mfmachado.humblemq.proto.SubscribeGrpc;
import br.edu.ufabc.mfmachado.humblemq.proto.SubscribeRequest;
import br.edu.ufabc.mfmachado.humblemq.usecase.ChannelHandler;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscribeServiceImpl extends SubscribeGrpc.SubscribeImplBase {
    private final ChannelHandler channelHandler;

    @Override
    public void listenMessages(SubscribeRequest request, StreamObserver<Message> responseObserver) {
        try {
            Context context = Context.current();
            channelHandler.subscribeToChannel(
                    request.getChannel(), new Subscriber(Subscriber.SubscriptionType.STREAM, responseObserver, context))
            ;
        } catch (ChannelDoesNotExist e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("The channel informed does not exist")
                            .asRuntimeException()
            );
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("An unexpected error ocurred while sending the message")
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void fetchMessage(SubscribeRequest request, StreamObserver<Message> responseObserver) {
        try {
            Context context = Context.current();
            channelHandler.subscribeToChannel(
                    request.getChannel(), new Subscriber(Subscriber.SubscriptionType.FETCH, responseObserver, context)
            );

        } catch (ChannelDoesNotExist e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("The channel informed does not exist")
                            .asRuntimeException()
            );
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("An unexpected error ocurred while sending the message")
                            .asRuntimeException()
            );
        }
    }
}
