package com.codingkiddo.rag.api;

import java.util.Map;

public class IngestRequest {
    private String collection = "default";
    private String docId;
    private String text;
    private Map<String, Object> metadata;

    public String getCollection() { return collection; }
    public void setCollection(String collection) { this.collection = collection; }

    public String getDocId() { return docId; }
    public void setDocId(String docId) { this.docId = docId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}
