package com.codingkiddo.rag.api;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codingkiddo.rag.service.RagService;

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
    
    @PostMapping("/ingest")
    public Map<String, Object> ingest(@RequestBody IngestRequest req) {
        if (req.getDocId() == null || req.getDocId().isBlank()) {
            return Map.of("ok", false, "error", "docId is required");
        }
        if (req.getText() == null || req.getText().isBlank()) {
            return Map.of("ok", false, "error", "text is required");
        }
        service.ingestDocument(
                (req.getCollection() == null || req.getCollection().isBlank()) ? "default" : req.getCollection(),
                req.getDocId(),
                req.getText(),
                req.getMetadata()
        );
        return Map.of("ok", true, "docId", req.getDocId(), "collection", req.getCollection());
    }
}
