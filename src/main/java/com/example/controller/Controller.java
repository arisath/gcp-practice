package com.example.controller;

import com.example.model.Message;
import com.example.service.AsynchronousSubscriberService;
import com.example.service.PublisherService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class Controller
{

    @Autowired
    private PublisherService publisherService;

    @Autowired
    private AsynchronousSubscriberService asynchronousSubscriberService;

    @PostMapping("/publish")
    public String publish(@RequestBody Message message) throws Exception
    {
        publisherService.publish(message.getMessage());

        return "Message submitted to Pub/Sub";
    }

    @GetMapping("/receive")
    public String receive(@RequestParam(required = false) Integer numberOfMessages) throws Exception
    {

        if (numberOfMessages == null)
        {
            return new Gson().toJson(asynchronousSubscriberService.createSubscriberWithSyncPull(5));
        }

        if (numberOfMessages < 0)
        {
            return "";
        }
        else
        {
            return new Gson().toJson(asynchronousSubscriberService.createSubscriberWithSyncPull(numberOfMessages));
        }

    }


}
