# Item 50. 컬렉션 처리 단계 수를 제한하라

- - -

* 모든 컬렉션 처리 메서드는 비용이 많이 든다.
* 표준 컬렉션 처리는 내부적으로 요소들을 활용해 반복을 돌며, 내부적으로 계산을 위해 추가적인 컬렉션을 만들어 사용한다. (Item 49 참조)
* 시퀀스 처리도 시퀀스 전체를 랩하는 객체가 만들어지며, 조작을 위해서 또 다른 추가적인 객체를 만들어 낸다. (연산 내용이 시퀀스 객체로 전달되므로, 인라인으로 사용할 수 없다. 따라서 람다 표현식을 객체로 만들어 사용해야 한다.)
* 두 처리 모두 요소의 수가 많다면, 꽤 큰 비용이 들어간다. 따라서 적절한 메서드를 활용해서, 컬렉션 처리 단계 수를 적절하게 제한하는 것이 좋다.
