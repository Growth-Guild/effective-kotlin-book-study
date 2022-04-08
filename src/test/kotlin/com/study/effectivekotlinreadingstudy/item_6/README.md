# Item 6. 사용자 정의 오류보다는 표준 오류를 사용하라
- - -

* require, check, assert 함수를 이용하면 대부분의 코틀린 오류를 처리할 수 있지만 이외에도 예측하지 못한 상황을 나타내야 하는 경우가 있다.
  * 예를 들어 JSON 파싱과 관련된 에러가 발생한다면 JSONParsingException 등을 발생시키는 동작
* 표준 라이브러리에는 이렇게 다양한 상황을 나타낼 수 있는 예외가 없지만 최대한 표준 라이브러리의 오류를 사용하는 것이 좋다.
* 표준 라이브러리는 많은 개발자가 알고 있으므로, 이를 재사용하는 것이 좋다.
* 일반적으로 사용되는 예외를 몇 가지 정리하면 아래와 같다.
  * IllegalArgumentException과 IllegalStateException: require와 check를 사용해 throw 할 수 있는 예외
  * IndexOutOfBoundException: 인덱스 파라미터의 값이 범위를 벗어났다는 의미를 가진 예외
  * ConcurrentModificationException: 동시 수정을 금지했는데, 발생해 버렸다는 것을 의미
  * UnsupportedOperationException: 사용자가 사용하려고 했던 메서드가 현재 객체에서는 사용할 수 없다는 것을 의미
  * NoSuchElementException: 사용자가 사용하려고 했던 요소가 존재하지 않음을 의미
