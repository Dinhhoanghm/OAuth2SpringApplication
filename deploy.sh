cd db
docker build -f Dockerfile  -t aiv-db:v1.0.0 .
docker run --name test -p 5433:5432 -d aiv-db:v1.0.0
cd ../
docker build -f Dockerfile  -t aiv-hub:v1.0.0 .
docker rm -f aivhub-application
docker run --name aivhub-application --net=host -e "JAVA_ARGS=-Dspring.profiles.active=local" -d aiv-hub:v1.0.0