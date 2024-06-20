# 가치페이

## 프로젝트 소개
- 친구, 가족, 동호회 등 각종 모임에서 돈을 모아 함께 사용하는 단체 간편결제 시스템입니다.
- 금융 상품을 통해 제테크를 할 수 있으며 목표 금액 설정 후 도달 시 자동으로 구성원들에게<br>
분배해주는 서비스를 제공합니다.

### 주요 기능

** 타사 혹은 자사의 계좌 혹은 카드가 있다는 가정하에 진행 **<br>
** PG(Payment Gateway)로 등록되었다는 가정하에 진행 **<br>
- [ 회원 ] 
  - 회원 정보 조회
  - 계좌 등록 혹은 카드 등록
```markdown
- 계좌 혹은 카드 등록 시 Access Token 발급(Redis에 저장)
```

- [ 그룹 ]
  - 그룹 생성 및 대표 계좌 등록
  - 그룹 멤버 등록
  - 가치 머니(선불금) 충전
  - 정기 회비 납부
  - 그룹 상세 정보 조회 ( 정기 납부일, 그룹 충전금, 멤버 별 총 납부액, 멤버 별 당월 미납 여부 )
  - 그룹 해지

```markdown
- 대표 계좌는 그룹 생성 멤버의 계좌로 생성
- 목표 금액 설정 가능
- 해당 멤버가 미납 상태라면 그룹 탈퇴 불가
- 멤버 모두가 동의할 시 그룹 해지 가능
```

- [ 거래 ]
  - 거래 내역 조회
  - 그룹의 멤버들에게 거래 시 알림

- [ 거래 - 결제 ]
  - 결제
```markdown
- Access Token을 사용하여 카드사에 결제를 요청하는 API 호출
```

- [ 금융 상품 ]
  - 예적금
  - 금융 상품 추천

### 부가 기능 
** 주요 기능 구현 후 추가 예정 

- 가계부
- 모임 활동 추천 ( ex.소비나 자산에 따른 다른 활동 추천 )

### 키 포인트 
- RESTful API 디자인 원칙 따르기
- 본인 인증 및 로그인 (JWT, OAuth2, MFA)
- 거래 데이터 보호(SSL/TLS)
- 결제와 송금에 관련된 트랜잭션 처리
- 중복 거래 방지(AOP)
- 결제 데이터 수집

## ERD
![image](https://private-user-images.githubusercontent.com/167763898/341130388-200aab29-5d8e-4153-971b-e7eda347dea5.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MTg4NjE2NjksIm5iZiI6MTcxODg2MTM2OSwicGF0aCI6Ii8xNjc3NjM4OTgvMzQxMTMwMzg4LTIwMGFhYjI5LTVkOGUtNDE1My05NzFiLWU3ZWRhMzQ3ZGVhNS5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQwNjIwJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MDYyMFQwNTI5MjlaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0zYzUxOWNmNzQwMjU4MTRkNjgyYzk5ZjI0ZDAxMzFkMTNhNjFlZTU2N2YzMDkyNmUzOWEyZDYyZGQ5ZDgyNzM2JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.fWFQsgmVGB1L266NKXahjwaGjCYRLmTz9Kbw4-GAIYw)