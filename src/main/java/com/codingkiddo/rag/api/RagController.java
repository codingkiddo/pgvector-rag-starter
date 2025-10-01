package com.codingkiddo.rag.api;

import com.codingkiddo.rag.service.RagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RagController {
    private final RagService service;

    public RagController(RagService service) {
        this.service = service;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of("status", "ok");
    }

    @GetMapping("/search")
    public List<Map<String, Object>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "default") String collection,
            @RequestParam(defaultValue = "10") int k
    ) {
        return service.search(collection, q, k);
    }
}
