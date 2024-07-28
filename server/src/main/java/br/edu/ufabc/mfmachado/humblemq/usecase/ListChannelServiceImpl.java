package br.edu.ufabc.mfmachado.humblemq.usecase;

import br.edu.ufabc.mfmachado.humblemq.entity.ChannelEntity;
import br.edu.ufabc.mfmachado.humblemq.gateway.repository.ChannelRepository;
import br.edu.ufabc.mfmachado.humblemq.proto.Channel;
import br.edu.ufabc.mfmachado.humblemq.proto.ListChannelGrpc;
import br.edu.ufabc.mfmachado.humblemq.proto.ListChannelRequest;
import br.edu.ufabc.mfmachado.humblemq.proto.ListChannelResponse;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListChannelServiceImpl extends ListChannelGrpc.ListChannelImplBase {

    private final ChannelRepository channelRepository;

    ListChannelServiceImpl(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public void listChannel(ListChannelRequest request, StreamObserver<ListChannelResponse> responseObserver) {
        List<ChannelEntity> channelEntities = channelRepository.findAll();
        ListChannelResponse.Builder responseBuilder = ListChannelResponse.newBuilder();
        channelEntities.forEach(
                channelEntity ->
                        responseBuilder.addChannels(Channel.newBuilder().setName(channelEntity.getName()).build())
        );
        ListChannelResponse response = responseBuilder.build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}