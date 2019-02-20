package annotation;

/**
 * Created by rnthd2 on 2019. 1. 24..
 */
/*배열과 제네릭 자료형과 두 가지 중요한 차이점을 갖고 있다
배열은 공변 자료형이라는 것이다.
반면 제네릭은 무공변 자료형이다.
EffectiveJava_Theory_TypeBound참조

배열은 실체화되는 자료형이다.
배열의 각 원소의 자료형은 실행할 때 결정된다는 것이다.
반면 제네릭은 컴파일 시점에만 자료형에 대한 조건들이 적용되고, 실제 실행할 때는 자료형에 대한 정보가 사라진다(타입 이레이저, 다른 내용으로 더 알아 보자).
자료형 삭제덕에, 제네릭 자료형은 제네릭을 사용하지 않고 작성된 오래된 코드와도 문제없이 연동된다.

결론부터 얘기하자면, 제네릭배열은 컴파일 오류가 난다.
왜 이렇게 구성되어있냐면,
배열은 공변 자료형이기에 Object배열에 String이 들어갈 수 있다.
제네릭 배열이 가능하다면, Object배열에 List<String>과 List<Integer>가 들어갈 수 있다.
이렇게 되면 ClassCastException이 발생할 가능성이있다.

출처: https://dev-troh.tistory.com/85 [개발공부블로그]
https://medium.com/asuraiv/java-type-erasure%EC%9D%98-%ED%95%A8%EC%A0%95-ba9205e120a3 << 타입 이레이저
deal_max
----- 사실 여기까지가 아는내용의 복습!! 이라 하고 한정적/비한정적 와일드카드에 대한 실체화를 찾아보자 ------


 */
public class EffectiveJava_Generic<T> {
	private T[] elements;
	private static final int DEFAULT_INITIAL_CAPACITY = 16;

	public EffectiveJava_Generic() {
//		compile error! >> T로 초기화 할 수 없단다
//		elements = new T[DEFAULT_INITIAL_CAPACITY];

//		cast해서 해보자, 일단 어케어케 들어는간다
		elements = (T[]) new Object[DEFAULT_INITIAL_CAPACITY];

	}
}
