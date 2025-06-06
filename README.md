# 🚀 DEALLY  
**"진짜 좋은 거래를 경험하다"**  

📌 **프로젝트 설명:**  
DEALLY는 이용자 간의 상품 등록, 조회, 구매 및 1:1 채팅을 통해 자유롭게 거래할 수 있는 **중고거래 웹사이트**입니다.  

---

## 🛠 기술 스택  

### 🔹 백엔드  
<p align="left"> <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/> 
  <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/> 
  <img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"/> 
  <img src="https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white"/> 
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white"/> 
  <img src="https://img.shields.io/badge/WebSocket-4EA94B?style=for-the-badge&logo=appveyor&logoColor=white"/> </p>


### 🔹 프론트엔드
<p align="left"> <img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white"/> 
  <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white"/> 
  <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white"/> 
  <img src="https://img.shields.io/badge/Bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white"/> 
  <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white"/> </p>

---

## 🧩 ERD
![image](https://github.com/user-attachments/assets/7d54a11b-88da-4e78-b0fc-1ce306562a75)



## 🔥 주요 기능

### 1️⃣ 회원 기능

#### 📝 회원가입
- 이메일 형식의 ID 사용 
- 이메일, 닉네임 중복 검사

#### 👤 마이페이지
- **내 정보 조회 및 수정**
  - 비밀번호변경 가능
- **구매 내역 확인**
- **판매 내역 확인**

#### 🔐 로그인 및 보안
- **JWT 인증**
  - 로그인 시 쿠키에 **JWT 토큰 발급** (Access / Refresh)
 
 ### 2️⃣ 상품 
 - 상품 등록 / 조회 / 수정 / 삭제 (CRUD)
- 상품 이미지 업로드 및 미리보기 기능
- 상품명 기반 검색 기능
- 거래 상태 설정 (판매중 / 거래완료)
- 판매자 전용 기능: 거래 상대 지정, 상태 변경 

### 3️⃣ 채팅
- 상품 상세 페이지에서 채팅하기 버튼 클릭 시 채팅방 생성 또는 이동
- 상품 등록자와 로그인 사용자를 기준으로 1:1 채팅방 생성 (중복 생성 방지)
- WebSocket을 이용한 실시간 채팅 기능
- 채팅 메시지 DB 저장 및 채팅 내역 불러오기
- 유저 본인의 채팅방 목록 조회 가능
  
