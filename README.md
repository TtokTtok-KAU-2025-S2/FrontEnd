2025년 2학기 산학프로젝트 똑똑 - FE Repository

### 커밋 컨벤션

### tag: description

```
feat: add main page router
```

- init: 새 프로젝트 생성
- feat: 새로운 기능 추가
- fix: 수정 사항 발생 시, 버그 수정
- comment: 필요한 주석 추가 및 변경
- docs: 문서 수정
- style: 코드 formatting, 세미콜론 누락, 코드 자체의 변경이 없는 경우
- refactor: 코드 리팩토링
- test: 테스트 코드 추가
- chore: 패키지 매니저 수정, 그 외 기타 수정(ex. .gitignore)
- design: CSS 등 사용자 UI 디자인 변경
- rename: 파일 또는 폴더 명을 수정하거나 옮기는 작업
- remove: 파일을 삭제하는 작업만 수행한 경우
- build: 빌드 관련 파일 수정
- temp: 기능 완성 전 임시적으로 repository에 push 되어야 하는 경우

### 브랜치 컨벤션

### **main - dev -  {기능}**

- 개인 작업은 꼭 feature 브랜치에서 하기
- feat/login-page
    - `login-page`
    - feature 하위 개발 중인 기능을 식별할 수 있도록 branch 이름 작성
- 모든 작업 시작 전 **develop에서 Pull을 받은 후** → feature 브랜치에서 작업 시작
- 개인 작업 마치면 feature브랜치로 PR 날리기
    - **PR template 활용하여 상세히 작성**
    - branch merge 후 삭제
- 프로젝트 진행 상황에 맞게 주기적으로 main으로 merge 하기
**<main 브랜치는 건드리지 말기 ‼️‼️>**

### Issue / PR title 컨벤션

### 1. ISSUE 템플릿 작성

### 2. 해당 ISSUE branch로 이동 후 작성

### 2. PR 템플릿 작성

### 3. 팀원 전부 리뷰 완료 후 PR merge
