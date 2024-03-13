# Board-Server

### 기획
> 커뮤니티 사이트의 익명 게시판을 구현함으로서 자유롭게 소통 및 정보 공유 사이트를 목표로 구현

### 목적
> Spring-MVC 패턴 및 SpringBoot, JPA 기능 습득 및 활용이 목적이므로 디자인은 최대한 단순하게 사용 또는 Rest API로 사용 예정
> 대용량 트래픽을 고려한 어플리케이션 개발 (초당 1000 tps 이상의 게시글 검색 API)

### 개발환경
> Java 17, SpringBoot v3.1.5, JPA, QueryDSL, JUnit5, MySQL, Redis, Docker, AWS EC2, Locust

### 프로그램 주요 기능
- 회원
  - 가입, 탈퇴
  - 아이디, 닉네임, 이메일 중복체크
  - 비밀번호 암호화
  - 로그인, 로그아웃
- 게시판
    - 게시글 관리
      - 게시글 추가, 삭제, 수정, 조회
      - 추천, 비추천 기능
    - 게시글 검색 기능
      - 게시글 제목, 닉네임을 통해 검색
  - 댓글 작성 기능
      - 댓글 작성, 대댓글 작성, 조회, 수정, 삭제