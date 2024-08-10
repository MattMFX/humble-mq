package br.edu.ufabc.mfmachado.humblemq.usecase.impl;

import br.edu.ufabc.mfmachado.humblemq.entity.Channel;
import br.edu.ufabc.mfmachado.humblemq.entity.Subscriber;
import br.edu.ufabc.mfmachado.humblemq.gateway.entity.ChannelEntity;
import br.edu.ufabc.mfmachado.humblemq.gateway.repository.ChannelRepository;
import br.edu.ufabc.mfmachado.humblemq.proto.ChannelType;
import br.edu.ufabc.mfmachado.humblemq.proto.Message;
import br.edu.ufabc.mfmachado.humblemq.usecase.ChannelHandler;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultChannelHandler implements ChannelHandler {
    private final ChannelRepository channelRepository;

    @Override
    public void createChannel(String name, ChannelType type) {
        Channel.registerChannel(name, type);
        ChannelEntity channelEntity = new ChannelEntity();
        channelEntity.setName(name);
        channelEntity.setType(type);
        channelRepository.save(channelEntity);
    }

    @Override
    public void deleteChannel(String name) {
        channelRepository.deleteByName(name);
    }

    @Override
    public List<Channel> listChannels() {
        List<ChannelEntity> channelEntities = channelRepository.findAll();
        return channelEntities.stream()
                .map(channelEntity -> Channel.getChannel(channelEntity.getName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public Channel getChannel(String name) {
        return Channel.getChannel(name).orElseThrow(() -> new IllegalArgumentException("Channel does not exist"));
    }

    @Override
    public void subscribeToChannel(String channel, Subscriber subscriber) {
        getChannel(channel).subscribe(subscriber);
    }
}
