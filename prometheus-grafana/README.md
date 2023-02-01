#  
mkdir -p jmx-data 
mkdir -p graf-data 
mkdir -p prom-data
docker network create mservice_networks    
docker volume create --driver local --opt type=none --opt device=prom-data --opt o=bind --name=prom-data
# prometheusdata 
docker volume create --driver local --opt type=none --opt device=graf-data --opt o=bind --name=graf-data
# grafanadata
docker volume create --driver local --opt type=none --opt device=jmx-data --opt o=bind --name=jmx-data
# jmxdata
