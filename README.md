# 캘린더형 일정관리 사이트

### [http://todo-develop-ing.site](http://todo.develop-ing.site)
### 개발 기간 : 2020.09 ~
### 규&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 모 : 개인
### 사용 기술 :
#### - java8, SpringBoot 2.3.12
#### - javascript, html, css, thymeleaf
#### - MySQL5.6
#### - Ubuntu, docker, nginx
<br/><br/>

---
### 2021-11-08
1. 계층별 패키지 구조를 기능별 구조로 변경
<pre>
변경 전                         변경 후
todo                            todo
├── TodoApplication.java        ├── TodoApplication.java
├── configuration               ├── calendar
│   └──                         │   └── 
├── controller                  ├── configuration
│   └──                         │   └── 
├── dao                         ├── member
│   └──                         │   └── 
├── security                    ├── security
│   └──                         │   └── 
├── service                     ├── todo
│   └──                         │   └── 
└── vo                          └── utils
    └──                             └── 
</pre>
2. vo와 dto 분리
3. DI 필드 주입에서 생성자 주입으로 변경
4. 날짜, 시간 표현을 위한 객체를 java.util 하위 클래스에서 java.time 하위 클래스로 변경