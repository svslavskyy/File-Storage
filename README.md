## Installation requirments
1) ```git clone https://github.com/svslavskyy/File-Storage.git```
2) ```cd ./elasticsearch/bin elasticsearch```   
3) ```cd File-Storage```
4) ```mvn spring-boot:run```


## Testing 

```curl http://localhost:8080/file``` Post File

```curl http://localhost:8080/file/{id}``` GET File by id

```curl http://localhost:8080/file/{id}}/tags``` Post tags to File

```curl http://localhost:8080/file/{id}/tags``` Delete tags to File

```curl http://localhost:8080/file/{id}``` Delete File
