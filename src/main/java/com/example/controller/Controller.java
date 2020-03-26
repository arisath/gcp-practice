package com.example.controller;

import com.example.model.Message;
import com.example.service.PublisherService;
import com.example.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private PublisherService publisherService;

	@Autowired
	private SubscriberService subscriberService;

    @PostMapping("/publish")
    public String publish(@RequestBody Message message) throws Exception {
        publisherService.publish(message.getMessage());

        return "Message submitted to Pub/Sub";
    }

    @GetMapping("/receive")
    public String receive() throws Exception {
		subscriberService.receive();

		return "Messages received";
    }


}
