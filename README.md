# LuckyBurger7

> 가상의 햄버거 프랜차이즈

<!-- TOC -->

- [LuckyBurger7](#LuckyBurger7)
    - [1. 프로젝트 개요](#1-프로젝트-개요)
        - [1.1. ERD](#11-erd)
        - [1.2. 아키텍쳐](#12-아키텍쳐)
        - [1.3. 사용기술](#13-사용기술)
    - [2. 기능 설계](#2-기능-설계)
        - [2.1. 진행 배치 표](#24-진행-배치-표)
    - [3. 트러블 슈팅](#3-트러블-슈팅)
        - [3.1. 사용자 주문 조회에 대한 인덱스 적용 실패](#31-사용자-주문-조회에-대한-인덱스-적용-실패)
        - [3.2. 동시성 제어 테스트 중 불안정한 성공/실패 발생](#32-동시성-제어-테스트-중-불안정한-성공실패-발생)
        - [3.3. Redis 원자성 문제](#33-레디스-원자성-문제)
    - [4. 성능 개선](#4-성능-개선)
        - [4.1 사용자/점주 주문 전체 조회 성능 개선](#41-사용자점주-주문-전체-조회-성능-개선)
        - [4.2 쿠폰 발급 동시성 제어 안정성 개선](#42-쿠폰-발급-동시성-제어-안정성-개선)
        - [4.3 주문 성능 개선](#43-주문-성능-개선)
        - [4.4 장바구니 캐싱 적용](#44-장바구니-캐싱-적용)
    - [5. 주요 기능 및 API](#5-주요-기능-및-api)
        - [5.1. API 목록](#51-api-목록)
    - [6. 시연 영상](#6-시연-영상)
    - [7. 개발 환경](#7-개발-환경)
    - [8. Git 그라운드 룰](#8-git-그라운드-룰)
    - [9. 팀원](#9-팀원)
    - [10. 브로셔](#10-브로셔)

<!-- /TOC -->

## 1. 프로젝트 개요

- 가상의 햄버거 프랜차이즈를 구상하면서 소비자와 판매자 모두에게 효율적인 서비스 제공을 목표
- 관리자에게는 통계 데이터를 통해 통합적인 점포 관리 기능을 제공
- 점주에게는 매장 운영의 효율성을 극대화하는 필수 기능을 제공
- 사용자에게는 음식을 편리하게 주문할 수 있으며 쿠폰 적용을 통해 저렴한 가격으로 서비스를 이용

### 1.1. ERD

<img width="1516" height="699" alt="Image" src="https://github.com/user-attachments/assets/38df1816-b460-4c27-a4a2-2336f84db2d9" />

### 1.2. 아키텍쳐

<img width="1839" height="919" alt="Image" src="https://github.com/user-attachments/assets/de212b43-959d-4671-9d7c-86863742ff0c" />

### 1.3. 사용기술

 ✨ 언어 및 프레임워크  
<img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=JavaScript&logoColor=white"/>
<img src="https://img.shields.io/badge/SpringBoot-FCC624?style=flat&logo=SpringBoot&logoColor=white"/>
<img src="https://img.shields.io/badge/SpringDataJPA-0097A7?style=flat&logo=SpringDataJPA&logoColor=white"/>  

✨ 인증.인가  
<img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=flat&logo=SpringSecurity&logoColor=white"/>
<img src="https://img.shields.io/badge/JWT-5455FE?style=flat&logo=JWT&logoColor=white"/>  

✨ 데이터베이스  
<img src="https://img.shields.io/badge/mysql-4479A1?style=flat&logo=mysql&logoColor=white"/>
<img src="https://img.shields.io/badge/Redis-FF4438?style=flat&logo=Redis&logoColor=white"/>
<img src="https://img.shields.io/badge/RDS-5455FE?style=flat&logo=RDS&logoColor=white"/>  

✨ CI/CD & Infra  
<img src="https://img.shields.io/badge/Docker-2496ED?style=flat&logo=Docker&logoColor=white"/>
<img src="https://img.shields.io/badge/GitHubActions-2088FF?style=flat&logo=GitHubActions&logoColor=white"/>
<img src="https://img.shields.io/badge/EC2-1ED760?style=flat&logo=EC2&logoColor=white"/>
<img src="https://img.shields.io/badge/ECS-002244?style=flat&logo=ECS&logoColor=white"/>
<img src="https://img.shields.io/badge/ECR-F03E2F?style=flat&logo=ECR&logoColor=white"/>
<img src="https://img.shields.io/badge/GitHubActions-2088FF?style=flat&logo=GitHubActions&logoColor=white"/> 

✨ 테스트 / 성능 / 모니터링  
<img src="https://img.shields.io/badge/JUnit5-25A162?style=flat&logo=JUnit5&logoColor=white"/>
<img src="https://img.shields.io/badge/postman-FF6C37?style=flat&logo=postman&logoColor=white"/>
<img src="https://img.shields.io/badge/nGrinder-FF8000?style=flat&logo=nGrinder&logoColor=white"/>
<img src="https://img.shields.io/badge/Prometheus-E6522C?style=flat&logo=Prometheus&logoColor=white"/>
<img src="https://img.shields.io/badge/Grafana-F46800?style=flat&logo=Grafana&logoColor=white"/>
<img src="https://img.shields.io/badge/Loki-D4AA00?style=flat&logo=Loki&logoColor=white"/>  

✨ 협업 / 도구  
<img src="https://img.shields.io/badge/git-F05032?style=flat&logo=git&logoColor=white"/>
<img src="https://img.shields.io/badge/github-181717?style=flat&logo=github&logoColor=white"/>
<img src="https://img.shields.io/badge/intelliJ-21BDDB?style=flat&logo=intellijidea&logoColor=white"/>
<img src="https://img.shields.io/badge/ERDCloud-FAFAFA?style=flat&logo=ERDCloud&logoColor=white"/>
<img src="https://img.shields.io/badge/Figma-F24E1E?style=flat&logo=Figma&logoColor=white"/>
<img src="https://img.shields.io/badge/Draw.io-4B5562?style=flat&logo=Draw.io&logoColor=white"/>  


## 2. 기능 설계

### 2.1. 사용자(User)

- **1. 인증 & 계정관리**
    - **회원가입 & 로그인**: 이메일, 비밀번호, 이름, 연락처, 주소1, 주소2 정보를 가지고 회원가입 가능
    - **JWT기반 인증**: 이메일, 역할 을 포함하고 있음
    - **회원정보 수정**: 사용자는 개인정보를 수정 할 수 있다
- **2. 메뉴 및 매장**
    - **전체 사용(로그인 없이 누구나 가능)**
    - **메뉴 조회 및 검색**: 프랜차이즈에서 판매되는 메뉴를 검색 및 조회할 수 있다
    - **매장 조회 및 검색**: 프랜차이즈에 가입된 매장을 검색 및 조회하고 정보를 확인 할 수 있다
    - **특정 매장에서 판매하는 메뉴 조회**: 매장마다 판매하는 제품이 다를 수 있다
    - **이벤트, 쿠폰 조회**: 프랜차이즈에서 진행되는 이벤트 및 쿠폰을 조회 할 수 있다
- **3. 장바구니**
    - **장바구니 메뉴 설정**: 원하는 메뉴를 장바구니에 추가, 수정, 삭제 할 수 있다
    - **장바구니 메뉴 조회**: 장바구니에 담긴 메뉴를 확인 할 수 있다
- **4. 주문 & 쿠폰**
    - **쿠폰 조회 및 발급**: 프랜차이즈에서 진행하는 쿠폰이벤트의 쿠폰을 발급받을 수 있다
    - **주문**: 장바구니에 담긴 제품을 주문할 수 있다
    - **보유 쿠폰 & 보유 포인트 사용**: 사용자는 보유하고 있는 쿠폰이나 포인트를 결제에 사용할 수 있다
    - **주문 제품 조회**: 사용자는 자신이 주문한 내용을 단건, 전체로 조회 할 수 있다
    - **주문 취소**: 자신이 주문한 제품이 Waiting 상태면 주문 취소가 가능하다
    - **리뷰 작성**: 자신의 주문한 내용에만 리뷰를 작성, 조회, 수정, 삭제 할 수 있다

### 2.2. 점주(Owner)

- **1. 점포 주문**
    - **점포의 주문 조회**: 자신이 속한 점포의 주문을 단일, 전체 로 조회 할 수 있다
    - **주문 상태 변경**: 자신이 속한 점포의 주문 상태를 변경 할 수 있다 (준비, 조리, 배달, 완료, 취소)
- **2. 점포 관리**
    - **점포 별 메뉴 관리**: 자신이 속한 점포의 메뉴를 정할 수 있다 (판매, 재고부족, 비활성화)
    - **점포 별 쿠폰 관리**: 자신이 속한 점포의 쿠폰 상태를 정할 수 있다 (사용 가능, 사용 불가능)
    - **점포 별 영업상태 관리**: 자신이 속한 점포의 영업상태를 정할 수 있다 (영업중, 쉬는시간, 영업마감)
- **3. 점포 리뷰**
    - **리뷰 조회**: 자신이 속한 점포에 달린 리뷰를 확인 할 수 있다
    - **댓글 작성**: 자신이 속한 점포에 달린 리뷰에 댓글을 작성 할 수 있다

### 2.3. 관리자(Admin)

- **1. 점포 & 점주 등록**
    - **점포 등록**: 프랜차이즈의 점포를 등록, 수정, 삭제 할 수 있다
    - **점주 등록**: 점주의 계정을 생성, 등록, 삭제 할 수 있다
    - **점포 to 점주 등록**: 점포에 대한 점주의 계정을 연결 할 수 있다
- **2. 이벤트 & 쿠폰 관리**
    - **이벤트 관리**: 프랜차이즈에서 진행 할 이벤트를 등록, 수정, 삭제 할 수 있다
    - **쿠폰 관리**: 프랜차이즈에서 사용 할 쿠폰을 등록, 수정, 삭제 할 수 있다
- **3. 메뉴 관리**
    - **메뉴 관리**: 프랜차이즈에서 판매 할 메뉴를 등록, 수정, 삭제 할 수 있다
- **4. 관리자 대시보드**
    - **총 판매량**: 전체 점포에 대한 총 판매량을 확인 할 수 있다
    - **총 매출**: 전체 점포에 대한 총 매출을 확인 할 수 있다
    - **Top10 & Bottom10**: 각 점포의 매출을 Top10 & Bottom10으로 확인 할 수 있다
    - **메뉴 별 판매량**: 전체 메뉴 별 판매량을 확인 할 수 있다
 
### 2.4. 진행 배치 표
<img width="1891" height="845" alt="Image" src="https://github.com/user-attachments/assets/26701a57-1c4f-4b20-9510-876a0a963067" />
<img width="794" height="734" alt="Image" src="https://github.com/user-attachments/assets/2d64bc6e-e207-4497-9e69-1b90bf3ad88e" />
<img width="766" height="734" alt="Image" src="https://github.com/user-attachments/assets/be29c6c3-32e1-4678-94a8-23ac6346f3a3" />

## 3. 트러블 슈팅

### 3.1. 사용자 주문 조회에 대한 인덱스 적용 실패

- **1. 증상 및 로그**
    - **증상**: 사용자 주문 조회에 대한 복합 인덱스를 [설정 전] 과 [설정 후]의 테스트 결과가 크게 차이가 나지 않음  
      언뜻 보기에는 약간의 성능 향상이 된 것 같아 보이지만, 점수 데이터를 참고 하였을 때 해당 부분은 복합 인덱스가 정상적으로 적용이 된 것인지 의심이 됨

    - **로그**:    <pre>
      [설정 전]  
      TPS : 581 / sec  
      MTT : 106 ms  
      커넥션 대기 시간 1 sec  
      <img width="1280" height="568" alt="Image" src="https://github.com/user-attachments/assets/79391ef2-6e01-468a-b948-f33cd4674035" /> </pre>
      <pre> [설정 후]  
      TPS : 697 / sec  
      MTT : 73 ms  
      커넥션 대기 시간 331 ms
      <img width="1280" height="568" alt="Image" src="https://github.com/user-attachments/assets/87db2e16-2dca-40c8-a891-12e9a2ae15da" /> </pre>

    - **원인 분석**:
        - 점주의 주문 데이터가 많아서 효과가 커 보이는 것일까?
            - 해당 의문의 해소를 위해 점포의 수는 150개, 사용자 수는 50명으로 하여 사용자 주문 데이터가 많을 수 있도록 더미 데이터 수를 개선 - 결과 (X)
        - 사용자 주문에 대한 복합 인덱스가 잘 못 설계된 것일까?
            - 인덱스를 강제로 실행해 보는 진단용 쿼리문을 통해 테스트 - 결과 (X)

    - **해결 및 회고**: PC를 재부팅 하니깐 해결 됨. 아마도 여러번 테스트를 진행해 보면서 DB를 바꿔 보기도 하고, 인덱스를 숨겼다, 다시 적용 했다를 반복하다 보니 데이터가 꼬였던 것이 아닌가 생각
      됨

### 3.2. 동시성 제어 테스트 중 불안정한 성공/실패 발생

- **1. 증상 및 로그**
    - **증상**: 동일한 테스트 환경(사용자 수, 쿠폰 수량)에서 간헐적으로 테스트가 성공하거나 실패함    
      테스트 실패 시 로그에서 Redis stock key 조회 실패가 관찰됨

    - **원인 분석**:
        - CommandLineRunner (대용량 더미 데이터를 위한 BigDummyDataLoader)와 ApplicationRunner (Redis 초기화를 위한 RedisDataInitializer)가
          동일한 실행 시점 (ApplicationContext 초기화 직후)에 동작하기 때문이었음
        - 현재 Spring Boot에서 CommandLineRunner와 ApplicationRunner는 동일한 우선순위 레벨에서 실행되며, 따로 우선순위를 명시하지 않을 시 순서가 보장되지 않음
            - 일부 Spring Boot 버전(ex. 2.7)에서는 ApplicationRunner → CommandLineRunner 순서로 실행되었었음
        - Runner 실행 순서에 따라
            - RedisDataInitializer 가 먼저 실행되면 → 정상 작동
            - BigDummyDataLoader 가 먼저 실행되면 → 이후 초기화 로직으로 인해 앞서 준비된 stock 등 Redis 키가 초기화되어 테스트가 실패

    - **해결 방안**:
        - 실행 순서 명시적 보장
            - `@Order` annotation을 통해 Runner 실행 우선 순위를 지정
            - Redis 초기화가 완료된 후 더미 데이터 로딩이 실행되도록 순서 고정

    - **테스트**:
        - 수정 후 10회 이상 반복 테스트 (300명 동시 요청, 30개 쿠폰 기준)
        - 모든 테스트 케이스에서 정상적인 Redis 키 초기화 및 발급 성공 확인

    - **회고**:
        - 단순히 비즈니스 로직의 동시성 문제가 아니라 애플리케이션 실행 단계의 초기화 순서가 동시성 제어 테스트의 안정성을 해친 사례임
        - Spring Boot Runner의 실행 순서는 보장되지 않으므로 명시적으로 우선순위를 지정하는 과정이 필요함을 인지할 수 있었음

### 3.3. 레디스 원자성 문제

- **1. 증상 및 로그**
    - **증상**: Redis의 RedisTemplate 기능을 활용해 복잡한 로직을 구현할 경우 @TransactionAnotation을 걸어도 원자성이 보장되지 않는 문제가 발생함

    - **원인 분석**:
        - Redis에 @Transaction을 적용하려면 Redis의 Configration에서 template.setEnableTransactionSupport(true); 설정을 해야 정상 동작함
        - 하지만 이 방법은 동시성 문제를 방어하기 위해 락 적용시 락을 획득하기 위한 로직 때문에 응답속도가 늦어질 수 있다는 단점이 있음

    - **해결 방안**:
        - Lua Script를 사용해 원자성을 보장하고 스크립트 전체가 하나의 명령어이기 때문에 네트워크 왕복 시간을 줄이면서 동시성 문제도 해결할 수 있음

    - **테스트**:
        - 고의로 예외 발생을 위한 데이터 입력 후 의도에 맞게 데이터가 삽입되는지 확인함

    - **회고**:
        - Redis @Transaction과 Lua script는 롤백을 지원하지 않기 때문에 값에 대한 검증이 충분히 이뤄진 뒤에 수정, 생성을 해야 함

## 4. 성능 개선

### 4.1. 사용자/점주 주문 전체 조회 성능 개선

- **1. 성능 개선 (전)**
    - 데이터가 쌓이면서 주문 조회 시 응답 속도가 느리고, 간헐적으로 조회 실패를 하기도 함
    - DB에 저장된 주문 데이터가 100,000건 경우  
      TPS : 776 / sec  
      MTT : 167 ms  
      커넥션 대기 시간 4 sec
    - DB에 저장된 주문 데이터가 1,000,000건 이상일 경우  
      TPS : 49 / sec  
      MTT : 2002 ms  
      커넥션 대기 시간 6 sec  
      <img width="1280" height="565" alt="Image" src="https://github.com/user-attachments/assets/e7c8179f-d498-4394-82a1-404779b7d586" />

- **2. 원인 분석**
    - 주문 조회 시 DB에 데이터가 많이 쌓인 상태에서 풀 스캔이 동작 하다보니 성능 저하가 발생한 것으로 예측 됨
    - DB커넥션 대기 시간이 길다는 것은 DB처리가 오래 걸린다는 뜻이기도 하기 때문에 DB 조회 부분이 병목 지점이라 생각하고 이를 개선하고자 함

- **3. 개선 사항**
    - 사용자 주문 조회에 대한 복합 인덱스 설정 (사용자 id, 주문 날짜, 주문 id)
    - migration 을 통하여 버전 관리  
      <img width="623" height="97" alt="Image" src="https://github.com/user-attachments/assets/b683961c-9611-480f-8bc3-38142b7a22f5" />

- **4. 성능 개선 (후)**
    - DB에 저장된 주문 데이터가 1,000,000 건 이상일 경우
    - TPS : 420/ sec  
      MTT : 198 ms  
      커넥션 대기 시간 2 sec  
      <img width="1280" height="566" alt="Image" src="https://github.com/user-attachments/assets/6d0cf9b4-4dec-4e61-a845-b060ed00fc7a" />
    - TPS는 약 8.5배, MTT는 약 10배 가량 성능이 향상되었고, 대기 시간 또한 1/3 가량으로 줄어들어 성능이 개선 됨
      <img width="776" height="484" alt="image" src="https://github.com/user-attachments/assets/e9c421f4-4539-4e42-918a-47c77780b55a" />


- **5. 측정 방법/도구**
    - nGrinder를 통한 부하 테스트
    - Prometheus, Grafana를 통한 모니터링

### 4.2. 쿠폰 발급 동시성 제어 안정성 개선

- **도메인**
    - 한정 수량 쿠폰 발급 서비스 (이벤트 트래픽 환경)

- **1. 성능 개선 (전)**
    - RDBMS 단독 트랜잭션 기반 처리
    - 쿠폰 재고 30개, 300명 동시 요청 시
        - 초과 발급 발생 (70장)
        - Deadlock 다수 발생
    - 재고 차감을 DB 내부에서 수행 → 트랜잭션 충돌 및 지연 다수 발생
    - 외부 실패(서버 중단, 네트워크 오류 등)에 대한 복원 로직 부재
- **2. 개선 사항**
    - v1 - Redis 사전 제어 레이어 도입 (Fail-fast 구조)
        - 쿠폰 오픈, 중복 발급, 재고 확인 과정을 Lua script로 원자적 처리
        - DB 접근 전 검증이 통과된 요청만 실제 트랜잭션 수행 → DB 경합 축소
        - 여전히 DB에서 재고 차감을 수행할 경우 deadlock 약 19건 발생
    - v2 - 재고 차감 로직을 Redis로 이전 (DB 트랜잭션 최소화)
        - 쿠폰 재고 감소를 Redis에서 책임지고, DB에는 발급 정보 저장만 수행
        - 트랜잭션 내부의 공유 자원 접근이 사라져 deadlock 0건 달성
    - v3 - Redis Key 설계를 통한 외부 실패 내성 강화
        - TTL 기반의 Safety Lock 구조를 도입해 외부 실패에도 일관성 유지
        - 키 구조 설계
          | | **목적**                      | **설명**                                               |
          |-----------------------------------------|--------------------------------|--------------------------------------------------------|
          | gate:coupon:{couponId} | 쿠폰 오픈 제어 | 오픈 전 요청 차단, TTL 만료로 자동 해제 |
          | stock:coupon:{couponId} | 재고 관리 | Lua 스트립트에서 원자적 차감, TTL 만료로 쿠폰 만료 시 삭제 |
          | issued:set:coupon:{couponId} | 중복 발급 방지(발급 완료)       | Set 구조로 중복 확인 및 TTL 단일 관리 |
          | reserve:coupon:{couponId}:user:{userId} | Safety Lock(발급 시도 중)       | 짧은 TTL로 발급 시도 상태 유지 및 자동 해제 |

        - 발급 상태 전이 로직 요약
            - 발급 성공
                - issued:set에 사용자 추가 → reserve 키 삭제
            - DB 트랜잭션 실패
                - try-catch 블록에서 재고 복원 및 reserve 키 삭제
            - 프로세스 외부 실패
                - reserve 키 TTL 만료로 자동 삭제 → TTL 만료 이벤트 리스너를 두어, issued:set에 사용자가 없을 경우 자동 복원
        - TTL을 통해 오픈/만료/예약 상태를 자동 관리하여 메모리 누수를 방지함
- **3. 결과 (후)**  
  | | **발급 수량**       | **Deadlock** | **외부 실패 대응** | **개선 효과**     |
  |-----------------------------------|---------------------|--------------|--------------------|-------------------|
  | 개선 전(DB만)                      | 70/30장 (초과 발급)  | 다수 발생 | X | - |
  | 개선 v1 (Redis 적용, DB 차감)      | 11/30장 | 19건 | X | 초과 발급 방지 |
  | 개선 v2 (Redis 차감)               | 30/30장 | 0건 | X | 트랜잭션 충돌 제거 |
  | 개선 v3 (Redis 차감, 예약 키 적용)  | 30/30장 | 0건 | O | 에러 내성 강화 |

- **4. 측정 방법/도구**
    - nGrinder: 300명 동시 요청 시뮬레이션
    - 테스트 기준: Deadlock 발생 로그, 발급 성공 및 실패 응답 수, Redis Key TTL 동작 검증

### 4.3. 주문 성능 개선

- **도메인**
    - 유저가 담은 장바구니를 통해 주문 정보 생성

- **1. 성능 개선 (전)**
    - 장바구니에 담긴 메뉴가 늘어날수록 응답속도가 느려지는 상황이 발생  
      ( 동시에 300명 기준 5번 응답속도의 평균 )

  | **메뉴 수** | **평균 응답속도** | **p95** | **p99** |
  |-------------|-------------------|---------|---------|
  | 3 | 18.3ms | 46.3ms | 56.2 ms |
  | 20 | 128.7 ms | 281 ms | 355.2 ms |

  메뉴 수 증가함에 따라 평균 응답속도 약 7배, p95 약 6배, p 99 약 6.3배 정도의 응답속도 증가

- **2. 개선 사항**
    - (전) 주문 시 해당 유저의 orderForm 테이블의 데이터를 지운 뒤, 장바구니의 메뉴를 orderForm 테이블에 데이터를 저장하는 방식 → DB 접근이 반복
    - (후) 주문 시 해당 유저의 Id 값에따라 orderForm:{userId} 로 키를 설정하여 아이디마다 장바구니에 담긴 shopMenuId, price, quantity 세가지의 정보를  
      캐싱 저장하여 결제할 때 조회하여 사용

- **3. 결과 (후)**  
  (동시에 300명 기준 5번 응답속도의 평균)
  | | **평균 응답속도** | **p95** | **p99** |
  |-------------|-------------------|---------|---------|
  | 캐싱 전 | 128.7 ms | 281 ms | 355.2 ms |
  | 캐싱 후 | 75.5 ms | 159.2 ms | 187.6 ms |

  평균 응답속도 약 1.7배, p95 약 1.8배, p99 약 1.9배 가량 성능 향상  
  Redis 캐싱을 도입하여 반복 조회되는 데이터의 DB 접근을 줄임으로써 평균 응답속도와 p95, p99 응답속도를 크게 개선

  <img width="720" height="428" alt="Image" src="https://github.com/user-attachments/assets/c2648971-df60-4de0-82ba-0e4e0ba4c441" />

- **4. 측정 방법/도구**
    - nGrinder : 동시 300명 요청
    - Prometheus, Grafana: 평균 응답속도 및 p95, p99 모니터링

### 4.4. 장바구니 캐싱 적용

- **도메인**
    - 유저가 장바구니에 물건을 담는다

- **1. 성능 개선 (전)**
  | **VUser** | **평균 응답시간** | **MTT(평균 테스트 시간)** | **p95** | **p99** |
  |-----------|-------------------|---------------------------|---------|---------|
  | 50 | 143 ms | 227.5 ms | 265 ms | 340 ms |
  | 100 | 273 ms | 437.8 ms | 633 ms | 1.27 s |
  | 200 | 475 ms | 867.2 ms | 1.10 s | 1.63 s |
    - TPS: 151 / DB Connection Acquire time 증가

- **2. 개선 사항**
    - 모든 데이터 요청을 DB에서 처리하기 때문에 많은 요청이 몰릴 시 커넥션 풀이 고갈되어 응답 시간이 지연되는 상황 발생
    - Redis의 Lua Script를 도입하여 DB에 대한 접근횟수를 줄이는 방법 적용
    - 흐름 : Redis 조회 및 생성 → 실패 시 DB 사용 → 결과 응답
    - Lau Script는 Redis 내부에서 단일 명령으로 실행되기 때문에 네트워크 오버헤드가 적고 더 빠르며, 조건 분기 및 복합 연산을 한 번에 처리 할 수 있어 원자성과 동시성을 안정적으로 챙길 수 있음

- **2. 개선 사항**
    - (전) 주문 시 해당 유저의 orderForm 테이블의 데이터를 지운 뒤, 장바구니의 메뉴를 orderForm 테이블에 데이터를 저장하는 방식 → DB 접근이 반복
    - (후) 주문 시 해당 유저의 Id 값에따라 orderForm:{userId} 로 키를 설정하여 아이디마다 장바구니에 담긴 shopMenuId, price, quantity 세가지의 정보를  
      캐싱 저장하여 결제할 때 조회하여 사용

- **3. 결과 (후)**  
  | **VUser** | **평균 응답시간** | **MTT(평균 테스트 시간)** | **p95** | **p99** |
  |-----------|-------------------|---------------------------|---------|---------|
  | 50 | 58.8 ms | 127.9 ms | 104 ms | 144 ms |
  | 100 | 102 ms | 233.3 ms | 226 ms | 282 ms |
  | 200 | 150 ms | 446.6 ms | 320 ms | 422 ms |
    - TPS: 240 / DB Connection Acquire time 변화 없음

- **4. 측정 방법/도구**
    - nGrinder 세팅
        - 프로세스 : 5 (고정)
        - 스레드 : VUser / Process
    - Connect Pool Size : 21
    - 서버 환경 : local
    - 모니터링 도구 : Grafana, Prometheus

## 5. 주요 기능 및 API

### 5.1. API 목록

| **도메인**    | **기능**                    | **Method** | **URI**                                                  |
|------------|---------------------------|------------|----------------------------------------------------------|
| auth       | 로그인                       | POST       | /api/v1/login                                            |
|            | 점주 가입                     | POST       | /api/v1/admin/ownerSignup                                |
|            | 점주 수정                     | PUT        | /api/v1/admin/owners/{ownerId}                           |
|            | 점주 탈퇴                     | DELETE     | /api/v1/admin/owners/{ownerId}                           |
|            | 점주 전체 조회                  | GET        | /api/v1/admin/owners                                     |
| carts      | 장바구니 담기                   | POST       | /api/v2/user/carts                                       |
|            | 장바구니 메뉴 수정                | PUT        | /api/v2/user/carts                                       |
|            | 장바구니 메뉴 삭제                | DELETE     | /api/v2/user/carts                                       |
|            | 장바구니 조회                   | GET        | /api/v2/user/carts                                       |
| coupons    | 쿠폰 단일 조회                  | GET        | /api/v1/coupons/{couponId}                               |
|            | 쿠폰 전체 조회                  | GET        | /api/v1/coupons?page=0&size=10                           |
|            | 보유 쿠폰 전체 조회               | GET        | /api/v1/user/coupons?page=0&size=10                      |
|            | 쿠폰 발급                     | POST       | /api/v2/user/coupons/{couponId}                          |
|            | 쿠폰 추가                     | POST       | /api/v2/admin/coupons                                    |
|            | 쿠폰 수정                     | PUT        | /api/v2/admin/coupons/{couponId}                         |
|            | 쿠폰 삭제                     | DELETE     | /api/v2/admin/coupons/{couponId}                         |
|            | 활성 쿠폰 조회                  | GET        | /api/v1/admin/coupons/availability?page=0&size=10        |
| events     | 이벤트 단일 조회                 | GET        | /api/v1/events/{eventId}                                 |
|            | 이벤트 전체 조회                 | GET        | /api/v1/events/all                                       |
|            | 이벤트 단일 조회 NotDeleted      | GET        | /api/v1/events                                           |  
|            | 이벤트 추가                    | POST       | /api/v1/admin/events                                     |
|            | 이벤트 수정                    | PUT        | /api/v1/admin/events/{eventId}                           |
|            | 이벤트 삭제                    | DELETE     | /api/v1/admin/events/{eventId}                           |
| menus      | 메뉴 전체 조회                  | GET        | /api/v1/menus?page=0&size=10                             |
|            | 메뉴 단일 조회                  | GET        | /api/v1/menus/{menuId}                                   |
|            | 메뉴 검색                     | GET        | /api/v1/menus/search?menuName=000                        |
|            | 메뉴 추가                     | POST       | /api/v1/admin/menus                                      |
|            | 메뉴 수정                     | PUT        | /api/v1/admin/menus/{menuId}                             |
|            | 메뉴 삭제                     | DELETE     | /api/v1/admin/menus/{menuId}                             |
| orders     | 주문 준비                     | GET        | /api/v2/user/orderInfo                                   |
|            | 주문 생성                     | POST       | /api/v2/user/orders                                      |    
|            | 자기 주문 단일 조회               | GET        | /api/v1/user/orders/{orderId}                            |
|            | 자기 주문 전체 조회               | GET        | /api/v1/user/orders?page=0&size=10                       |
|            | 주문 취소                     | PUT        | /api/v1/user/orders/{orderId}                            |
|            | 점포 주문 단일 조회               | GET        | /api/v1/owner/orders/{orderId}                           |
|            | 점포 주문 전체 조회               | GET        | /api/v2/owner/orders?page=0&size=10                      |
|            | 주문 상태 변경                  | PUT        | /api/v1/owner/orders/{orderId}                           |
|            | 총 주문 수 카운트                | GET        | /api/v1/admin/orders/count                               |
| reviews    | 주문에 대한 리뷰 생성              | POST       | /api/v1/user/orders/{orderId}/reviews                    |
|            | 주문에 대한 리뷰 단일 조회           | GET        | /api/v1/user/reviews/{reviewId}                          |
|            | 주문에 대한 리뷰 수정              | PUT        | /api/v1/user/reviews/{reviewId}                          |
|            | 주문에 대한 리뷰 삭제              | DELETE     | /api/v1/user/reviews/{reviewId}                          |
|            | 점포 리뷰 전체 조회               | GET        | /api/v1/owner/shops/{shopId}/reviews/all                 |
|            | 점포 리뷰 전체 조회 NotDeleted    | GET        | /api/v1/owner/shops/{shopId}/reviews                     |
|            | 리뷰 댓글 작성                  | POST       | /api/v1/owner/shops/{shopId}/reviews/{reviewId}/comments |
| shops      | 점포 검색                     | GET        | /api/v1/shops/search?shopName=000                        |
|            | 점포 메뉴 전체 조회 NotDeactivate | GET        | /api/v1/shops/{shopId}/shopMenus                         |
|            | 점포 메뉴 상세 조회               | GET        | /api/v1/shops/{shopId}/shopMenus/{shopMenuId}            |
|            | 점포 대시보드                   | GET        | /api/v1/owner/shops/{shopId}/dashboard                   |
|            | 점포 메뉴 전체 조회               | GET        | /api/v1/owner/shops/{shopId}/menus                       |
|            | 점포별 메뉴 관리                 | PUT        | /api/v1/owner/shops/{shopId}/menus/{menuId}              |
|            | 점포별 쿠폰 사용 여부 변경           | PUT        | /api/v1/owner/shops/{shopId}/coupons/{couponId}          |
|            | 점포별 쿠폰 조회                 | GET        | /api/v1/owner/shops/{shopId}/coupons/{couponId}          |
|            | 점포 월 정산 조회                | GET        | /api/v1/owner/shops/{shopId}/sales/monthly?month=MM      |
|            | 점포 영업 상태 변경               | PUT        | /api/v1/owner/shops/{shopId}                             |
|            | 점포 추가                     | POST       | /api/v1/admin/shops                                      | 
|            | 점포 전체 조회                  | GET        | /api/v1/admin/shops                                      |
|            | 점포 수정                     | PUT        | /api/v1/admin/shops/{shopId}                             |
|            | 점포 삭제                     | DELETE     | /api/v1/admin/shops/{shopId}                             |
| statistics | 관리자 대시보드                  | GET        | /api/v1/admin/dashboard                                  |
|            | 총 월 매출 추이                 | GET        | /api/v1/admin/statistics/sales/monthly                   |
|            | 점포 별 매출 Top10             | GET        | /api/v1/admin/statistics/sales/shops/top10               |
|            | 점포 별 매출 Bottom10          | GET        | /api/v1/admin/statistics/sales/shops/bottom10            |
|            | 햄버거 메뉴 별 판매량              | GET        | /api/v1/admin/statistics/sales/menus/burger              |
|            | 사이드 메뉴 별 판매량              | GET        | /api/v1/admin/statistics/sales/menus/side                |
| users      | 회원 가입                     | POST       | /api/v1/signup                                           |
|            | 회원 탈퇴                     | DELETE     | /api/v1/withdraw                                         |
|            | 사용자 정보 수정                 | PUT        | /api/v1/user/profile                                     |
|            | 사용자 정보 조회                 | GET        | /api/v1/user/profile                                     |

## 6. 시연 영상

[시연 영상 링크](https://www.youtube.com/watch?v=VEFpxX6SzHw)

## 7. 개발 환경

- Java 17, Spring Boot, Spring Data JPA
- Spring Security, JWT
- MySQL, Redis, Flyway
- Gradle
- Junit 5, Testcontainers
- Prometheus, Grafana, Loki

## 8. Git 그라운드 룰

- **도메인 간 상호작용**
    - 서비스로만 통신 (타 도메인 Repository 사용 금지)
    - [Domain] + EntityFinder (엔티티 조회용 서비스 @Transactional(readonly) 필수)
- **예외처리 방법**
    - 예외처리 CustomException + 도메인별 코드 정의 -> 커스텀으로
    - [Domain] + ErrorCode
        - 도메인_NOT_FOUND
- Commit 컨벤션
    ```markdown
    - feat: 기능 개발
    - refactor: 리팩토링
    - fix: 버그 수정
    - chore: 빌드 관련 파일 수정 등
    - docs: 문서 작성 또는 수정
    - test: 테스트 코드
    
    <타입>: <제목> #이슈번호
    ```
- **Pull Request Rules**
    - **리뷰 시간**: 19:00 리뷰 시간 (다같이 모여서 리뷰 진행)
- **Project Management**
    - **이슈 기반 개발**: GitHub Issues 중심으로 관리하고, Projects(칸반 보드)와 연동하여 시각적으로 확인
    - Issue Label 및 Template 적극 활용

## 9. 팀원

| 이름      | Github                                |
|---------|---------------------------------------|
| **장태욱** | [링크](https://github.com/doldollee00)  |  
| **김기수** | [링크](https://github.com/Lunarltn)     |  
| **김동현** | [링크](https://github.com/donghyeon505) |  
| **장혜준** | [링크](https://github.com/joon448)      |  
| **유석진** | [링크](https://github.com/sonomooo)     |  

## 10. 브로셔

[럭키버거 브로셔](https://teamsparta.notion.site/7-2a22dc3ef514804194b8f1eb43b54409)
