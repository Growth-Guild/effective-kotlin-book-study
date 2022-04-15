# Item 28. API 안정성을 확인하라

- - -

* 안정적이고 최대한 표준적인 API를 선호하는 이유
  * API가 변경되고, 개발자가 이를 업데이트 했다면, 여러 코드를 수동으로 업데이트 해야한다.
  * 사용자가 새로운 API를 배워야 한다. 

### 시멘틱 버저닝(Semantic Versioning)
* 버전 번호를 세 번호(MAJOR, MINOR, PATCH)로 나누어서 구성한다.
  * MAJOR 버전: 호환되지 않는 수준의 API 변경
  * MINOR 버전: 이전 변경과 호환되는 기능을 추가
  * PATCH 버전: 간단한 버그 수정
* MAJOR를 증가시킬 때는 MINOR와 PATCH를 0으로 돌린다.
* MINOR를 증가시킬 때는 PATCH를 0으로 돌린다.