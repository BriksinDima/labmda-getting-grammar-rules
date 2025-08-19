package app.rules;

import io.micronaut.serde.annotation.Serdeable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.util.List;

@Serdeable
@DynamoDbBean
public class Rule {
    private String ruleId;
    private String name;
    private String description;
    private String level;
    private List<String> tags;
    private Boolean enabled;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("ruleId")
    public String getRuleId() { return ruleId; }
    public void setRuleId(String ruleId) { this.ruleId = ruleId; }

    @DynamoDbAttribute("name")
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @DynamoDbAttribute("description")
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @DynamoDbAttribute("level")
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    @DynamoDbAttribute("tags")
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    @DynamoDbAttribute("enabled")
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
}