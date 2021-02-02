# 카카오페이 뿌리기 API

## 요구 사항

* 뿌리기, 받기, 조회 기능을 수행하는 REST API 를 구현합니다.
  * 요청한 사용자의 식별값은 숫자 형태이며 "X-USER-ID" 라는 HTTP Header로 전달됩니다.
  * 요청한 사용자가 속한 대화방의 식별값은 문자 형태이며 "X-ROOM-ID" 라는 HTTP Header로 전달됩니다.
  * 모든 사용자는 뿌리기에 충분한 잔액을 보유하고 있다고 가정하여 별도로 잔액에 관련된 체크는 하지 않습니다.
* 작성하신 어플리케이션이 다수의 서버에 다수의 인스턴스로 동작하더라도 기능에 문제가 없도록 설계되어야 합니다.
* 각 기능 및 제약사항에 대한 단위테스트를 반드시 작성합니다.

## 상세 구현 요건 및 제약사항

#### 1. 뿌리기 API

* 다음 조건을 만족하는 뿌리기 API를 만들어 주세요.
  * 뿌릴 금액, 뿌릴 인원을 요청값으로 받습니다.
  * 뿌리기 요청건에 대한 고유 token을 발급하고 응답값으로 내려줍니다.
  * 뿌릴 금액을 인원수에 맞게 분배하여 저장합니다. (분배 로직은 자유롭게 구현해 주세요.)
  * token은 3자리 문자열로 구성되며 예측이 불가능해야 합니다.

#### 2. 받기 API

* 다음 조건을 만족하는 받기 API를 만들어 주세요.
  * 뿌리기 시 발급된 token을 요청값으로 받습니다.
  * token에 해당하는 뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나를
    API를 호출한 사용자에게 할당하고, 그 금액을 응답값으로 내려줍니다.
  * 뿌리기 당 한 사용자는 한번만 받을 수 있습니다.
  * 자신이 뿌리기한 건은 자신이 받을 수 없습니다.
  * 뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수
    있습니다.
  * 뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기 실패 응답이 내려가야 합니다.

#### 3. 조회 API

* 다음 조건을 만족하는 조회 API를 만들어 주세요.
  * 뿌리기 시 발급된 token을 요청값으로 받습니다.
  * token에 해당하는 뿌리기 건의 현재 상태를 응답값으로 내려줍니다. 현재
    상태는 다음의 정보를 포함합니다.
  * 뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보 ([받은 금액, 받은
    사용자 아이디] 리스트)
  * 뿌린 사람 자신만 조회를 할 수 있습니다. 다른사람의 뿌리기건이나 유효하지
    않은 token에 대해서는 조회 실패 응답이 내려가야 합니다.
  * 뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.

## 설계 및 구현

#### 	1.DB Modeling

  * sprinkle / sprinkleGet 테이블로 구성 
  * AWS MariaDB 연결 되어 있음 H2 DB 사용 원할시 application.properties 에서 주석 처리 
