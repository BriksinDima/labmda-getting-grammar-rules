package app.rules.service;

import app.rules.model.Rule;
import jakarta.inject.Singleton;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;


@Singleton
public class RulesService {

    private final DynamoDbTable<Rule> table;

    public RulesService(DynamoDbEnhancedClient enhanced) {
        String tableName = Optional.ofNullable(System.getenv("RULES_TABLE")).orElse("RulePlayRules");
        this.table = enhanced.table(tableName, TableSchema.fromBean(Rule.class));
    }

    public Map<String, Object> list(int limit, String lastKey) {
        int pageSize = Math.min(200, Math.max(1, limit));

        var filter = Expression.builder()
                .expression("#enabled = :true")
                .putExpressionName("#enabled", "enabled")
                .putExpressionValue(":true", AttributeValue.builder().bool(true).build())
                .build();

        ScanEnhancedRequest.Builder scanBuilder = ScanEnhancedRequest.builder()
                .limit(pageSize)
                .filterExpression(filter)
                .attributesToProject("ruleId", "name", "description", "level",
                        "tags", "enabled", "examplesCount", "createdAt", "updatedAt");

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
        Rule r = table.getItem(Key.builder().partitionValue(id).build());
        if (r == null) {
            throw new NoSuchElementException("Rule not found: " + id);
        }
        return r;
    }
}