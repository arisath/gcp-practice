package com.example.service;

import com.example.model.ReceivedMsg;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.pubsub.v1.stub.GrpcSubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStubSettings;
import com.google.pubsub.v1.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AsynchronousSubscriberService {

    private static final String PROJECT_ID = ServiceOptions.getDefaultProjectId();

    @Value("${subscription}")
    private String subscription;

    public void createSubscriber() throws Exception {

        ProjectSubscriptionName subscriptionName =
                ProjectSubscriptionName.of(PROJECT_ID, subscription);
        MessageReceiver receiver =
                new MessageReceiver() {
                    @Override
                    public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
                        // handle incoming message, then ack/nack the received message
                        System.out.println("Id : " + message.getMessageId());
                        System.out.println("Data : " + message.getData().toStringUtf8());
                        consumer.ack();
                    }
                };

        Subscriber subscriber = null;
        try {
            // Create a subscriber for "my-subscription-id" bound to the message receiver
            subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
            subscriber.startAsync().awaitRunning();
            // Allow the subscriber to run indefinitely unless an unrecoverable error occurs
            subscriber.awaitTerminated();
        } finally {
            // Stop receiving messages
            if (subscriber != null) {
                subscriber.stopAsync();
            }
        }
        // [END pubsub_subscriber_async_pull]
    }





    public List<ReceivedMsg> createSubscriberWithSyncPull(
            int numOfMessages) throws Exception {
        // [START pubsub_subscriber_sync_pull]
        SubscriberStubSettings subscriberStubSettings =
                SubscriberStubSettings.newBuilder()
                        .setTransportChannelProvider(
                                SubscriberStubSettings.defaultGrpcTransportProviderBuilder()
                                        .setMaxInboundMessageSize(20 << 20) // 20MB
                                        .build())
                        .build();

        try (SubscriberStub subscriber = GrpcSubscriberStub.create(subscriberStubSettings)) {
            // String projectId = "my-project-id";
            // String subscriptionId = "my-subscription-id";
            // int numOfMessages = 10;   // max number of messages to be pulled
            String subscriptionName = ProjectSubscriptionName.format(PROJECT_ID, subscription);
            PullRequest pullRequest =
                    PullRequest.newBuilder()
                            .setMaxMessages(numOfMessages)
                            .setReturnImmediately(false) // return immediately if messages are not available
                            .setSubscription(subscriptionName)
                            .build();

            // use pullCallable().futureCall to asynchronously perform this operation
            PullResponse pullResponse = subscriber.pullCallable().call(pullRequest);
            List<String> ackIds = new ArrayList<>();
            List<ReceivedMsg> listOfMessages = new ArrayList<>();

            for (ReceivedMessage message : pullResponse.getReceivedMessagesList()) {
                // handle received message
                // ...
                listOfMessages.add(new ReceivedMsg(message));
                System.out.println(message.getMessage());
                System.out.println(message.getMessage().getData());

                ackIds.add(message.getAckId());
            }
            // acknowledge received messages
/*            AcknowledgeRequest acknowledgeRequest =
                    AcknowledgeRequest.newBuilder()
                            .setSubscription(subscriptionName)
                            .addAllAckIds(ackIds)
                            .build();
            // use acknowledgeCallable().futureCall to asynchronously perform this operation
            subscriber.acknowledgeCallable().call(acknowledgeRequest);*/
            return listOfMessages;
        }
        // [END pubsub_subscriber_sync_pull]
    }



}
