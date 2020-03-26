package com.example.service;

import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SubscriberService {

    private static final String PROJECT_ID = ServiceOptions.getDefaultProjectId();

    @Value("${subscription}")
    private String subscription;

    static class MessageReceiverExample implements MessageReceiver {

        @Override
        public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
         System.out.println();
         ArrayList<String> arrayList = new ArrayList<>();
         arrayList.add(message.getData().toStringUtf8());
            System.out.println(
                    "Message Id: " + message.getMessageId() + " Data: " + message.getData().toStringUtf8());
            consumer.ack();
            System.out.println();

        }
    }

    /**
     * Receive messages over a subscription.
     */
    public void receive() throws Exception {

        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(
                PROJECT_ID, subscription);
        Subscriber subscriber = null;
        try {
            // create a subscriber bound to the asynchronous message receiver
          //  ArrayList<String> arrayList = new ArrayList<>();

            subscriber = Subscriber.newBuilder(subscriptionName, new MessageReceiverExample()).build();
            subscriber.startAsync().awaitRunning();

            // subscriber.awaitTerminated();
        } catch (IllegalStateException e) {
            System.out.println("Subscriber unexpectedly stopped: " + e);
        }
    }
}
