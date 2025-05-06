<div align="center">
  <h1>정보보안 SW 웹/앱 개발 공모전 출품작</h1>
  <img src="https://github.com/user-attachments/assets/3971e081-340d-4853-ac38-564618c4a325"/>
</div>

## 🚀1. 목표와 기능

### 1.1 목표

<div align="center">
  <img src="https://github.com/user-attachments/assets/2912f8f1-b63b-4878-a083-65f47a52837e" width="300" height="300"/>
  <img src="https://github.com/user-attachments/assets/dd770a4f-1d44-490f-94ab-3e46b7337447" width="300" height="300"/>
</div>

1. 계속해서 사용자가 증가하는 **Open LLM**에 비해 비교적 보안에 대한 개념은 취약하다.
2. 다양한 기업들에서 **Open LLM** 사용에 대한 규제가 존재 혹은 사용금지 사항.
    1. 보안 사항들을 지키면서 **Open LLM**를 사용하는 방법은 없을까?라는  의문에서 출발하였다.
    2. 각 기업의 보안 규정에 맞는 **커스텀 LLM**를 제공하는 방법.

<div align="center">
  <img src="https://github.com/user-attachments/assets/7ee1f757-dff0-407b-99a8-9064bf2212d6" width="400" height="300"/>
  <img src="https://github.com/user-attachments/assets/0f94c595-21c1-4923-b219-0d33f7af4e7f" width="400" height="300"/>
</div>

3. 기업에서는 **생성형 AI**에 대해 투자하는 금액이 증가하는 추세지만 처음부터 **사내용 LLM**를 구축하는데 비용과 시간이 많이 소비된다. 
4. 3번같은 이유로 기업들은 대부분 이미 오픈소스로 구축되어있는 **AI모델**을 가져와서 미세조정하는 방식을 택한다.

⇒ **검열 AI**를 학습 시키고, **생성형 AI**와 통신 사이에 **검열 AI**를 거쳐서 **정보 보안**을 실현하자!

<br/>

### 1.2 기능

1. **회원가입**
    1. 회원 유형을 일반,회사 관리자와 직원으로 구분하여 회원가입을 진행.
    2. 휴대폰 번호 SMS 인증 및 이메일 인증 코드 검증을 통해 보안성 강화.
    3. 회사 코드의 발급을 통해 직원 가입 시, 관리자가 입력한 회사명과 회사 부서명을 기반으로 가입 진행.
2. **로그인**
    1. ***JWT***와 ***RTR(Refresh Token Rotation)***기법으로 보안성 강화.
    2. ***Spring Security***의 ***SecurityFilterChain*** 을 적용하여 안전한 인증 시스템 구축.
    3. ***Zustand***를 통해 상태 관리.
3. **다양한 LLM 모델 지원**
    - `Claude` : claude-3-5-haiku 모델.
    - `GPT` : GPT-4o-mini 모델.
    - `Gemini` : Gemini-1.5-flash 모델.
4. **보안 파일 업로드**
    1. 관리자 회원은 사내 주요 보안 문서를 업로드 가능.
        
        텍스트 **OR** 파일(csv, pdf, txt)
        
    2. 업로드한 내용은 DB에 저장되지 않고 ***Python AI 서버***로 직접 전송.
    3.  회사별 맞춤 보안 검열 AI는 직접 데이터를 학습하지 않고 ***차분 프라이버시(Differential Privacy)***를 통해 학습.
    4. 위의 과정들을 통해 회사 AI의 학습이 완료되고 회사별 맞춤형 보안 검열 서비스 제공.
5. **LLM 채팅** 
    1. 3가지 LLM 모델을 활용한 실시간 채팅 제공.
    2. 각 모델별로 독립된 채팅 환경을 구성하여 사용자 편의성 향상.
    3. 최근 대화 4개 조회 및 미리보기 기능 지원.
    4. 왼쪽 사이드 바에는 이전 대화 키워드 리스트 존재, 클릭하여 채팅방 이동 가능.
    5. 일반 회원: 기본 검열 모델을 적용하여 필터링된 질문 가능.
    6. 관리자 및 직원 회원: 회사별 맞춤 검열 A**I** 적용 후 질문 가능.
6. **마이페이지**
    1. 관리자 계정은 소속 회사의 직원 정보 조회 가능
        1. 조회 가능 항목 : 이름,이메일,직책
        2. 회원 이름을 기반으로 검색 기능 제공.
    2. 안전한 회원 탈퇴 기능 제공.
    3. 로그아웃 기능 제공.
