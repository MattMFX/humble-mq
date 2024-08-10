package br.edu.ufabc.mfmachado.humblemq.usecase;

import br.edu.ufabc.mfmachado.humblemq.entity.Channel;
import br.edu.ufabc.mfmachado.humblemq.entity.Subscriber;
import br.edu.ufabc.mfmachado.humblemq.proto.ChannelType;
import br.edu.ufabc.mfmachado.humblemq.proto.Message;
import io.grpc.stub.StreamObserver;

import java.util.List;

public interface ChannelHandler {

    void createChannel(String name, ChannelType type);

    void deleteChannel(String name);

    List<Channel> listChannels();

    Channel getChannel(String name);

    void subscribeToChannel(String channel, Subscriber subscriber);
}
