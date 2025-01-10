docker run --name -p 5432:5432 postgres_db -e POSTGRES_PASSWORD=admin -d postgres
docker exec -it postgres_db bash
psql -h localhost -p 5432 -U admin
CREATE SCHEMA admin
\c admin
run db_generate.sql
