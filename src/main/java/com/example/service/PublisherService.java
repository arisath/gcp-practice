package com.example.service;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class PublisherService {

    private static final String PROJECT_ID = ServiceOptions.getDefaultProjectId();

    @Value("${topic}")
    private String topic;

    public void publish(String message) throws Exception {

    String topicId = topic;
    ProjectTopicName topicName = ProjectTopicName.of(PROJECT_ID, topicId);
    Publisher publisher = null;
    List<ApiFuture<String>> futures = new ArrayList<>();

        try {

            publisher = Publisher.newBuilder(topicName).build();

            ByteString data = ByteString.copyFromUtf8(message);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                    .setData(data)
                    .build();

            ApiFuture<String> future = publisher.publish(pubsubMessage);
            futures.add(future);

        } finally {
            List<String> messageIds = ApiFutures.allAsList(futures).get();

            for (String messageId : messageIds) {
                System.out.println(messageId);
            }

            if (publisher != null) {
                publisher.shutdown();
            }
        }
    }


}
