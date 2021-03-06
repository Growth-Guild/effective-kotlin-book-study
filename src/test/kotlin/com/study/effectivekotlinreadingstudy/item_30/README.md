# Item 30. 요소의 가시성을 최소화하라

- - -

* 작은 인터페이스는 배우기 쉽고 유지하기 쉽다.
* 기능이 많은 클래스보다 적은 클래스를 이해하는 것이 쉽다. 또한 유지보수 하기도 쉽다.
* 변경을 가할 때는 기존의 것을 숨기는 것보다 새로운 것을 노출하는 것이 쉽다.
* 클래스의 상태를 나타내는 프로퍼티를 외부에서 변경할 수 있다면, 클래스는 자신의 상태를 보장할 수 없다.
  * 클래스가 만족해야 하는 상태에 대한 규약 등이 있을 수 있는데, 이를 모르는 사람은 클래스의 상태를 마음대로 변경할 수 있으므로, 클래스의 불변성이 무너질 가능성이 있다.
* 일반적으로 코틀린에서는 구체 접근자의 가시성을 제한해서 모든 프로퍼티를 캡슐화하는 것이 좋다.
* 서로서로 의존하는 프로퍼티가 있을 때는 객체 상태를 보호하는 것이 더 중요해진다.

### 가시성 한정자 사용하기
* 내부적인 변경 없이 작은 인터페이스를 유지하고 싶다면, 가시성(visibility)을 제한하면 된다.
* 기본적으로 클래스와 요소를 외부에 노출할 필요가 없다면, 가시성을 제한해서 외부에서 접근할 수 없게 만드는 것이 좋다.

#### 클래스의 가시성 한정자
* public(디폴트): 어디에서나 볼 수 있다.
* private: 클래스 내부에서만 볼 수 있다.
* protected: 클래스와 서브클래스 내부에서만 볼 수 있다.
* internal: 모듈 내부에서만 볼 수 있다.

#### 톱레벨 요소의 가시성 한정자
* public(디폴트): 어디에서나 볼 수 있다.
* private: 같츤 파일 내부에서만 볼 수 있다.
* internal: 모듈 내부에서만 볼 수 있다.

- - -

* 코틀린에서 모듈이란 함께 컴파일되는 코틀린 소스를 의미한다. 따라서 다음을 의미한다.
  * 그레이들(Gradle) 소스 세트
  * 메이븐(Maven) 프로젝트
  * 인텔리제이(IntelliJ) IDEA 모듈
  * 앤트(Ant) 태스크 한 번으로 컴파일되는 파일 세트

- - -

* 한 가지 큰 제한은, API를 상속할 때 오버라이드해서 가시성을 제한할 수는 없다는 점이다.
  * 서브클래스가 슈퍼클래스로도 사용될 수 있기 때문이다.
  * 이는 상속보다 컴포지션을 선호하는 대표적인 이유다.
