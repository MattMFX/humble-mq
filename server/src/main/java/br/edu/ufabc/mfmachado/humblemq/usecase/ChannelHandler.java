package br.edu.ufabc.mfmachado.humblemq.usecase;

import br.edu.ufabc.mfmachado.humblemq.entity.Channel;
import br.edu.ufabc.mfmachado.humblemq.entity.Subscriber;
import br.edu.ufabc.mfmachado.humblemq.proto.ChannelType;
import br.edu.ufabc.mfmachado.humblemq.proto.Message;
import br.edu.ufabc.mfmachado.humblemq.usecase.model.ChannelModel;

import java.util.List;

public interface ChannelHandler {

    void createChannel(String channelName, ChannelType type);

    void deleteChannel(String channelName);

    List<ChannelModel> listChannels();

    void subscribeToChannel(String channelName, Subscriber subscriber);

    void sendMessages(String channelName, List<Message> messages);

    void recoverChannels();
}
