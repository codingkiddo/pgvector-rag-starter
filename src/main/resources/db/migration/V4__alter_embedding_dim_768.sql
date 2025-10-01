-- Adjust embedding dimension to 768 for nomic-embed-text
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns 
             WHERE table_name='doc_chunks' AND column_name='embedding') THEN
    BEGIN
      ALTER TABLE doc_chunks ALTER COLUMN embedding TYPE vector(768);
    EXCEPTION WHEN others THEN
      RAISE NOTICE 'Could not alter embedding dimension automatically. Drop & recreate or migrate data.';
    END;
  END IF;
END $$;
