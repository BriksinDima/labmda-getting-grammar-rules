package app.rules.web;

import app.rules.service.RulesService;
import app.rules.model.Rule;
import io.micronaut.http.annotation.*;
import io.micronaut.http.HttpResponse;
import jakarta.inject.Inject;

import java.util.Map;

@Controller("/rules")
public class RulesController {

    @Inject
    RulesService service;

    @Get
    public HttpResponse<Map<String, Object>> list(@QueryValue(defaultValue = "50") int limit,
                                                  @QueryValue(defaultValue = "") String lastKey) {
        return HttpResponse.ok(service.list(limit, lastKey.isBlank() ? null : lastKey));
    }

    @Get("/{id}")
    public HttpResponse<Rule> get(@PathVariable String id) {
        var rule = service.get(id);
        return rule != null ? HttpResponse.ok(rule) : HttpResponse.notFound();
    }
}