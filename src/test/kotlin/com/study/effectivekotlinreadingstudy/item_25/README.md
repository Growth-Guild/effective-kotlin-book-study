# Item 25. 공통 모듈을 추출해서 여러 플랫폼에서 재사용하라

- - -

### 풀스택 개발
* 코틀린의 장점 중 하나는 코틀린이 자바스크립트로 컴파일 될 수 있다는 것이다.
* 이미 많은 kotlin/js 라이브러리가 있으며, 코틀린을 활용해 다양한 웹 애플리케이션을 만들 수 있다.
* 즉, 웹 백엔드와 프론트엔드를 모두 코틀린을 ㅗ개발할 수 있다는 것이다.

### 모바일 개발
* 코틀린의 멀티 플랫폼 기능을 활용하면, 로직을 한 번만 구현하고, 안드로이드와 iOS 두 플랫폼에서 이를 재사용할 수 있다.
* 비즈니스 로직은 프레임워크와 플랫폼에 종속되지 않고, 독립적이어야 한다.
* 공통 로직은 처음부터 코틀린을 직접 만들거나, 다른 공통 모듈을 활용해서 만들 수 있다. 만들기만 하면, 여러 플랫폼에서 사용할 수 있다.

### 라이브러리
* 플랫폼에 크게 의존하지 않는다는 점은 공통 모듈을 JVM, 자바스크립트, 네이티브 환경에서 작동하는 모든 언어에서 활용할 수 있다는 의미이다.

### 함께 사용하기
* 코틀린으로 작성할 수 있는 것들
  * 코틀린/JVM을 사용한 백엔드 개발 - 스프링, Ktor 등
  * 코틀린/JS를 사용한 웹사이트 개발 - 리액트 등
  * 코틀린/JVM을 사용한 안드로이드 개발 - 안드로이드 SDK 등
  * 코틀린/네이티브를 통해 Objective-C/스위프트로 iOS 프레임워크 개발
  * 코틀린/JVM을 사용한 데스크톱 개발 - TornadoFX 등
  * 코틀린/네이티브를 사용한 라즈레비파이, 리눅스, macOS 프로그램 개발
