CREATE TABLE IF NOT EXISTS doc_chunks (
  id           BIGSERIAL PRIMARY KEY,
  collection   TEXT NOT NULL DEFAULT 'default',
  doc_id       TEXT NOT NULL,
  chunk_id     INT  NOT NULL,
  content      TEXT NOT NULL,
  metadata     JSONB DEFAULT '{}'::jsonb,
  embedding    vector(1536) NOT NULL,
  created_at   TIMESTAMPTZ DEFAULT now()
);
CREATE INDEX IF NOT EXISTS idx_doc_chunks_collection ON doc_chunks(collection);
CREATE INDEX IF NOT EXISTS idx_doc_chunks_doc ON doc_chunks(doc_id);
