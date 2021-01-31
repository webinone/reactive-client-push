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
   ├─docker
   │    docker-compose.yml  (redis, kafka local에 구성)
   │
   ├─src/main/java
   │   │  README.md
   │   ├─hub (허브컨트롤룸)
   │   └─hq (hq컨트롤룸)
   └─server (zoo server project)
       │  README.md
       │  build.gradle
       │  JenkinsFile
       ├─autocall-server
       │     README.md
       │     build.gradle
       │     Dockerfile
       ├─client-api
       │     README.md
       │     build.gradle
       │     Dockerfile
       ├─core-api
       │     README.md
       ├─open-api
       │     README.md
       │     build.gradle
       │     Dockerfile
       ├─data-server
       │     README.md
       │     build.gradle
       │     Dockerfile
       └─oauth-server
            README.md
            build.gradle
            Dockerfile
            
            
   src\
   ..main\java
		 ... \io
			.... \barogo
				..... \{project}
					..... \api -> controller 패키지
						...... \advice -> controller advice 패키지
          ..... \common -> 공통 utils등의 모듈등이 속해 있는 패키지
          ..... \config -> spring config 설정 패키지
          ..... \exception -> exception 패키지
					..... \model -> dto, vo, entity(domain) 패키지
						...... \dto -> dto 패키지
						...... \entity -> jpa entity 패키지
						...... \vo -> vo 패키지
					..... \service -> service 패키지
          ..... \persistence (dao)
						...... \mapper -> mybatis mapper 패키지
						...... \repository -> jpa repository 패키지

     ...\resources
   ..test\java
     ...\resources
```

### Setup