>![image](https://user-images.githubusercontent.com/42058025/106576641-be6d8580-6580-11eb-8600-32334adbc02c.png)

#### 	2. Swagger

  * Swagger로 API 테스트 및 문서화
  * '/'진입시 BaseController를 통해 redirect
>![image](https://user-images.githubusercontent.com/42058025/106577086-494e8000-6581-11eb-97e7-49bae717d120.png)

#### 	3.Test

| 구분          | 테스트명                                                     | 경로                                                         |
| ------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 검증          | 헤더 검증 (회원 아이디 누락)                                 | ~.controller.ValidateTest#HeaderValidate001                  |
|               | 헤더 검증 (대화방 아이디 누락)                               | ~.controller.ValidateTest#HeaderValidate002                  |
|               | 헤더 값 검증                                                 | ~.controller.ValidateTest#HeaderValidate003                  |
|               | 요청 값 검증                                                 | ~.controller.ValidateTest#paramValidate001                   |
| 도메인        | 뿌리기 저장 테스트                                           | ~.SprinkleRepositoryTest#sprinkleSaveTest    |
|               | 뿌린금액 리스트 저장 테스트                                  | ~.SprinkleGetRepositoryTest#sprinkleGetSaveTest |
| 컨트롤러      | 뿌리기 API                                                   | ~.controller.SprinkleApiControllerTest#saveSprinkle          |
|               | 받기 API                                                     | ~.controller.SprinkleApiControllerTest#getSprinkle           |
|               | 조회 API                                                     | ~.controller.SprinkleApiControllerTest#selectSprinkle        |
| 뿌리기 서비스 | 금액, 인원을 요청값으로 받고 금액은 인원수 이상이어야 합니다. | ~.service.SprinkleServiceTest#sprinkleTest001                |
|               | 뿌리기 요청건에 대한 고유 token을 발급하고 응답값으로 내려줍니다. | ~.service.SprinkleServiceTest#sprinkleTest002                |
|               | 뿌릴 금액을 인원수에 맞게 분배하여 저장합니다.               | ~.service.SprinkleServiceTest#sprinkleTest003                |
| 받기 서비스   | 할당되지 않은 분배건 하나를 API를 호출한 사용자에게 할당하고, 그 금액을 응답값으로 내려줍니다. | ~.service.SprinkleGetServiceTest#sprinkleGetTest001          |
|               | 뿌리기 당 한 사용자는 한번만 받을 수 있습니다.               | ~.service.SprinkleGetServiceTest#sprinkleGetTest002          |
|               | 자신이 뿌리기한 건은 자신이 받을 수 없습니다.                | ~.service.SprinkleGetServiceTest#sprinkleGetTest003          |
|               | 대화방에 속한 사용자만이 받을 수 있습니다.                   | ~.service.SprinkleGetServiceTest#sprinkleGetTest004          |
|               | 뿌린지 10분이 지난 요청에 대해서는 받기 실패 응답이 내려가야 합니다. | ~.service.SprinkleGetServiceTest#sprinkleGetTest005          |
|               | 받을수 있는 건이 없으면 실패 응답                            | ~.service.SprinkleGetServiceTest#sprinkleGetTest006          |
| 조회 서비스   | 조회를 요청하고 token에 해당하는 뿌리기 건의 현재 상태를 응답값으로 내려줍니다. | ~.service.SprinkleServiceTest#sprinkleTest004                |
|               | 뿌린 사람 자신만 조회를 할 수 있습니다.                      | ~.service.SprinkleServiceTest#sprinkleTest005                |
|               | 뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.               | ~.service.SprinkleServiceTest#sprinkleTest006                |

#### 	4.Test Result

>![image](https://user-images.githubusercontent.com/42058025/106586391-be26b780-658b-11eb-8132-5360de325b0c.png)

## API Documents

#### 	● Requet Header

| Name      | Required | Description   |
| --------- | -------- | ------------- |
| X-USER-ID | O        | 유저 아이디   |
| X-ROOM-ID | O        | 대화방 아이디 |

#### 	1.뿌리기 API

​		● Requet URL	(`POST` /v1/sprinkle)

```
http://localhost:8080/v1/sprinkle
```

​		● Requet Parameter 

| Parameter Name | Type   | Description |
| -------------- | ------ | ----------- |
| amount         | Number | 뿌릴 금액   |
| totalCount     | Number | 뿌릴 갯수   |

​		● Request Exemple

```
POST /v1/sprinkle HTTP/1.1
Content-Type: application/json;charset=UTF-8
X-USER-ID: 11111
X-ROOM-ID: abcdef
Accept: application/json
Content-Length: 27
Host: 15.164.70.143:8080

{
  "amount": 10000,
  "totalCount": 10
}
```

​		● Response Parameter

| Parameter Name | Type   | Description       |
| :------------- | ------ | ----------------- |
| code           | String | 응답 코드         |
| message        | String | 응답 메시지       |
| data           | String | 고유 token 문자열 |

​		● Response Exemple

```
{
  "code": "SPRINKLE_SUCCESS",
  "message": "뿌리기가 완료 되었습니다.",
  "data": "tyB"
}
```



#### 	2.받기 API

​		● Requet URL	(`PUT` /v1/sprinkle/{token})

```
http://localhost:8080/v1/sprinkle/tyB
```

​		● Requet Parameter 

| Path Parameter Name | Type   | Description  |
| ------------------- | ------ | ------------ |
| token               | String | 고유 token값 |

​		● Request Exemple

```
PUT /v1/sprinkle/tyB HTTP/1.1
Content-Type: application/json;charset=UTF-8
X-USER-ID: 22222
X-ROOM-ID: abcdef
Accept: application/json

```

​		● Response Parameter

| Parameter Name | Type   | Description |
| :------------- | ------ | ----------- |
| code           | String | 응답 코드   |
| message        | String | 응답 메시지 |
| data           | Number | 받은 금액   |

​		● Response Exemple

```
{
  "code": "GET_SUCCESS",
  "message": "받기가 완료 되었습니다.",
  "data": 5278
}
```



#### 	3.조회API

​		● Requet URL	(`GET` /v1/sprinkle/{token})

```
http://localhost:8080/v1/sprinkle/tyB
```

​		● Requet Parameter 

| Path Parameter Name | Type   | Description  |
| ------------------- | ------ | ------------ |
| token               | String | 고유 token값 |

​		● Request Exemple

```
GET /v1/sprinkle/tyB HTTP/1.1
Content-Type: application/json;charset=UTF-8
X-USER-ID: 11111
X-ROOM-ID: abcdef
Accept: application/json

```

​		● Response Parameter

| Parameter Name           | Type   | Description    |
| :----------------------- | ------ | -------------- |
| code                     | String | 응답 코드      |
| message                  | String | 응답 메시지    |
| data                     | Object | 조회 응답내역  |
| data.sprinkleAt          | String | 뿌린 일시      |
| data.amount              | Number | 뿌린 금액      |
| data.gottenAmount        | Number | 전체 받은 금액 |
| data.gottenList[].amount | Number | 받은 금액      |
| data.gottenList[].userId | Number | 받은 사용자 ID |

​		● Response Exemple

```
{
  "code": "SUCCESS",
  "message": "조회 완료 되었습니다.",
  "data": {
    "sprinkleAt": "2021-02-02T20:16:02",
    "amount": 10000,
    "gottenAmount": 5278,
    "gottenList": [
      {
        "amount": 5278,
        "userId": 22222
      }
    ]
  }
}
```



## 핵심 문제해결 전략



#### 토큰생성의 문제

* 3자리의 짧은 문자열로 구성되어 있어 중복 생성의 확률이 높음 
* 토큰의 생명주기 (조회 기간) 가 7일 이므로 7일 이후에 생성되었던 토큰도 중복으로 사용 가능 하게 개발
* 만약 토큰을 더이상 생성 할수 없는 경우 무한루프 발생 -> 런타임 익셉션 처리



#### 뿌릴 금액과 갯수의 검증

* 요청값이 1원 이상 / 1개 이상인지 검증

* 뿌릴 금액이 뿌릴 갯수보다 적을 경우 분배 할수 없으므로 예외처리 

  

#### 받기 호출시 동시성 문제

* 다수의 서버에 다수의 인스턴스로 동작할경우 받기 호출시 데드락이 발생할 여지가 있음
* JPA 의 @Version을 사용하여 낙관적 잠금으로 어플리케이션 레벨 락을 만듬 
* 트래픽이 높아질 경우 비관적락이나 2차캐시 적용을 염두해야함
* 이외에도 Redis / Kafka 등 메시징 시스템을 사용해 순차 처리 및 동시성 제어를 해야함

