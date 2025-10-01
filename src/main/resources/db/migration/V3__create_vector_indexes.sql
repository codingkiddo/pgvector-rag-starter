CREATE INDEX IF NOT EXISTS idx_doc_chunks_embedding_ivf
ON doc_chunks USING ivfflat (embedding vector_cosine_ops)
WITH (lists = 100);
ANALYZE doc_chunks;
