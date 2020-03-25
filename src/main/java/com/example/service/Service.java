package com.example.service;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class Service {

    private static final String PROJECT_ID = ServiceOptions.getDefaultProjectId();

    public void publish(String[] args) throws Exception {

    String topicId = args[0];
    ProjectTopicName topicName = ProjectTopicName.of(PROJECT_ID, topicId);
    Publisher publisher = null;
    List<ApiFuture<String>> futures = new ArrayList<>();

        try {

            publisher = Publisher.newBuilder(topicName).build();

            String message = args[1];

            // convert message to bytes
            ByteString data = ByteString.copyFromUtf8(message);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                    .setData(data)
                    .build();

            // Schedule a message to be published. Messages are automatically batched.
            ApiFuture<String> future = publisher.publish(pubsubMessage);
            futures.add(future);

        } finally {
            // Wait on any pending requests
            List<String> messageIds = ApiFutures.allAsList(futures).get();

            for (String messageId : messageIds) {
                System.out.println(messageId);
            }

            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                publisher.shutdown();
            }
        }
    }


}
