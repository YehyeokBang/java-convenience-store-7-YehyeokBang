# java-convenience-store-precourse

## 프로그램 안내

구매자의 할인 혜택과 재고 상황을 고려하여 최종 결제 금액을 계산하고 안내하는 결제 시스템입니다.

- 사용자가 입력한 상품의 가격과 수량을 기반으로 최종 결제 금액을 계산합니다.
- 총구매액은 상품별 가격과 수량을 곱하여 계산하며, 프로모션 및 멤버십 할인 정책을 반영하여 최종 결제 금액을 산출합니다.
- 구매 내역과 산출한 금액 정보를 영수증으로 출력합니다. 
- 영수증 출력 후 추가 구매를 진행할지 또는 종료할지를 선택할 수 있습니다. 
- 사용자가 잘못된 값을 입력할 경우 "[ERROR]"로 시작하는 에러 메시지를 출력 후 그 부분부터 입력을 다시 받습니다.
- [아래](#실행-예시)에서 실행 예시를 확인할 수 있습니다.

## 구현 기능 목록

- 환영 인사 메시지 출력
  - [X] `안녕하세요. W편의점입니다.` 문구 출력
- 상품 재고 출력
  - [X] `현재 보유하고 있는 상품입니다.` 문구 출력
  - [X] `- 상품명 가격 수량 프로모션(선택)` 형식으로 보유하고 있는 상품 재고 출력
    - 재고가 0개라면 `재고 없음`을 출력한다. 
    - `products.md`과 `promotions.md` 파일을 이용한다.
- 구매할 상품 확인
  - [X] `구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])` 문구 출력
  - [X] 상품 및 수량 입력
    - 상품명, 수량은 하이픈(`-`)으로 구분한다.
    - 개별 상품은 대괄호(`[]`)로 묶어 쉼표(`,`)로 구분한다.
- 프로모션 적용 여부 확인
  - 프로모션 적용 수량보다 적게 가져온 경우
    - [X] `현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)` 문구 출력
      - (예시: 2+1 상품을 2개만 가져온 경우 1개를 무료로 받을 수 있다고 안내합니다.)
    - [X] 추가 여부 입력
      - Y: 증정 받을 수 있는 상품을 추가한다.
      - N: 증정 받을 수 있는 상품을 추가하지 않는다.
  - 프로모션 적용 상품 재고가 부족한 경우
    - [X] `현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)` 문구 출력
      - (예시: 2+1 프로모션 상품이 5개, 일반 상품이 10개 있을 때, 7개를 구매한다면 4개는 프로모션이 적용되지 않습니다.)
    - [X] 구매 여부 입력
      - Y: 일부 수량에 대해 정가로 결제한다.
      - N: 정가로 결제해야하는 수량만큼 제외한 후 결제를 진행한다.
- 멤버십 할인 적용 여부 확인
  - [X] `멤버십 할인을 받으시겠습니까? (Y/N)` 문구 출력
  - [X] 적용 여부 입력
    - Y: 멤버십 할인을 적용한다.
    - N: 멤버십 할인을 적용하지 않는다.
  - [X] 멤버십 적용 금액 확인
    - 프로모션 미적용 금액의 30%를 할인한다.
    - 멤버십 할인 최대 적용 금액 확인 (최대 8,000원)
- 영수증 출력
  - [X] 구매 상품 내역 출력
  - [X] 증정 상품 내역 출력
  - [X] 금액 정보 출력
- 추가 구매 여부 확인
  - [X] `감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)` 문구 출력
  - [X] 추가 구매 여부 입력
    - Y: 재고가 업데이트된 상품 목록을 확인 후 추가로 구매를 진행한다. 
    - N: 구매를 종료한다.
- [X] 사용자가 잘못된 값을 입력한 경우 오류 메시지 출력 후 그 부분부터 재입력

## 예외 상황 목록

- 사용자가 잘못된 값을 입력한 경우
  - `[상품명-수량]` 또는 `[상품명-수량],[상품명-수량],...`형식으로 입력하지 않은 경우
    - 예외 상황을 줄이기 위해 좌우 및 사이 공백은 무시합니다.
  - 상품명을 입력하지 않은 경우
  - 존재하지 않는 상품명인 경우
  - 수량을 입력하지 않은 경우
  - 수량이 1보다 작은 경우
  - 재고 수량보다 많이 구매하려는 경우
  - 동일한 이름을 가진 상품을 나눠 구매하는 경우 (예시: `[콜라-1],[콜라-2]`)
  - 특정 안내 메시지에 `Y/N`(소문자 가능) 외의 입력을 한 경우 (예시: `무료로 더 받을 수 있습니다. O`)

- 데이터 파일을 읽을 수 없는 경우
  - 상품 데이터가 잘못된 경우 (예시: `수량이 음수인 경우`)
  - 프로모션 데이터가 잘못된 경우 (예시: `시작 날짜가 종료 날짜보다 미래인 경우`)

## 예외 상황 메시지

사용자가 잘못된 값을 입력했을 때, `"[ERROR]"`로 시작하는 오류 메시지와 함께 상황에 맞는 안내를 출력한다.
- 구매할 상품과 수량 형식이 올바르지 않은 경우: `[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.` 
- 존재하지 않는 상품을 입력한 경우: `[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.` 
- 구매 수량이 재고 수량을 초과한 경우: `[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.` 
- 기타 잘못된 입력의 경우: `[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`

## 기타 및 고민

- 상품 목록 파일에 프로모션 상품만 존재할 경우 프로모션이 없는 상품도 소개합니다. 
  - (예시: 파일에 `오렌지주스 1,800원 9개 MD추천상품`만 있는 경우 `오렌지 주스 1,800원 재고 없음` 항목을 추가하여 관리합니다.)
- 예외를 최대한 줄이려고 했습니다.
  - 입력값에 큰 영향을 미치지 않고, 실수지만 명확히 값을 판단할 수 있는 경우 그대로 사용하게 하여 사용성을 높이려고 했습니다. 
  - 콘솔 입력 특성상 공백이 추가되는 빈도가 높다고 생각했습니다. 필요한 입력값은 모두 사이에 공백이 존재하지 않기 때문에 `replaceAll()`을 사용하여 입력 내부의 공백을 없앤 후 사용합니다.
  - Y/N, y/n 모두 사용 가능합니다.
- 편의점에서 상품을 진열하는 것을 편의점 워크인 냉장고로 생각하며 구현해봤습니다. 각각의 라인은 Deque 자료구조로 구성되어 선입선출이 가능하게 구현했습니다. 그러나 객체간 의존성이 너무 강한 것 같습니다.
- 멤버십 할인
  - 프로모션 미적용 금액을 프로모션이 적용되는 상품을 모두 제외한 금액으로 이해했습니다. 
    - (예시: 2+1 상품 3개를 구매하는 경우 3개 모두 프로모션이 적용되기 때문에 멤버십 할인이 적용될 금액이 없어서 0원 할인됩니다.)

## 실행 예시

```text
안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 10개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 5개
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[콜라-3],[에너지바-5]

멤버십 할인을 받으시겠습니까? (Y/N)
Y 

===========W 편의점=============
상품명		수량	금액
콜라		3 	3,000
에너지바 		5 	10,000
===========증	정=============
콜라		1
==============================
총구매액		8	13,000
행사할인			-1,000
멤버십할인			-3,000
내실돈			 9,000

감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
Y

안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 7개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 재고 없음
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[콜라-10]

현재 콜라 4개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
Y

멤버십 할인을 받으시겠습니까? (Y/N)
N

===========W 편의점=============
상품명		수량	금액
콜라		10 	10,000
===========증	정=============
콜라		2
==============================
총구매액		10	10,000
행사할인			-2,000
멤버십할인			-0
내실돈			 8,000

감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
Y

안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 재고 없음 탄산2+1
- 콜라 1,000원 7개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 재고 없음
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[오렌지주스-1]

현재 오렌지주스은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
Y

멤버십 할인을 받으시겠습니까? (Y/N)
Y

===========W 편의점=============
상품명		수량	금액
오렌지주스		2 	3,600
===========증	정=============
오렌지주스		1
==============================
총구매액		2	3,600
행사할인			-1,800
멤버십할인			-0
내실돈			 1,800

감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
N
```

## 커밋 메시지 컨벤션

커밋 단위는 앞에서 정리한 구현할 기능 목록 단위로 진행됩니다.  
[AngularJS Git Commit Message Conventions](https://gist.github.com/stephenparish/9941e89d80e2bc58a153)를 참고하여 커밋 컨벤션을
구성하였습니다.

```text
<작업 유형>: <주제>
<공백>
<본문>
```

## 작업 유형

커밋의 유형을 표시합니다.

- **feat**: 새로운 기능 추가
- **fix**: 버그 수정
- **docs**: 문서 관련 변경 사항
- **refactor**: 코드 리팩토링 (버그 수정이나 기능 추가가 아닌 코드 구조 변경)
- **test**: 테스트 추가 또는 수정

## 주제

커밋의 주제를 간단히 작성합니다.

- 명령형으로 작성합니다.
- 문장 끝에 마침표를 사용하지 않습니다.
- 예시 : `fix: 재고가 없어도 구매할 수 있던 문제 수정`

## 본문 (선택 사항)

- 변경(및 추가) 이유와 변경된 부분의 자세한 내용을 설명합니다.
- 현재 시제 문장 형태로 작성합니다.
- 이전 동작과의 차이점을 작성합니다.

## 커밋 메시지 예시

```text
docs: 구현할 기능 목록 작성
```

```text
fix: 멤버십 할인의 한도 초과 문제 수정

멤버십 할인의 최대 한도를 넘겨 할인이 적용되던 문제를 해결합니다.
이전에는 제한 없이 멤버십 할인이 적용되었는데, 검증 로직을 추가하여 최대 한도를 넘을 수 없게 수정합니다.
```