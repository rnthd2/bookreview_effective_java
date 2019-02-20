package annotation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnthd2 on 2019. 1. 24..
 */
/*
모던 랭귀지들은 타입바운드 개념을 제공합니다.
타입바운드는 3가지로 분류 할 수 있습니다. 무공변성(invariant), 공변성(covariant), 반공변성(contravariant)

공변성 T'가 T의 서브타입이면, C<T'>는 C<T>의 서브타입이다.
반공변성 T'가 T의 서브타입이면, C<T>는 C<T'>의 서브타입이다.
무변성 C<T>와 C<T'>는 아무 관계가 없다.

1. 자바의 제네릭은 기본적으로 무변성이다.
2. 자바는 서브타입 와일드카드(<? extends String>)를 이용하여 공변성을 표현한다.
3. 서브타입 와일드카드가 공변성을 나타낸다면 슈퍼타입 와일드카드(<? super String>)는 반공변성을 나타낸다.
4. 메서드의 반환 타입은 공변적이고, 메서드의 파라미터 타입은 반공변적 이라는 것을 알 수 있다.

공변성 T'가 T의 서브타입이면, C<T'>는 C<? extends T>의 서브타입이다.
반공변성 T'가 T의 서브타입이면, C<T>는 C<? super T'>의 서브타입이다.
무변성 C<T>와 C<T'>는 아무 관계가 없다.

출처: http://happinessoncode.com/2017/05/21/java-generic-and-variance-1/
*/
public class EffectiveJava_Theory_TypeBound {
	public static void main(String[] args) {

//		4. 메서드의 반환 타입은 공변적이고, 메서드의 파라미터 타입은 반공변적 이라는 것을 알 수 있다.
		List<? extends String> extends_list = new ArrayList<>();
//		어떤걸 get을 해도 String이 가장 슈퍼니까 Object 가 자식을 다 받을 수 있는거랑 같지 >> compile error 안남!
		String first = extends_list.get(0);
//		String에 상속된 걸 넣으라능~ >> compile error
//		extends_list.add("test");

		List<? super String> super_list = new ArrayList<>();
//		get이 뭘 반환할지 알고 String으로 받냠 >> compile error! >> String 이 Object를 받을 수 없잖아
//		String first = super_list.get(0);
//		super String 이라면 String을 받을 수 있으니까 add에 String을 넣을 수 있는건 명백함 >> compile error 안남!
		super_list.add("test");

	}
}
