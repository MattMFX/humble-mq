package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
)

const (
	CommandInteractive = "#"
	CommandCreate      = "create"
	CommandDelete      = "delete"
)

var commands map[string]func()
var descriptions map[string]string

func init() {
	commands = map[string]func(){
		CommandInteractive: func() { fmt.Println("Command #") },
		CommandCreate:      func() { fmt.Println("Read create") },
		CommandDelete:      func() { fmt.Println("Read delete") },
	}

	descriptions = map[string]string{
		CommandCreate: "  create   Create a new channel on the messaging server\n            Usage: create <channel_name>",
		CommandDelete: "  delete   Delete a channel on the messaging server\n            Usage: delete <channel_name>",
	}
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)

	for {
		fmt.Println(">>> Press # to enter interactive mode <<<")
		scanner.Scan()

		err := scanner.Err()
		if err != nil {
			log.Fatal(err)
		}

		if scanner.Text() == CommandInteractive {
			readCommands()
		}
	}
}

func readCommands() {
	for {
		fmt.Println("\n*** Please, enter a command or press # to exit interactive mode ***")
		fmt.Println("*** The commands available are ***")

		for cmd, desc := range descriptions {
			fmt.Printf("  -> %s: %s\n", cmd, desc)
		}

		scanner := bufio.NewScanner(os.Stdin)
		scanner.Scan()
		err := scanner.Err()
		if err != nil {
			log.Fatal(err)
		}

		cmd := scanner.Text()

		if action, exists := commands[cmd]; exists {
			action()
		} else {
			fmt.Println("Command not found")
		}
	}

}
