
## Create Docker with postgis DB
docker run -d -p=5432:5432 --network=project --name postdb -e POSTGRES_PASSWORD=postgres -e POSTGRES_USERNAME=postg
res -v=proejct-db:/var/lib/postgresql/data postgis/postgis:latest

