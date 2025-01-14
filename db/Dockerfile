# Use the official PostgreSQL image as the base image
FROM postgres:14.1-alpine

# Set environment variables for PostgreSQL
ENV POSTGRES_USER=admin
ENV POSTGRES_PASSWORD=admin
ENV POSTGRES_DB=test

# Copy initialization scripts to the container
# All `.sql` and `.sh` files in `/docker-entrypoint-initdb.d/` are executed during container startup
COPY db_generate.sql /docker-entrypoint-initdb.d/

# Expose PostgreSQL's default port
EXPOSE 5432
