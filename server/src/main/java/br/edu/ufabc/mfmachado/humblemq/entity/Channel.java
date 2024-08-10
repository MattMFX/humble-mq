package br.edu.ufabc.mfmachado.humblemq.entity;

import br.edu.ufabc.mfmachado.humblemq.proto.ChannelType;
import br.edu.ufabc.mfmachado.humblemq.proto.Message;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Channel {
    private static final ConcurrentHashMap<String, Channel> registeredChannels = new ConcurrentHashMap<>();

    @Getter
    private final String name;
    @Getter
    private final ChannelType type;

    private final List<String> messages;
    private final List<Subscriber> subscribers;

    private Channel(String name, ChannelType type) {
        this.name = name;
        this.type = type;
        this.messages = new CopyOnWriteArrayList<>();
        this.subscribers = new CopyOnWriteArrayList<>();
    }

    public static void registerChannel(String channelName, ChannelType channelType) {
        Channel channel = new Channel(channelName, channelType);
        registeredChannels.put(channelName, channel);
        new Thread(channel::startProcessing).start();
        System.out.println("Channel " + channelName + " registered");
    }

    public static Optional<Channel> getChannel(String channelName) {
        return Optional.ofNullable(registeredChannels.get(channelName));
    }

    public void subscribe(Subscriber subscriber) {
        System.out.println("Subscriber added to channel " + name);
        subscribers.add(subscriber);
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public Integer getPendingMessages() {
        return messages.size();
    }

    private void startProcessing() {
        System.out.println("Processing messages for channel " + name);
        while (true) {
            messages.forEach(message -> {
                List<Subscriber> receivers = getReceivers();
                if (!receivers.isEmpty()) {
                    receivers.forEach(receiver -> sendToSubscriber(receiver, message));
                    messages.remove(message);
                }
            });
        }
    }

    private List<Subscriber> getReceivers() {
        if (ChannelType.SIMPLE.equals(type) && !subscribers.isEmpty()) {
            int random = ThreadLocalRandom.current().nextInt(subscribers.size());
            return List.of(subscribers.get(random));
        } else {
            return subscribers;
        }
    }

    private void sendToSubscriber(Subscriber subscriber, String message) {
        switch (subscriber.subscriptionType()) {
            case STREAM -> subscriber.connection().onNext(Message.newBuilder().setContent(message).build());
            case FETCH -> {
                subscriber.connection().onNext(Message.newBuilder().setContent(message).build());
                subscriber.connection().onCompleted();
                subscribers.remove(subscriber);
            }
        }
    }
}
