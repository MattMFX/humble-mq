package br.edu.ufabc.mfmachado.humble_mq_client_java;

import br.edu.ufabc.mfmachado.humble_mq_client_java.services.*;
import br.edu.ufabc.mfmachado.humblemq.proto.ChannelType;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class HumbleMqClientJavaApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(HumbleMqClientJavaApplication.class, args);
		ManagedChannel channel = Grpc.newChannelBuilder("localhost:6565", InsecureChannelCredentials.create()).build();
		CreateChannelClient createChannelClient = new CreateChannelClient(channel);
		DeleteChannelClient deleteChannelClient = new DeleteChannelClient(channel);
		SendMessageClient sendMessageClient = new SendMessageClient(channel);
		SubscribeClient subscribeClient = new SubscribeClient(channel);
		ListChannelClient listChannelClient = new ListChannelClient(channel);


		Random random = new Random();
		createChannelClient.createChannel("my-channel", random.nextBoolean() ? ChannelType.SIMPLE : ChannelType.MULTIPLE);

		for (int j = 0; j < random.nextInt(11); j++) {
			sendMessageClient.sendMessage("my-channel");
		}
		listChannelClient.listChannel();
		subscribeClient.fetchMessage("my-channel");

		Thread.sleep(10000);
		deleteChannelClient.deleteChannel("mychannel");
	}
}
