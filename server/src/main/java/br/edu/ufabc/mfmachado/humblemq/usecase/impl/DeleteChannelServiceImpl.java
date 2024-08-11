package br.edu.ufabc.mfmachado.humblemq.usecase.impl;

import br.edu.ufabc.mfmachado.humblemq.exceptions.channel.ChannelDoesNotExist;
import br.edu.ufabc.mfmachado.humblemq.proto.DeleteChannelGrpc;
import br.edu.ufabc.mfmachado.humblemq.proto.DeleteChannelRequest;
import br.edu.ufabc.mfmachado.humblemq.proto.DeleteChannelResponse;
import br.edu.ufabc.mfmachado.humblemq.usecase.ChannelHandler;
import io.grpc.Status;
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
            if (notValid(request)) {
                throw new IllegalArgumentException();
            }
            channelHandler.deleteChannel(request.getName());
            responseObserver.onNext(DeleteChannelResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("The channel name must be informed")
                            .asRuntimeException()
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
                            .withDescription("An unexpected error ocurred while deleting the channel")
                            .asRuntimeException()
            );
        }
    }

    private Boolean notValid(DeleteChannelRequest request) {
        return request.getName().isEmpty();
    }
}
