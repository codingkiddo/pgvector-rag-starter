package com.codingkiddo.rag.service;

import com.codingkiddo.rag.store.DocChunkRepository;
import dev.langchain4j.model.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RagService {
    private final EmbeddingModel embeddingModel;
    private final DocChunkRepository repo;

    public RagService(EmbeddingModel embeddingModel, DocChunkRepository repo) {
        this.embeddingModel = embeddingModel;
        this.repo = repo;
    }

    public void ingestDocument(String collection, String docId, String text, Map<String, Object> metadata) {
        List<String> chunks = chunk(text, 800, 120);
        int i = 0;
        for (String chunk : chunks) {
            float[] emb = embeddingModel.embed(chunk).content().vector();
            String json = toJson(metadata);
            repo.upsert(collection, docId, i++, chunk, json, emb);
        }
    }

    public List<Map<String, Object>> search(String collection, String query, int k) {
        float[] emb = embeddingModel.embed(query).content().vector();
        return repo.knn(collection, emb, k);
    }

    private static List<String> chunk(String text, int maxLen, int overlap) {
        List<String> chunks = new ArrayList<>();
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(text.length(), start + maxLen);
            String piece = text.substring(start, end);
            chunks.add(piece);
            if (end == text.length()) break;
            start = Math.max(end - overlap, start + 1);
        }
        return chunks;
    }

    private static String toJson(Map<String, Object> map) {
        if (map == null || map.isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean first = true;
        for (Map.Entry<String, Object> e : map.entrySet()) {
            if (!first) sb.append(',');
            first = false;
            sb.append('"').append(escape(e.getKey())).append('"').append(':');
            Object v = e.getValue();
            if (v == null) sb.append("null");
            else if (v instanceof Number || v instanceof Boolean) sb.append(v.toString());
            else sb.append('"').append(escape(String.valueOf(v))).append('"');
        }
        sb.append('}');
        return sb.toString();
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
