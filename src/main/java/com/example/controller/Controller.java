package com.example.controller;

import com.example.model.Message;
import com.example.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@Autowired
	private Service service;

	@PostMapping("/publish")
	public String publish(@RequestBody Message message) throws Exception
	{
		String[] ar = {"first-topic",message.getMessage()};

		service.publish(ar);

		return "Message submitted to Pub/Sub";
	}




}
