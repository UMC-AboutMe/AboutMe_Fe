<h1 align="center">AboutMe Frontend</h1>
<div align="center"></div>

![image](https://github.com/UMC-AboutMe/AboutMe_Fe/assets/62244340/cf69937d-532a-429e-94d8-77f4e797e96e)

<div align="center">
<img src="https://img.shields.io/badge/Android%20Studio-3DDC84?style=flat-square&logo=Android%20Studio&logoColor=white" />
<img src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=Kotlin&logoColor=white" />
</div>

<br>

## 프로젝트 소개

*아웃사이드와 인사이드 기능이란?*

: *아웃사이드와 인사이드 기능은 AboutMe의 핵심 기능이자 컨셉으로, 나의 겉모습은 아웃사이드, 속마음은 인사이드라고 생각하면 됩니다. 여러 상황에서 사람들을 만나며 명함을 공유할 수 있겠지만, 내 기분, 일정, 생각과 같은 민감한 정보는 친한 친구들에게만 공유하는 것처럼, 앱 내에서 전환해가며 사용할 수 있는 기능입니다.* 

**<아웃사이드 기능>**

1. 마이프로필 제작
- 앞면 : 자신을 대표하는 사진과 이름, 나이 등 간단한 정보를 손쉽게 추가할 수 있다.
- 뒷면 : 기존의 직장 및 직급 정보만 담긴 명함 대신, 자신의 취미, SNS 주소, MBTI 등과 같은 항목들을 원하는 대로 추가하여 디지털 프로필을 제작할 수 있다.
2. 멀티프로필 
- 상황에 따라 다른 프로필을 사용할 수 있는 멀티프로필 기능을 제공한다.(예: 데일리 프로필/비지니스 프로필)
3. 프로필 보관함
- 항목을 카테고리 별로 구분하여 다른 사람들의 디지털 명함을 손쉽게 보관할 수 있다. 빠른 검색을 위해 검색 조건을 설정할 수 있다.

**<인사이드 기능>**

1. 마이 스페이스 
- 캐릭터 커스터마이징을 통해 말풍선으로 자신의 기분, 헤드폰으로 요즘 듣는 노래, 시계로 오늘의 일정 등을 다양하게 표현할 수 있다.
2. 아지트
- 아웃사이드의 프로필 보관함과 같은 기능을 담당한다.
3. 상점
- 캐릭터 커스터마이징에 필요한 악세사리를 구매할 수 있는 상점이 있다. **→ BM과 연결되게 된다.**

<br>

## 팀원 구성

<div align="center">

| **송혜음** | **정승원** | **차현정** |
| :------: |  :------: | :------: |
| [<img src="https://avatars.githubusercontent.com/u/118244028?v=4" height=150 width=150> <br/> @hyeumm](https://github.com/hyeumm) | [<img src="https://avatars.githubusercontent.com/u/62244340?v=4" height=150 width=150> <br/> @tristanjung1006](https://github.com/tristanjung1006) | [<img src="https://avatars.githubusercontent.com/u/138744022?v=4" height=150 width=150> <br/> @jeong724](https://github.com/jeong724) |

</div>

<br>

## 프로젝트 구조

## 아키텍쳐

## commit 규칙

[커밋태그] : 내용 #이슈번호

예시) add : login 파일 추가 #1

| 태그이름   | 내용                                                                      |
| ---------- | ------------------------------------------------------------------------- |
| `add`     | 새로운 기능 (파일 추가도 포함)을 추가할 경우                              |
| `feat`     | 버그를 고친 경우                                                          |
| `fix`   | 코드 수정을 한 경우                                                       |
| `docs`  | 급하게 치명적인 버그를 고쳐야하는 경우                                    |
| `comment`    | 코드 포맷 변경, 세미 콜론 누락, 코드 수정이 없는 경우                     |
| `test` | 프로덕션 코드 리팩토링                                                    |
| `merge`  | 필요한 주석 추가 및 변경                                                  |
| `refactor`     | 문서를 수정한 경우                                                        |
| `style`     | 테스트 추가, 테스트 리팩토링(프로덕션 코드 변경 X)                        |
| `remove`    | 빌드 태스트 업데이트, 패키지 매니저를 설정하는 경우(프로덕션 코드 변경 X) |
| `setting`   | 파일 혹은 폴더명을 수정하거나 옮기는 작업만인 경우                        |

## 머지하는 방법

깃허브에 브랜치 푸시하면 compare&...뜰거임

그럼 그거 초록색 버튼 누르고 pr남기고 머지하기

그 다음

안드로이드 스튜디오 돌아와 터미널

git checkout master

git merge 브랜치

git pull

master 브랜치로 병합되었음을 확인할 수 있음
