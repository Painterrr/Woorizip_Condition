# woorizip-condition
--------------------------------------

## 프로젝트 이름
### 우리집Woorizip
- 프로젝트 오가니제이션 주소: https://github.com/orgs/TeamWAF/repositories <br>
(전체 public으로 전환 대기 중)
<br><br>

 
## 프로젝트 로고
- <img src="https://github.com/Painterrr/Woorizip/blob/main/application_logo.png" width="200" height="200" />
<br><br>
 
## ⭐ 프로젝트 정보
- 목적: 부동산 중개 서비스 개선
- 개발 기간: 2024.03.18 ~ 05.03 (7주)
<br><br>
 
## 👤 팀 소개
** 각 도메인 별 FE, BE, CI/CD 구성
- 조승빈: 팀장, 채팅(chatting)
- 권성준: 조건(condition)
- 이명진: 매물(estate, zip)
- 한상우: 계정(account)
<br><br>
 
## 💠 서비스 소개
![007](https://github.com/Painterrr/Woorizip/assets/98957340/92f3af41-8262-4be0-9919-13d1460302ac)
부동산 중개 서비스. <br>

의뢰인들이 발품 팔던 시대는 지나고, 중개사들이 먼저 연락하는 시대! <br>

의뢰인들은 매물을 조회하고 원하는 매물이 없을 시, "원하는 매물의 조건을 등록"하기만 하면 끝. <br>

중개사들은 조건에 부합하는 매물을 보유하고 있을 때, 의뢰인에게 채팅을 통해 연락 가능. <br>

![010](https://github.com/Painterrr/Woorizip/assets/98957340/4f96565c-b22e-4cbf-9c0e-056f82b803b3)
<br><br>

## 서비스 아키텍처
![019](https://github.com/Painterrr/Woorizip/assets/98957340/a3746c0f-b361-4a66-a27c-c546f7c7395b)
 <br>
- MSA
- 내부 서버 간 gRPC 통신으로 "언어중립적 특성" 및 "멀티플렉싱(Multiplexing)" 확보
- 이외 서버 간 통신은 https로 진행
- 비즈니즈 유연성 확보
- 서비스 안정성 확보
<br><br>
 
## 시스템 아키텍처
![021](https://github.com/Painterrr/Woorizip/assets/98957340/b69098e9-4d7a-472b-8c93-3742476a231f)
<br>
* CI/CD
 - Jenkins: 자동 CI로 이미지 빌드. 빌드된 이미지를 ECR에 전송. 해당 이미지의 API를 Argo에 전송.
 - ECR: 빌드된 이미지 리포지토리.
 - Argo: 자동 CD. ECR로부터 이미지를 전달받고 jenkins로부터 받은 API로 CD 확인 및 깃허브 소스와의 sync 및 health check 진행. CD 모니터링 가능.
* Security
 - ACM: SSL, TLS 인증서 발급 -> https 사용
 - WAF: SQL Injection 및 DDoc 방어
* 고가용성
 - Kubernetes: 클러스터를 구성하여 고가용성 확보.
 - ALB: 타겟그룹 간 로드밸런싱.
 - Karpenter: 클러스터 오토 스케일러. 기존 CAS에 비하여 구조가 간편하여 좀 더 빠르고 유연함. 노드 비용도 CAS에 비하여 절감 가능.
 <br><br>

## CI/CD Pipeline
![024](https://github.com/Painterrr/Woorizip/assets/98957340/3cbf7ac7-6a78-41cc-9799-003509162b3a)
