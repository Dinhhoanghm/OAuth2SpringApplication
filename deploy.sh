cd db
docker build -f Dockerfile  -t aiv-db:v1.0.0 .
docker rm -f aivhub_db
docker run --name aivhub_db -p 5433:5432 -d aiv-db:v1.0.0
cd fe
docker build -t aivhub-frontend:v1.0.0 .
docker rm -f aivhub-frontend
docker run -d -p 5173:8080 --name aivhub-frontend aivhub-frontend:v1.0.0
cd ../
docker build -f Dockerfile  -t aiv-hub:v1.0.0 .
docker rm -f aivhub-application
docker run --name aivhub-application --net=host -e "JAVA_ARGS=-Dspring.profiles.active=local" -d aiv-hub:v1.0.0