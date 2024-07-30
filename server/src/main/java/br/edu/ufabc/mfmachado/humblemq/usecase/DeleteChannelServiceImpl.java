package br.edu.ufabc.mfmachado.humblemq.usecase;

import br.edu.ufabc.mfmachado.humblemq.entity.ChannelEntity;
import br.edu.ufabc.mfmachado.humblemq.gateway.repository.ChannelRepository;
import br.edu.ufabc.mfmachado.humblemq.proto.DeleteChannelGrpc;
import br.edu.ufabc.mfmachado.humblemq.proto.DeleteChannelRequest;
import br.edu.ufabc.mfmachado.humblemq.proto.DeleteChannelResponse;
import br.edu.ufabc.mfmachado.humblemq.proto.OperationStatus;
import io.grpc.stub.StreamObserver;

public class DeleteChannelServiceImpl extends DeleteChannelGrpc.DeleteChannelImplBase {

    private final ChannelRepository channelRepository;

    public DeleteChannelServiceImpl(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public void deleteChannel(DeleteChannelRequest request, StreamObserver<DeleteChannelResponse> responseObserver) {
        try {
            channelRepository.deleteByName(request.getName());
            DeleteChannelResponse response = DeleteChannelResponse.newBuilder()
                    .setName(request.getName())
                    .setDeletionStatus(OperationStatus.SUCCESS)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            DeleteChannelResponse response = DeleteChannelResponse.newBuilder()
                    .setName(request.getName())
                    .setDeletionStatus(OperationStatus.FAILURE)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
