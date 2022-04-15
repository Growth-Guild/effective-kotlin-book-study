# Item 18. 코딩 컨벤션을 지켜라

- - -

* [Coding conventions](https://kotlinlang.org/docs/coding-conventions.html) 에서 볼 수 있는 것처럼 코틀린은 잘 정리된 코딩 컨벤션을 가지고 있다.
* 컨벤션을 잘 지키면 다음과 같은 장점이 있다.
  * 다양한 프로젝트를 접해도 쉽게 이해할 수 있다.
  * 다른 외부 개발자도 프로젝트의 코드를 쉽게 이해할 수 있다.
  * 다른 개발자도 코드의 작동 방식을 쉽게 추측할 수 있다.
  * 코드를 병합하고, 한 프로젝트의 코드 일부를 다른 코드로 이동하는 것이 쉽다.

- - -

* 컨벤션을 지킬 때 도움이 되는 두 가지 도구
  * IntelliJ Formatter: 공식 코딩 컨벤션 스타일에 맞춰서 코드를 변경해준다.
  * ktlint: 많이 사용되는 코드를 분석하고 컨벤션 위반을 알려주는 린터(linter)이다.