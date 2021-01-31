# client-push
zoo project의 client push 역할을 담당한다.

## Features
`라이더앱`, `컨트롤룸`, `관제` 등에 `라이더 위치정보`,`주문정보등록/변경`,`공유정보`,`사용자/허브설정정보` 등을 push 정보 한다. 

## Skill
* spring webflux
* spring reactive websocket 
* spring data reactive redis
* spring data r2dbc
* spring reactive kafka
* gradle project

## Server dependency
* postgresql
* kafka
* redis ? (NoSQL)
* data-server
* client-api
* open-api

## Project 및 패키지  구성

```sh
├─client-push
   │  README.md
   │  DockerFile
   │  build.gradle
   ├─docker
   │    docker-compose.yml  (redis, kafka local에 구성)
   │
   ├─src/main/java
   │   │  ClientPushApplication
   │   ├─ io/bargo/push (허브컨트롤룸)
   │   │   │
   │   │   ├─ api (webflux controller)
   │   │   │  
   │   │   ├─ common (공통모듈)
   │   │   │
   │   │   ├─ config (프로젝트 설정 정보)
   │   │   │  kafka, redis, r2dbc config
   │   │   ├─ handler (websocket handler)
   │   │   │  websocket 실제 구현 handler
   │   │   ├─ model 
   │   │   │  └─ dto
   │   │   │  └─ entity (r2dbc entity)
   │   │   │
   │   │   ├─ publisher (global publisher)
   │   │   ├─ repository
   │   │   └─ service
   │   │    
   │   └─src/main/resources  
   │
   └─src/test/main       
       │    
       └─src/test/resources  
```

### Setup


