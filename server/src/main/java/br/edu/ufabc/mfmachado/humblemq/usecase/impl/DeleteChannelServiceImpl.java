package br.edu.ufabc.mfmachado.humblemq.usecase.impl;

import br.edu.ufabc.mfmachado.humblemq.proto.DeleteChannelGrpc;
import br.edu.ufabc.mfmachado.humblemq.proto.DeleteChannelRequest;
import br.edu.ufabc.mfmachado.humblemq.proto.DeleteChannelResponse;
import br.edu.ufabc.mfmachado.humblemq.usecase.ChannelHandler;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteChannelServiceImpl extends DeleteChannelGrpc.DeleteChannelImplBase {
    private final ChannelHandler channelHandler;

    @Override
    public void deleteChannel(DeleteChannelRequest request, StreamObserver<DeleteChannelResponse> responseObserver) {
        try {
            channelHandler.deleteChannel(request.getName());
            responseObserver.onNext(DeleteChannelResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            // TODO Handle errors
        }
    }
}
