package br.edu.ufabc.mfmachado.humblemq.usecase.impl;

import br.edu.ufabc.mfmachado.humblemq.exceptions.channel.ChannelDoesNotExist;
import br.edu.ufabc.mfmachado.humblemq.proto.SendMessageGrpc;
import br.edu.ufabc.mfmachado.humblemq.proto.SendMessageRequest;
import br.edu.ufabc.mfmachado.humblemq.proto.SendMessageResponse;
import br.edu.ufabc.mfmachado.humblemq.usecase.ChannelHandler;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendMessageServiceImpl extends SendMessageGrpc.SendMessageImplBase {
    private final ChannelHandler channelHandler;

    @Override
    public void sendMessage(SendMessageRequest request, StreamObserver<SendMessageResponse> responseObserver) {
        try {
            channelHandler.sendMessages(request.getChannel(), request.getMessageList());
            responseObserver.onNext(SendMessageResponse.newBuilder().build());
            responseObserver.onCompleted();
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
