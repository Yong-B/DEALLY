# 🚀 DEALLY  
**"진짜 좋은 거래를 경험하다"**  

📌 **프로젝트 설명:**  
DEALLY는 당근마켓과 같은 **지역 기반 중고거래 플랫폼**입니다.  

---

## 🛠 기술 스택  

### 🔹 백엔드  
<p align="left"> <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/> 
  <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/> 
  <img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"/> 
  <img src="https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white"/> 
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white"/> </p>

### 🔹프론트엔드
<p align="left"> <img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white"/> 
  <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white"/> 
  <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white"/> 
  <img src="https://img.shields.io/badge/Bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white"/> 
  <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white"/> </p>

---


## 🔥 주요 기능

### 1️⃣ 회원 기능

#### 📝 회원가입
- 이메일 형식의 ID 사용 
- 이메일, 닉네임 중복 검사

#### 👤 마이페이지
- **내 정보 조회 및 수정**
  - 이름, 비밀번호, 전화번호 변경 가능
- **구매 내역 확인**
- **판매 내역 확인**
- **찜한 상품 목록 조회**

#### 🔐 로그인 및 보안
- **JWT 인증**
  - 로그인 시 쿠키에 **JWT 토큰 발급** (Access / Refresh)
- **비밀번호 찾기**
  - 이메일 주소를 통해 임시 비밀번호 발급
- **보안 강화**
  - `httpOnly` 쿠키 설정으로 XSS 공격 방어
  - `SameSite=Lax` 설정으로 CSRF 공격 방어
