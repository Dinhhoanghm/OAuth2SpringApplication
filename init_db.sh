docker exec -it postgres_db bash
psql -h localhost -p 5432 -U admin
CREATE SCHEMA admin
\c admin
