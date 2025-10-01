package com.codingkiddo.rag.store;

import com.pgvector.PGvector;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DocChunkRepository {
    private final JdbcTemplate jdbcTemplate;

    public DocChunkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void upsert(String collection, String docId, int chunkId, String content, String metadataJson, float[] embedding) {
    	PGvector vec = new PGvector(embedding);
        String sql = "INSERT INTO doc_chunks (collection, doc_id, chunk_id, content, metadata, embedding) " +
                     "VALUES (?,?,?,?,?::jsonb,?) " +
                     "ON CONFLICT DO NOTHING";
        jdbcTemplate.update(sql, ps -> {
            ps.setString(1, collection);
            ps.setString(2, docId);
            ps.setInt(3, chunkId);
            ps.setString(4, content);
            ps.setString(5, metadataJson == null ? "{}" : metadataJson);
            ps.setObject(6, vec);
        });
    }

    public List<Map<String, Object>> knn(String collection, float[] query, int k) {
    	PGvector vec = new PGvector(query);
        String sql = "SELECT id, doc_id, chunk_id, content, (embedding <-> ?) AS distance " +
                     "FROM doc_chunks " +
                     "WHERE collection = ? " +
                     "ORDER BY embedding <-> ? " +
                     "LIMIT ?";
        return jdbcTemplate.query(sql, ps -> {
            ps.setObject(1, vec);
            ps.setString(2, collection);
            ps.setObject(3, vec);
            ps.setInt(4, k);
        }, (rs, rowNum) -> Map.of(
                "id", rs.getLong("id"),
                "doc_id", rs.getString("doc_id"),
                "chunk_id", rs.getInt("chunk_id"),
                "content", rs.getString("content"),
                "distance", rs.getDouble("distance")
        ));
    }
}