7. **아이디 찾기 및 비밀번호 재설정**
    1. 아이디 찾기 - 가입한 유저의 휴대폰 번호로 가입한 이메일의 일부 값을 SMS 전송.
    2. 비밀번호 재설정 - 가입한 이메일로 랜덤한 임시 비밀번호 전송.

<br/>

### 1.3 팀 구성

| 디자이너 &nbsp;&nbsp;&nbsp;&nbsp; | 프론트엔드 | 백엔드 | 백엔드 | AI |  
|:-:|:-:|:-:|:-:|:-:|
|  | <img src="https://avatars.githubusercontent.com/u/123443488?v=4" width="150" height="150"/> | <img src="https://avatars.githubusercontent.com/u/144890194?v=4" width="150" height="150"/> |<img src="https://avatars.githubusercontent.com/u/152384393?v=4" width="150" height="150"/> | <img src="https://avatars.githubusercontent.com/u/84591332?v=4" width="150" height="150"/> |
| 김가현 | 안현석<br/>[@nyeonseok](https://github.com/nyeonseok) | 김도연<br/>[@tkv00](https://github.com/tkv00)  | 박재성<br/>[@pjs1710](https://github.com/pjs1710) | 이형준<br/>[@lhj010217](https://github.com/lhj010217) |

<br/>

## 🚀2. UI 및 시연 영상
### 2.1 UI
|Main                                  | SignUp                     |
|-------------------------------------------|-------------------------------------------|
|<img width="1278" alt="asdq124tgvzcx" src="https://github.com/user-attachments/assets/bc7ccb12-364b-40fb-9c3b-5474ede5967b" /> |  <img width="1278" alt="32rfscxz" src="https://github.com/user-attachments/assets/7a937da2-80ac-4050-87e9-c512ce7ffdc2" /> | 

|SignUp-일반인                                  | SignUp-관리자                     |
|-------------------------------------------|-------------------------------------------|
|<img width="1278" alt="ㅁㄴㅇㅌㅊㅋㅌㅊㅋㅊ" src="https://github.com/user-attachments/assets/d97d7b1a-38dd-4d4c-9722-f7a4211e6d04" /> | <img width="1267" alt="ㄴㅁㅌㅋㅊㅌㅋ" src="https://github.com/user-attachments/assets/8d7a05d0-3ffa-4127-bfc7-d4bcc49a0229" /> | 

|SignUp-직원                                  | Login                     |
|-------------------------------------------|-------------------------------------------|
|  <img width="1278" alt="er235" src="https://github.com/user-attachments/assets/17885238-e1af-41e9-a30f-db17e6bd401b" /> | <img width="1279" alt="svxcv" src="https://github.com/user-attachments/assets/6b181018-7fad-4a48-9492-d2fbe0543420" /> | 

|Find ID                            | Find PW                     |
|-------------------------------------------|-------------------------------------------|
|  <img width="1279" alt="ascascasd" src="https://github.com/user-attachments/assets/008631cd-d70a-4c02-86be-ad3b016e499b" /> |  <img width="1278" alt="adsadasdcxz" src="https://github.com/user-attachments/assets/309f92dc-3e1a-433f-800f-217248b74cd9" /> | 

|Select LLM Page                            | Recent Chat                  |
|-------------------------------------------|-------------------------------------------|
|  <img width="1274" alt="sdfvxczxcv" src="https://github.com/user-attachments/assets/62444816-d97e-4303-b8f4-319b109ec3fa" /> |  <img width="1278" alt="czxc" src="https://github.com/user-attachments/assets/a08da011-e510-4095-bc17-42772a68ca19" /> | 

|Claud Chat                     | GPT-4o-mini Chat                 |
|-------------------------------------------|-------------------------------------------|
| <img width="1280" alt="asczxc" src="https://github.com/user-attachments/assets/79cea2e7-90bd-400e-bb72-2c67ed7c6c40" /> | <img width="1280" alt="cZXCzCa" src="https://github.com/user-attachments/assets/c7d6987d-aac5-48f6-8ba4-8d3eb29ade0b" /> | 

|Gemini Chat                     | File Upload                 |
|-------------------------------------------|-------------------------------------------|
| <img width="1278" alt="axZXC" src="https://github.com/user-attachments/assets/784fcf34-07f8-4fab-b6b2-2af97698d021" /> | <img width="1277" alt="124zcx" src="https://github.com/user-attachments/assets/e07f2bec-c3a9-4072-8a4f-ccf65c950757" /> | 

|My Page                   | Company Employees Page              |
|-------------------------------------------|-------------------------------------------|
| <img width="1278" alt="lkjlkj" src="https://github.com/user-attachments/assets/c2ab6e27-25bc-4952-b07b-f39767253d78" /> | <img width="1279" alt="wefvxccx" src="https://github.com/user-attachments/assets/3208f879-1e8c-42b9-9f85-140662273b0d" /> | 


### 2.2 시연 영상
[시연영상 바로가기](https://drive.google.com/file/d/169hbamviDAUlYcrRy0zhkbk9yFFpBBb5/view)

<br/>

## 🚀3. 개발 환경 및 배포 URL
### 3.1 개발 환경
#### FrontEnd
[![My Skills](https://go-skill-icons.vercel.app/api/icons?i=remix,ts,zustand,cloudfront,lambda,aws&perline=6)](https://skillicons.dev)

- Remix
- TypeScript
- Zustand
- AWS CloudFront
- AWS Lamda

#### BackEnd
[![My Skills](https://go-skill-icons.vercel.app/api/icons?i=java,spring,redis,ec2,githubactions,gradle,mysql,junit,&perline=6)](https://skillicons.dev)
- Java
- Spring Boot
- JPA
- Redis
- AWS EC2
- Github Actions
- AWS Code Deploy
- MySQL
- Spring Security
- QueryDSL
- junit5


#### AI
[![My Skills](https://go-skill-icons.vercel.app/api/icons?i=python,tensorflow,pytorch,docker,ec2,fastapi,&perline=6)](https://skillicons.dev)
- Python
- BERT Classifier
- TensorFlow
- Opacus
- PyTorch
- Unicorn
- FastAPI
- AWS EC2
- Docker

#### Communication
[![My Skills](https://go-skill-icons.vercel.app/api/icons?i=slack,jira,discord&perline=6)](https://skillicons.dev)
- Slack
- Jira
- Discord

<br/>

### 3.2 배포 URL
[Vero 바로가기](https://d1ujq2bm8j3hx9.cloudfront.net/)

## 🚀4. Prototype
![sdfvxcz](https://github.com/user-attachments/assets/719ef74e-0a98-4f11-8436-91d6c34fcf34)

## 🚀5. 시스템 구조 및 워크 플로우
### 5.1 시스템 구조
#### FrontEnd
![dfgcbxcv](https://github.com/user-attachments/assets/a175ac6c-4e2d-4a36-898b-cc9eaebe246e)

<br/>

#### BackEnd
![qweqe 1](https://github.com/user-attachments/assets/c5bf3e7b-8017-49ec-9846-9320a00d15b1)

<br/>

#### AI
![ㅁㄴㅇㅁㄴㅇㅊ](https://github.com/user-attachments/assets/577b6655-45e6-4d4e-8f42-8f0918cea0bc)


<br/>

### 5.2 워크 플로우
#### BackEnd
![sdfxzcxzc](https://github.com/user-attachments/assets/9242b699-d198-4e1f-94e1-85dc85dfdc84)

#### AI
![image 10](https://github.com/user-attachments/assets/407b72c7-5094-48a9-87c0-ab256f62b80c)

## 🚀6. API Docs
![sdvxzcbvxcb](https://github.com/user-attachments/assets/f3da81f5-a9ea-418f-9f32-a907bc0c0de4)
![sdfsvxzcv](https://github.com/user-attachments/assets/3b780fd2-22ef-4d6e-b022-9758618c08c6)
![xzcvxczv](https://github.com/user-attachments/assets/8c1aa862-6b3a-4471-905f-d71d8c2a0bb0)
![dszv1](https://github.com/user-attachments/assets/3c34c20d-1f5f-468e-92ee-50671ef79020)
![cvzx2](https://github.com/user-attachments/assets/5a861976-fcfe-4c55-950b-087ffc03a52d)
![vsdaa3](https://github.com/user-attachments/assets/100b599d-ff08-4839-a776-b4e8833f72bb)


## 🚀7. ERD
![bnvcbnvn](https://github.com/user-attachments/assets/ea27010b-6f03-43eb-9f95-31cf8369781c)
