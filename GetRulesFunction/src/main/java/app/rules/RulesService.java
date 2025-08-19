package app.rules;

import jakarta.inject.Singleton;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.*;

@Singleton
public class RulesService {

    private final DynamoDbTable<Rule> table;

    public RulesService(DynamoDbEnhancedClient enhanced) {
        String tableName = Optional.ofNullable(System.getenv("RULES_TABLE")).orElse("Rules");
        this.table = enhanced.table(tableName, TableSchema.fromBean(Rule.class));
    }

    public Map<String, Object> list(int limit, String lastKey) {
        var scanBuilder = ScanEnhancedRequest.builder().limit(Math.min(200, Math.max(1, limit)));

        // только enabled=true (убери блок, если нужно отдавать всё)
        scanBuilder.filterExpression(
                Expression.builder()
                        .expression("#enabled = :true")
                        .putExpressionName("#enabled", "enabled")
                        .putExpressionValue(":true", AttributeValue.builder().bool(true).build())
                        .build()
        );

        if (lastKey != null && !lastKey.isBlank()) {
            scanBuilder.exclusiveStartKey(Map.of("ruleId", AttributeValue.builder().s(lastKey).build()));
        }

        var pageIt = table.scan(scanBuilder.build()).iterator();

        List<Rule> items = new ArrayList<>();
        String nextLastKey = null;

        if (pageIt.hasNext()) {
            var page = pageIt.next();
            items.addAll(page.items());
            var lek = page.lastEvaluatedKey();
            if (lek != null && lek.containsKey("ruleId") && lek.get("ruleId").s() != null) {
                nextLastKey = lek.get("ruleId").s();
            }
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("items", items);
        if (nextLastKey != null) payload.put("lastKey", nextLastKey);
        return payload;
    }

    public Rule get(String id) {
        return table.getItem(Key.builder().partitionValue(id).build());
    }
}