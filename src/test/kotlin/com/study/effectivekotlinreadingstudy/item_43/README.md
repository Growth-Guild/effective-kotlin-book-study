# Item 43. API의 필수적이지 않는 부분을 확장 함수로 추출하라.

- - -

* 클래스의 메서드를 정의할 때, 메서드를 멤버로 정의할 것인지 아니면 확장 함수로 정의할 것인지 결정해야 한다.

### 멤버 함수와 확장 함수의 차이점
* 확장은 따로 가져와서 사용해야 한다.
  * 그래서 일반적으로 확장은 다른 패키지에 존재한다.
  * 확장은 우리가 직접 멤버를 추가할 수 없는 경우, 데이터와 행위를 분리하도록 설계된 프로젝트에서 사용된다.
* 임포트해서 사용하는 특징 덕분에 확장은 같은 타입에 같은 이름으로 여러 개 만들 수도 있다.
  * 따라서 여러 라이브러리에서 여러 메서드를 받을 수도 있고, 충돌이 발생하지도 않는다는 장점이 생긴다.
  * 같은 이름으로 다른 동작을 하는 확장이 있다면 위험할 수도 있다.
  * 이러한 위험 가능성이 있다면 그냥 멤버 함수로 사용하는 것이 낫다.
* 또 다른 차이점은 확장은 가상(virtual)이 아니라는 점이다. 즉, 파생 클래스에서 오버라이드 할 수 없다.
  * 확장 함수는 컴파일 시점에 정적으로 선택된다.
  * 상속을 목적으로 설계된 요소는 확장 함수로 만들면 안 된다.
  * 이러한 차이는 확장 함수가 '첫 번째 아규먼트로 리시버가 들어가는 일반 함수'로 컴파일되기 때문이다.
* 확장 함수는 클래스가 아닌 타입에 정의할 수 있다.
  * nullable 또는 구체적인 제네릭 타입에도 확장 함수를 정의할 수 있다.
* 확장은 클래스 레퍼런스에서 멤버로 표시되지 않는다.
  * 확장 함수는 애너테이션 프로세서가 따로 처리하지 않는다.
  * 따라서 필수적이지 않은 요소를 확장 함수로 추출하면, 애너테이션 프로세스로부터 숨겨진다.
  * 이는 확장 함수가 클래스 내부에 있는 것은 아니기 때문이다.