### 🛡️ LV 0: AWS Budget 설정 완료
실수로 인한 고가의 리소스 비용 발생을 방지하기 위해 예산 알림을 설정했습니다.

* **월 예산**: $100.00
* **알림 임계값**: 예산의 80% ($80.00) 도달 시
* **알림 유형**: 실제 비용(Actual) 기준 이메일 알림

**[설정 증빙]**
![img.png](./images/img.png)

---

## 🌐 LV 1: 네트워크 구축 및 핵심 기능 배포
안전한 VPC 환경을 설계하고, 팀원 관리 API와 상태 모니터링(Actuator) 기능이 포함된 애플리케이션을 EC2에 성공적으로 배포했습니다.

### 1️⃣ 인프라 구축 (VPC & EC2)
* **VPC 설계**: Public Subnet과 Private Subnet을 분리하여 보안성을 강화한 네트워크 환경을 구축했습니다.
* **EC2 생성**: 외부 접속이 가능한 Public Subnet에 EC2 인스턴스를 생성하고 탄력적 IP를 연결했습니다.

### 2️⃣ 애플리케이션 개발 및 운영 전략
* **팀원 관리 API 구현**:
  * `POST /api/members`: 이름, 나이, MBTI 정보를 JSON으로 받아 저장
  * `GET /api/members/{id}`: 저장된 팀원 상세 정보 조회
  * `GET /api/members`: 저장된 팀원 전체 정보 조회
* **Profile 분리**: `application-local.yml`(H2)과 `application-prod.yml`(MySQL)로 환경을 분리하여 운영 안정성을 확보했습니다.
* **로그 전략**:
  * API 요청 시 `[API - LOG]` 포맷의 INFO 로그 기록
  * 예외 발생 시 ERROR 레벨의 스택트레이스 기록으로 트러블슈팅 구조 마련

### 3️⃣ 상태 모니터링 (Actuator)
* `spring-boot-starter-actuator` 의존성을 추가하고, 헬스 체크 엔드포인트를 노출하도록 설정했습니다.
* **설정값**: `management.endpoints.web.exposure.include=health`

### 4️⃣ 배포 검증 및 결과
* **배포 서버 퍼블릭 IP**: `3.38.106.17`
* **Health Check 확인**: 외부 브라우저에서 서버 상태가 `UP`임을 확인했습니다.

**[EC2 배포 확인 결과]**
![img_2.png](./images/img_2.png)

> **확인 주소**: [http://3.38.106.17:8080/actuator/health](http://3.38.106.17:8080/actuator/health)
> **응답 결과**: `{"status": "UP"}`
---

## 🔐 LV 2: DB 분리 및 보안 연결 (Parameter Store & Security Group Chaining)
애플리케이션의 보안성을 극대화하기 위해 하드코딩된 설정값을 제거하고, 인프라 간 직접적인 신뢰 관계(Security Group Chaining)를 구축했습니다.

### 1️⃣ 데이터베이스(RDS) 구축 및 보안 체이닝
* **RDS MySQL 구축**: 로컬 테스트 및 운영을 위해 AWS RDS(MySQL 8.0)를 생성했습니다.
* **보안 그룹 체이닝 (핵심)**:
  * RDS의 인바운드 규칙에서 불특정 다수(`0.0.0.0/0`)의 접속을 차단했습니다.
  * **소스(Source)** 항목에 Step 1에서 생성한 **EC2의 보안 그룹 ID(`sg-0de075fb5ff17b9f5`)**를 등록하여, 오직 인증된 EC2 서버를 통해서만 DB 접근이 가능하도록 설계했습니다.

**[RDS 보안 그룹 설정 증빙]**
![img_6.png](./images/img_6.png)

### 2️⃣ AWS Systems Manager (Parameter Store) 활용
* **민감 정보 관리**: DB 접속 정보(URL, Username, Password) 및 팀 이름을 코드에 노출하지 않고 AWS 클라우드 내부에 안전하게 관리했습니다.
* **계층 구조 설계**: `/config/teammember/` 경로를 사용하여 애플리케이션별 설정을 체계화했습니다.

**[Parameter Store 설정 리스트]**
![img_5.png](./images/img_5.png)

### 3️⃣ 애플리케이션 요구사항 구현 및 검증
* **Dynamic Property 주입**: `spring-cloud-aws-starter-parameter-store` 의존성을 활용해 런타임에 설정값을 주입받아 동작하도록 구현했습니다.
* **Actuator Info 확장**: Parameter Store에 저장된 `team-name`을 `/actuator/info` 엔드포인트에서 확인할 수 있도록 커스텀 설정을 적용했습니다.

### 4️⃣ 최종 배포 및 검증 결과
* **Actuator Info 엔드포인트**: [http://3.38.106.17:8080/actuator/info](http://3.38.106.17:8080/actuator/info)
* **결과**: 외부 브라우저 및 Postman에서 Parameter Store에 저장한 `"team4"` 값이 정상적으로 응답됨을 확인했습니다.

**[Postman 검증 화면]**
![img_3.png](./images/img_3.png)

**[브라우저 최종 확인 결과]**
![img_4.png](./images/img_4.png)