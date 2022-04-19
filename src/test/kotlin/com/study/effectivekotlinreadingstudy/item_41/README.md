# Item 41. hashCode 규약을 지켜라

- - -

### hashCode의 규약
* 어떤 객체를 변경하지 않았다면(equals에서 비교에 사용된 정보가 수정되지 않는 이상), hashCode는 여러 번 호출해도 그 결과가 항상 같아야 한다.
* equals 메서드의 실행 결과로 두 객체가 같다고 나오면, hashCode 메서드의 호출 결과도 같다고 나와야 한다.
* hashCode는 최대한 요소를 넓게 퍼뜨려야 한다. 다른 요소라면 최대한 다른 해시 값을 갖는 것이 좋다.

### hashCode 구현하기
* data 한정자를 붙이면, 코틀린이 알아서 equals와 hashCode를 정의해 준다.
* equals를 따로 정의했다면, 반드시 hashCode도 함께 정의해야 한다.
* equals를 따로 정의하지 않았다면, 이유가 없는 이상 hashCode를 따로 정의하지 않는 것이 좋다.
* equals로 같은 요소라고 판단되는 요소는 hashCode가 반드시 같은 값을 리턴해야 한다.
* hashCode는 기본적으로 equals에서 비교에 사용되는 프로퍼티를 기반으로 해시 코드를 만들어야 한다.
* 유용한 함수로 코틀린/JVM의 Objects.hashCode가 있다.
