package br.edu.ufabc.mfmachado.humblemq.usecase.impl;

import br.edu.ufabc.mfmachado.humblemq.proto.SendMessageGrpc;
import br.edu.ufabc.mfmachado.humblemq.proto.SendMessageRequest;
import br.edu.ufabc.mfmachado.humblemq.proto.SendMessageResponse;
import br.edu.ufabc.mfmachado.humblemq.usecase.ChannelHandler;
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
            request.getMessageList()
                    .forEach(message -> channelHandler.getChannel(request.getChannel()).addMessage(message.getContent()));
            responseObserver.onNext(SendMessageResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            // TODO handle exceptions
        }
    }
}
