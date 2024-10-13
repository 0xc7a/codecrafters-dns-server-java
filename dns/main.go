package main

import (
	"fmt"
	"github.com/miekg/dns"
	"log"
)

const address = "127.0.0.1:2053"

func constructRequest(domain ...string) *dns.Msg {
	var questions []dns.Question
	for _, domain := range domain {
		question := dns.Question{
			Name:   dns.Fqdn(domain),
			Qtype:  dns.TypeA,
			Qclass: dns.ClassINET,
		}
		questions = append(questions, question)
	}

	return &dns.Msg{
		MsgHdr: dns.MsgHdr{
			Id: 2173,
		},
		Compress: true,
		Question: questions,
	}
}

func sendRequest(client *dns.Client, request *dns.Msg) (*dns.Msg, error) {
	reply, _, err := client.Exchange(request, address)
	if err != nil {
		return nil, err
	}

	if t, ok := reply.Answer[0].(*dns.TXT); ok {
		fmt.Println(t.String())
	}

	if len(reply.Answer) != len(request.Question) {
		return nil, fmt.Errorf("#answers != #questions. Received %d, expected %d", len(reply.Answer), len(request.Question))
	}

	return reply, nil
}

func main() {
	request := constructRequest("abc.verylongdomainname.com", "def.verylongdomainname.com")

	client := new(dns.Client)

	reply, err := sendRequest(client, request)
	if err != nil {
		log.Fatalf("Error sending dns request: %s", err)
	}

	fmt.Println(reply.Id)
	fmt.Println(reply.MsgHdr.String())
	for _, question := range reply.Question {
		fmt.Println(question.String())
	}
	for _, answer := range reply.Answer {
		fmt.Println(answer.String())
	}
}
