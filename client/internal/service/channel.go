package service

import (
	"context"
	"fmt"
	"github.com/mattmfx/humble-mq-go-client/internal/app"
	pb "github.com/mattmfx/humble-mq-go-client/internal/pb"
)

func CreateChannel(channelName string, channelType pb.ChannelType) (*pb.CreateChannelResponse, error) {
	client, err := app.NewCreateChannelClient()
	if err != nil {
		return nil, err
	}

	request := pb.CreateChannelRequest{Name: channelName, Type: channelType}
	response, err := client.CreateChannel(context.Background(), &request)
	if err != nil {
		return nil, err
	}

	return response, nil
}

func ListChannels() (*pb.ListChannelResponse, error) {
	client, err := app.NewListChannelClient()
	if err != nil {
		return nil, err
	}

	request := pb.ListChannelRequest{}
	response, err := client.ListChannel(context.Background(), &request)
	if err != nil {
		return nil, err
	}

	fmt.Println(len(response.GetChannels()))

	return response, nil
}

func DeleteChannel(channelName string) (*pb.DeleteChannelResponse, error) {
	client, err := app.NewDeleteChannelClient()
	if err != nil {
		return nil, err
	}

	request := pb.DeleteChannelRequest{Name: channelName}
	response, err := client.DeleteChannel(context.Background(), &request)
	if err != nil {
		return nil, err
	}

	return response, nil
}
