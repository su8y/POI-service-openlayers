# postgis-project

PostGresql &amp; spring boot &amp; postgis

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
 

