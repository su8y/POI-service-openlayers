# postgis-project

PostGresql &amp; spring boot &amp; postgis

## Info 

* service 
  * 네이버 API를 이용한 경로 찾기 및 찾은 경로 저장 
  * 네이버 거리뷰 API를 이용한 POI데이터 거리뷰 
  * 더미 POI 데이터를 활용한 지도에서 POI데이터 조회하기
  * 사용자가 POI데이터를 추가가 가능함.


## Install

----

1. install wsl

```powershell 
wsl --install
wsl --set-version Ubuntu 2
wsl --set-default-version 2
```

2. install ubuntu:18.04    
   [download page https://www.microsoft.com/store/productId/9PNKSF5ZN4SW](https://www.microsoft.com/store/productId/9PNKSF5ZN4SW)
3. install docker & docker-desktop (deamon)

```bash 
brew install docker 
brew install docker-desktop
```

4. pull image (postgis/postgis, adoptopenjdk/openjdk11:latest)

```bash 
docker pull postgis/postgis:latest
docker pull adoptopenjdk/openjdk11:latest
```
5. run ddl.sql & data.sql after running docker conatainer   
   **easy Command**
    ```bash
   $ docker compose up 
   $ docker exec -it db /bin/bash
   # in bash 
   $ psql -Upostgres project
   # work something..
   ```
6. Postgresql DummyData setting
```sql
update poi set coordinates = st_setsrid(st_makepoint(poi.lon,poi.lat),4326); 
```
6. npm install 
```shell
$ cd src/main/resources/static
$ npm install
```
7. run gradle `boot jar ` or `complieJava`

8. create `security-env.yml`
```yaml
naver:
   application-name: poi-project
   secret:
      key: ${your secret key} # HTTP HEADER: X-NCP-APIGW-API-KEY
      header: X-NCP-APIGW-API-KEY
   client:
      id: ${your client id}
      header: X-NCP-APIGW-API-KEY-ID
   api:
      path:
         direction5: https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving
         geocoding: https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode
         reverse-geocoding: https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc
```
