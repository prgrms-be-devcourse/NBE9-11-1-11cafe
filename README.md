## ☕️ 로컬 카페 **Grids & Circles**
온라인 웹사이트를 통해 별도의 회원가입 없이 커피 원두 패키지를 판매 서비스 <br>
<br>

## 🔍 Introduction
주어진 요구 사항에 맞추어, Spring Framework 기반으로 카페의 커피 원두 메뉴와 주문을 관리하는 서비스입니다. <br>
CRUD(Create, Read, Update, Delete) 기능을 중심으로 메뉴 데이터를 효율적으로 관리하고, 주문 집계 로직을 구현하는 것을 목표로 합니다. <br>
<br>


## 📕 API 
- 장바구니
<img width="1291" height="305" alt="image" src="https://github.com/user-attachments/assets/24dd9a27-2303-410e-9af2-44ed979e80e6" />

- 상품
<img width="1292" height="154" alt="image" src="https://github.com/user-attachments/assets/82e7100b-e0f3-4034-9060-d8d91309a22d" />

- 주문
<img width="1289" height="104" alt="image" src="https://github.com/user-attachments/assets/6782aeff-45c2-4ee4-865c-e444849d9e19" /> 
<br>
<br>

<!--
## 📹 Demo
<img src="https://github.com/user-attachments/assets/66f02dbe-425c-4998-9e81-759987a114fc" width="1000"/>
-->


## 🛠️ Tech Stack
| Frontend | Backend | Database | API Documentation | Collaboration |
|:--------:|:-------:|:--------:|:-----------------:|:-------------:|
|<img src="https://img.shields.io/badge/React-61DAFB?style=flat-square&logo=React&logoColor=black"/>|<img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=Java&logoColor=white"/><br><img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=SpringBoot&logoColor=white"/><br><img src="https://img.shields.io/badge/Spring%20Web-6DB33F?style=flat-square&logo=Spring&logoColor=white"/><br><img src="https://img.shields.io/badge/JPA-007396?style=flat-square&logo=Java&logoColor=white"/><br><img src="https://img.shields.io/badge/Gradle%20Kotlin%20DSL-02303A?style=flat-square&logo=Gradle&logoColor=white"/>|<img src="https://img.shields.io/badge/H2-09476B?style=flat-square&logo=H2&logoColor=white"/>|<img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=Swagger&logoColor=black"/>|<img src="https://img.shields.io/badge/Git-F05032?style=flat-square&logo=Git&logoColor=white"/><br><img src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=GitHub&logoColor=white"/><br><img src="https://img.shields.io/badge/Notion-000000?style=flat-square&logo=Notion&logoColor=white"/><br><img src="https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=Slack&logoColor=white"/>|

<br>

## 🗂️ Database
<img width="1270" height="705" alt="image" src="https://github.com/user-attachments/assets/2c9a4b02-877b-4001-b645-14073dd96426" /> <br>
<br>

## 💻 Flowchart
<img width="1276" height="607" alt="image" src="https://github.com/user-attachments/assets/b77c24b5-f9cd-46da-904a-8bc1112dd9c2" /> <br>
<br>

## 🤝 Git Convention

### Branch
- `main` : 출시 배포 branch
- `develop` : 개발용 branch
- `feature` : 기능 구현용 branch
- `Issue_종류:내용` : branch 생성

### Issue

| 종류             | 내용                                             |
|----------------| ------------------------------------------------ |
| feat         | 기능 구현                                          |
| fix         | 버그 수정                                           |
| refactor    | 코드 리팩토링                                         |
| test         | 테스트 업무                                        |
| file        | 파일 이동 또는 삭제, 파일명 변경                         |
| docs        | md, yml 등의 문서 작업                               |
| chore       | 이외의 애매하거나 자잘한 수정                            |
| setting     | 빌드 및 패키지 등 프로젝트 설정                           |

<br>

## 📁 Project Structure
#### Backend
```
src
├── main
│   ├── generated
│   ├── java
│   │   └── com
│   │       └── back
│   │           └── _1cafe
│   │               ├── Application.java
│   │               ├── cart
│   │               │   ├── Cart.java
│   │               │   ├── CartController.java
│   │               │   ├── CartDto.java
│   │               │   ├── CartItem.java
│   │               │   ├── CartRepository.java
│   │               │   └── CartService.java
│   │               ├── customer
│   │               │   └── Customer.java
│   │               ├── global
│   │               │   ├── Config.java
│   │               │   ├── initData
│   │               │   │   └── BaseInitData.java
│   │               │   ├── rsData
│   │               │   │   └── RsData.java
│   │               │   ├── springDoc
│   │               │   │   └── SpringDoc.java
│   │               │   └── Util.java
│   │               ├── order
│   │               │   ├── Order.java
│   │               │   ├── OrderBatch.java
│   │               │   ├── OrderController.java
│   │               │   ├── OrderDto.java
│   │               │   ├── OrderItem.java
│   │               │   ├── OrderRepository.java
│   │               │   └── OrderService.java
│   │               └── product
│   │                   ├── Product.java
│   │                   ├── ProductController.java
│   │                   ├── ProductDto.java
│   │                   ├── ProductRepository.java
│   │                   └── ProductService.java
│   └── resources
│       ├── application-dev.yaml
│       ├── application-test.yaml
│       └── application.yaml
└── test
    └── java
        └── com
            └── back
                └── _1cafe
                    └── ApplicationTests.java
```

### Frontend
```
src
├── App.css
├── App.tsx
├── assets
│   └── coffeeicon.png
├── features
│   └── cart
│       ├── cartProductImages.ts
│       ├── cartService.ts
│       ├── cartTypes.ts
│       ├── components
│       │   ├── CartItemRow.tsx
│       │   ├── CartList.tsx
│       │   └── CartTotals.tsx
│       └── hooks
│           └── useCart.ts
├── index.css
├── main.tsx
└── pages
    └── cart
        ├── cartPage.css
        └── CartPage.tsx
```

<br>

## 👨‍💻 Team

| 이름 | 역할 |
|------|--------|
| 윤현정 | 팀장, 주문 API 개발, 리펙토링 |
| 이인희 | 상품 추가 검증 및 테스트 코드 작성, 프론트엔드 화면 구성 및 추가 기능 구현 |
| 최민규 | 상품 추가 API 개발, 발표자료 준비 |
| 최윤서 | 장바구니 API 개발, 프론트엔드 API 연동|
| 최준 | ERD 작성, 주문 API 개발, 발표자료 준비 |
| 한정목 | 장바구니 API 개발, 리펙토링 |
