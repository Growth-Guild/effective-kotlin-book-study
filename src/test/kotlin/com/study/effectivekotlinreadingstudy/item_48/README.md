# Item 48. 더 이상 사용하지 않는 객체의 레퍼런스를 제거하라

- - -

* 자바는 gc가 객체 해제와 관련된 모든 작업을 해 주지만 메모리 관리를 완전히 무시해 버리면, 메모리 누수가 발생해서 상황에 따라 OutOfMemoeryError가 발생할 수 있다.
* '더 이상 사용하지 않는 객체의 레퍼런스를 유지하면 안 된다'라는 규칙 정도는 지켜주는 것이 좋다.
* 객체에 대한 참조를 companion으로 유지해 버리면, gc는 해당 객체에 대한 메모리 해제를 할 수 없다.
  * 리소스를 정적으로 유지하지 않는 것이 가장 좋다.
* '메모리와 성능'뿐만 아니라 '가독성과 확장성'을 항상 고려해야 한다.
* 메모리 관련 문제를 피하는 방법은 변수의 스코프를 최소화하는 것이다.
