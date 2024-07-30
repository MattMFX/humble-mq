package br.edu.ufabc.mfmachado.humblemq.usecase;

import br.edu.ufabc.mfmachado.humblemq.entity.ChannelEntity;
import br.edu.ufabc.mfmachado.humblemq.gateway.repository.ChannelRepository;
import br.edu.ufabc.mfmachado.humblemq.proto.*;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class CreateChannelServiceImpl extends CreateChannelGrpc.CreateChannelImplBase {

    private final ChannelRepository channelRepository;

    public CreateChannelServiceImpl(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public void createChannel(CreateChannelRequest request, StreamObserver<CreateChannelResponse> responseObserver) {
        try {
            ChannelEntity channelEntity = new ChannelEntity();
            channelEntity.setName(request.getName());
            channelEntity.setType(request.getType());
            channelRepository.save(channelEntity);
            CreateChannelResponse response = CreateChannelResponse.newBuilder()
                    .setName(channelEntity.getName())
                    .setCreationStatus(OperationStatus.SUCCESS)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            CreateChannelResponse response = CreateChannelResponse.newBuilder()
                    .setName(request.getName())
                    .setCreationStatus(OperationStatus.FAILURE)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
