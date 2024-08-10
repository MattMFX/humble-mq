package br.edu.ufabc.mfmachado.humblemq.usecase.impl;

import br.edu.ufabc.mfmachado.humblemq.proto.CreateChannelGrpc;
import br.edu.ufabc.mfmachado.humblemq.proto.CreateChannelRequest;
import br.edu.ufabc.mfmachado.humblemq.proto.CreateChannelResponse;
import br.edu.ufabc.mfmachado.humblemq.usecase.ChannelHandler;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateChannelServiceImpl extends CreateChannelGrpc.CreateChannelImplBase {
    private final ChannelHandler channelHandler;

    @Override
    public void createChannel(CreateChannelRequest request, StreamObserver<CreateChannelResponse> responseObserver) {
        try {
            // TODO Validação de campos obrigatórios
            channelHandler.createChannel(request.getName(), request.getType());
            responseObserver.onNext(CreateChannelResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            // TODO Handle errors
        }
    }
}
