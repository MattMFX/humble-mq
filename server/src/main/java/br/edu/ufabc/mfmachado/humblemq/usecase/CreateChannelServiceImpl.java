package br.edu.ufabc.mfmachado.humblemq.usecase;

import br.edu.ufabc.mfmachado.humblemq.entity.ChannelEntity;
import br.edu.ufabc.mfmachado.humblemq.gateway.repository.ChannelRepository;
import br.edu.ufabc.mfmachado.humblemq.proto.CreateChannelGrpc;
import br.edu.ufabc.mfmachado.humblemq.proto.CreateChannelRequest;
import br.edu.ufabc.mfmachado.humblemq.proto.CreateChannelResponse;
import br.edu.ufabc.mfmachado.humblemq.proto.OperationStatus;
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
        ChannelEntity channelEntity = new ChannelEntity();
        channelEntity.setName(request.getName());
        channelRepository.save(channelEntity);
        CreateChannelResponse response = CreateChannelResponse.newBuilder()
                .setName(channelEntity.getName())
                .setCreationStatus(OperationStatus.SUCCESS)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
