package br.edu.ufabc.mfmachado.humblemq.usecase.impl;

import br.edu.ufabc.mfmachado.humblemq.exceptions.channel.ChannelAlreadyExistsException;
import br.edu.ufabc.mfmachado.humblemq.proto.ChannelType;
import br.edu.ufabc.mfmachado.humblemq.proto.CreateChannelGrpc;
import br.edu.ufabc.mfmachado.humblemq.proto.CreateChannelRequest;
import br.edu.ufabc.mfmachado.humblemq.proto.CreateChannelResponse;
import br.edu.ufabc.mfmachado.humblemq.usecase.ChannelHandler;
import io.grpc.Status;
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
            if (notValid(request)) {
                throw new IllegalArgumentException("Invalid request");
            }
            channelHandler.createChannel(request.getName(), request.getType());
            responseObserver.onNext(CreateChannelResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("The channel name and type must be informed")
                            .asRuntimeException()
            );
        } catch (ChannelAlreadyExistsException e) {
            responseObserver.onError(
                    Status.ALREADY_EXISTS
                            .withDescription("A channel with the same name already exists")
                            .asRuntimeException()
            );
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("An unexpected error ocurred while creating the channel")
                            .asRuntimeException()
            );
        }
    }

    private Boolean notValid(CreateChannelRequest request) {
        return request.getName().isEmpty() || request.getType().equals(ChannelType.UNSPECIFIED_TYPE);
    }
}
