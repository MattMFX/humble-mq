package app

import (
	pb "github.com/mattmfx/humble-mq-go-client/internal/pb"
	"google.golang.org/grpc"
)

type Client struct {
}

func NewGrpcClient() (*grpc.ClientConn, error) {
	conn, err := grpc.NewClient("localhost:6565", grpc.WithInsecure())
	if err != nil {
		return nil, err
	}

	return conn, nil
}

func NewCreateChannelClient() (pb.CreateChannelClient, error) {
	conn, err := NewGrpcClient()
	if err != nil {
		return nil, err
	}

	return pb.NewCreateChannelClient(conn), nil
}

func NewListChannelClient() (pb.ListChannelClient, error) {
	conn, err := NewGrpcClient()
	if err != nil {
		return nil, err
	}

	return pb.NewListChannelClient(conn), nil
}

func NewDeleteChannelClient() (pb.DeleteChannelClient, error) {
	conn, err := NewGrpcClient()
	if err != nil {
		return nil, err
	}

	return pb.NewDeleteChannelClient(conn), nil
}
