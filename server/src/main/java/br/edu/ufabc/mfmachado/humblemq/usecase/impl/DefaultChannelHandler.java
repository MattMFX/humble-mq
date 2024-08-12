package br.edu.ufabc.mfmachado.humblemq.usecase.impl;

import br.edu.ufabc.mfmachado.humblemq.entity.Channel;
import br.edu.ufabc.mfmachado.humblemq.entity.Subscriber;
import br.edu.ufabc.mfmachado.humblemq.exceptions.channel.ChannelAlreadyExistsException;
import br.edu.ufabc.mfmachado.humblemq.exceptions.channel.ChannelDoesNotExist;
import br.edu.ufabc.mfmachado.humblemq.gateway.entity.ChannelEntity;
import br.edu.ufabc.mfmachado.humblemq.gateway.entity.MessageEntity;
import br.edu.ufabc.mfmachado.humblemq.gateway.repository.ChannelRepository;
import br.edu.ufabc.mfmachado.humblemq.proto.ChannelType;
import br.edu.ufabc.mfmachado.humblemq.proto.Message;
import br.edu.ufabc.mfmachado.humblemq.usecase.ChannelHandler;
import br.edu.ufabc.mfmachado.humblemq.usecase.model.ChannelModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class DefaultChannelHandler implements ChannelHandler {
    private final ChannelRepository channelRepository;

    @Override
    public void createChannel(String channelName, ChannelType type) {
         if (channelRepository.existsByName(channelName)) {
             throw new ChannelAlreadyExistsException();
         }

        Channel.registerChannel(channelName, type);
        ChannelEntity channelEntity = new ChannelEntity(channelName, type);
        channelRepository.save(channelEntity);
    }

    @Override
    public void deleteChannel(String channelName) {
        ChannelEntity channelEntity = channelRepository.findByName(channelName).orElseThrow(ChannelDoesNotExist::new);
        channelRepository.delete(channelEntity);
        Channel.unregisterChannel(channelName);
    }

    @Override
    public List<ChannelModel> listChannels() {
        List<ChannelEntity> channelEntities = channelRepository.findAll();
        return channelEntities.stream()
                .map(channelEntity -> ChannelModel.builder()
                        .name(channelEntity.getName())
                        .type(channelEntity.getType())
                        .messageCount(channelEntity.getMessages().size())
                        .build())
                .toList();
    }

    @Override
    public void subscribeToChannel(String channelName, Subscriber subscriber) {
        getChannel(channelName).subscribe(subscriber);
    }

    @Override
    public void sendMessages(String channelName, List<Message> messages) {
        Channel channel = getChannel(channelName);
        ChannelEntity channelEntity = channelRepository.findByName(channelName).orElseThrow(ChannelDoesNotExist::new);
        List<MessageEntity> messageEntities = messages.stream()
                .map(message -> new MessageEntity(UUID.randomUUID(), message.getContent()))
                .toList();

        channelEntity.getMessages().addAll(messageEntities);
        ChannelEntity finalChannelEntity = channelRepository.saveAndFlush(channelEntity);
        sendAndRemove(channel, finalChannelEntity, messageEntities);
    }

    /**
     * Esse método é chamado no boot da aplicação para recuperar os channels e as mensagens que estavam
     * ativos antes da aplicação ser desligada, garantindo a persistencia dos canais e suas mensagens.
     */
    @Override
    public void recoverChannels() {
        List<ChannelEntity> channelEntities = channelRepository.findAll();
        channelEntities.forEach(channelEntity -> {
            Channel channel = Channel.registerChannel(channelEntity.getName(), channelEntity.getType());
            sendAndRemove(channel, channelEntity, channelEntity.getMessages().stream().toList());
        });
    }

    private Channel getChannel(String channelName) {
        return Channel.getChannel(channelName).orElseThrow(ChannelDoesNotExist::new);
    }

    private void sendAndRemove(Channel channel, ChannelEntity channelEntity, List<MessageEntity> messageEntities) {
        CompletableFuture.runAsync(() ->
            messageEntities.forEach(messageEntity -> {
                channel.sendMessage(messageEntity.getContent());
                channelEntity.getMessages().removeIf(m -> m.getUuid().equals(messageEntity.getUuid()));
                channelRepository.saveAndFlush(channelEntity);
            }));
    }
}
