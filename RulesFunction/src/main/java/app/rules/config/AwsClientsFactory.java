package app.rules.config;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;
import java.util.Optional;

@Factory
public class AwsClientsFactory {

    @Singleton
    DynamoDbClient dynamoDbClient() {
        Region region = Optional.ofNullable(System.getenv("AWS_REGION"))
                .map(Region::of)
                .orElse(Region.US_EAST_1);

        String endpoint = System.getenv("DDB_ENDPOINT");
        if (endpoint != null && !endpoint.isBlank()) {
            return DynamoDbClient.builder()
                    .region(region)
                    .endpointOverride(URI.create(endpoint))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create("dummy", "dummy")))
                    .build();
        }

        return DynamoDbClient.builder()
                .region(region)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Singleton
    DynamoDbEnhancedClient enhanced(DynamoDbClient ddb) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(ddb)
                .build();
    }
}