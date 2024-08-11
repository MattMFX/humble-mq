package br.edu.ufabc.mfmachado.humblemq.entity;

import br.edu.ufabc.mfmachado.humblemq.proto.ChannelType;
import br.edu.ufabc.mfmachado.humblemq.proto.Message;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Channel {
    private static final ConcurrentHashMap<String, Channel> registeredChannels = new ConcurrentHashMap<>();

    @Getter
    private final String name;
    @Getter
    private final ChannelType type;

    private final List<Subscriber> subscribers;

    private Channel(String name, ChannelType type) {
        this.name = name;
        this.type = type;
        this.subscribers = new CopyOnWriteArrayList<>();
    }

    public static Channel registerChannel(String channelName, ChannelType channelType) {
        Channel channel = new Channel(channelName, channelType);
        registeredChannels.put(channelName, channel);
        System.out.println("Channel " + channelName + " registered");
        return channel;
    }

    public static void unregisterChannel(String channelName) {
        registeredChannels.remove(channelName);
        System.out.println("Channel " + channelName + " unregistered");
    }

    public static Optional<Channel> getChannel(String channelName) {
        return Optional.ofNullable(registeredChannels.get(channelName));
    }

    public void subscribe(Subscriber subscriber) {
        System.out.println("Subscriber added to channel " + name);
        subscribers.add(subscriber);
    }

    public CompletableFuture<Void> sendMessage(String message) {
        return CompletableFuture.runAsync(() -> {
            List<Subscriber> receivers = getReceivers();
            receivers.forEach(receiver -> sendToSubscriber(receiver, message));
        });
    }

    private List<Subscriber> getReceivers() {

        subscribers.forEach(subscriber -> {
            if (subscriber.context().isCancelled()) {
                System.out.println("Subscriber cancelled");
                subscribers.remove(subscriber);
            }
        });

        while (subscribers.isEmpty()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Error while waiting for subscribers");
            }
        }

        if (ChannelType.SIMPLE.equals(type)) {
            int random = ThreadLocalRandom.current().nextInt(subscribers.size());
            return List.of(subscribers.get(random));
        } else {
            return subscribers;
        }
    }

    private void sendToSubscriber(Subscriber subscriber, String message) {
        try {
            if (Objects.nonNull(subscriber.context().getDeadline()) && subscriber.context().getDeadline().isExpired()) {
                System.out.println("Subscriber deadline expired");
                throw new StatusRuntimeException(Status.DEADLINE_EXCEEDED);
            }
            switch (subscriber.subscriptionType()) {
                case STREAM -> subscriber.connection().onNext(Message.newBuilder().setContent(message).build());
                case FETCH -> {
                    subscriber.connection().onNext(Message.newBuilder().setContent(message).build());
                    subscriber.connection().onCompleted();
                    System.out.println("Subscriber completed");
                    subscribers.remove(subscriber);
                }
            }
        } catch (StatusRuntimeException e) {
            System.out.println("The client is no longer available, removing subscriber");
            subscribers.remove(subscriber);
            subscriber.connection().onError(Status.CANCELLED.asRuntimeException());
            throw e;
        }
    }










//    public void addMessage(String message) {
//        messages.add(message);
//    }
//
//    public Integer getPendingMessages() {
//        return messages.size();
//    }
//
//    private void startProcessing() {
//        System.out.println("Processing messages for channel " + name);
//        while (isActive) {
//            messages.forEach(message -> {
//                List<Subscriber> receivers = getReceivers();
//                if (!receivers.isEmpty()) {
//                    receivers.forEach(receiver -> sendToSubscriber(receiver, message));
//                    messages.remove(message);
//                }
//            });
//        }
//    }
//
//    private void stopProcessing() {
//        isActive = Boolean.FALSE;
//    }
//
//


}
