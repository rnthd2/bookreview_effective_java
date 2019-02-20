package annotation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnthd2 on 2019. 1. 28..
 */

//이 책 6장 애너테이션 부분은 사용자(커스텀) 어노테이션에 대한 설명이다

//[p238]애너테이션 선언에 다는 애너테이션을 메타애너테이션이라 한다.

//[p238]런타임에도 유지되어야한다는 표시다
// 만약 이 메타애너테이션을 생략하면 테스트 도구는 @Test를 인식할 수 없다.
//개발자는 해당 어노테이션을 소스코드에서 단순 주석으로 사용할 지, 컴파일시기까지 유지할지, 런타임까지 유지할 것인지를 결정할 수 있습니다.
// 자세한 설명은 http://kang594.blog.me/39704853
@Retention(RetentionPolicy.SOURCE)

//[p238]반드시 메서드에서만 사용돼야 한다고 알려준다.
// 매개 변수 없는 정적 메서드 전용이다. -> 강제하려면 애너테이션 처리기를 직접 구현해야 한다.
@Target(ElementType.METHOD)
@interface Test {
}

class Sample {
	//[p238]@Test 애너테이션을 실제 적용한 모습이다.
	//[p238]"아무 매개변수 없이 단순히 대상에 마킹한다는 뜻에서 마커 애너테이션이라 한다.
	@Test public static void m1() {
	}

	public static void m2() {
	}

	@Test public static void m3() {
		throw new RuntimeException("실패");
	}

	public static void m4() {
	}

	@Test public void m5() { // 잘못 사용한 예 : 정적 메서드가 아니다.
	}

	public static void m6() {
	}

	@Test public static void m7() {
		throw new RuntimeException("실패");
	}

	public static void m8() {
	}
//	@NotNull
}

public class EffectiveJava_Annotation_3_6_3 {

	/**
	 * (1)
 	 */
	/*public static void main(String[] args) throws Exception {
		int tests = 0;
		int passed = 0;
		//		https://stackoverflow.com/questions/23634909/reflection-exception-in-thread-main-java-lang-arrayindexoutofboundsexception
		//		Class<?> testClass = Class.forName(args[0]);
		Class<?> testClass = Sample.class;
		for (Method m : testClass.getDeclaredMethods()) {
			//[p240] Test에너테이션이 달린 애들을 호출해서 isAnnotationPresent가 실행할 메서드를 찾아주는 메서드다
			if (m.isAnnotationPresent(Test.class)) {
				tests++;
				try {
					m.invoke(null);
					passed++;
				} catch (InvocationTargetException wrappedExc) {
//					[p240] Test 메서드가 예외를 던지면 리플렉션 매커니즘이 InvocationTargetException를 감싸서 다시 던진다.
//					Java 리플렉션을 통해서 우리는 필드 정보를 얻어서 해당 필드에 붙어있는 어노테이션의 정보까지 사용할 수 있기 때문에 편리하게 구현이 가능합니다.
					Throwable exc = wrappedExc.getCause();
					System.out.println(m + " failed: " + exc);
				}catch(Exception exc){
					System.out.println("Invalid @Test: " + m);
				}
			}
		}
		System.out.printf("Passed: %d, Failed: %d%n",
				passed, tests - passed);
	}*/


	/**
	 * (2)
	 */
	/*public static void main(String[] args) throws Exception {
		int tests = 0;
		int passed = 0;
		Class<?> testClass = Sample2.class;
		for (Method m : testClass.getDeclaredMethods()) {

			if (m.isAnnotationPresent(ExceptionTest.class)) {
				tests++;
				try {
					m.invoke(null);
					System.out.printf("Test %s failed: no exception%n", m);
				} catch (InvocationTargetException wrappedEx) {
					Throwable exc = wrappedEx.getCause();
					Class<? extends Throwable> excType =
							m.getAnnotation(ExceptionTest.class).value();
					if (excType.isInstance(exc)) {
						passed++;
					} else {
						System.out.printf(
								"Test %s failed: expected %s, got %s%n",
								m, excType.getName(), exc);
					}
				} catch (Exception exc) {
					System.out.println("Invalid @ExceptionTest: " + m);
				}
			}
		}

		System.out.printf("Passed: %d, Failed: %d%n",
				passed, tests - passed);
	}*/

	/**
	 * (3)
	 * @param args
	 * @throws Exception
	 */
	/*public static void main(String[] args) throws Exception {
		int tests = 0;
		int passed = 0;
		Class<?> testClass = Sample3.class;
		for (Method m : testClass.getDeclaredMethods()) {

			// Code to process annotations with array parameter (Page 185)
			if (m.isAnnotationPresent(ExceptionClassTest.class)) {
				tests++;
				try {
					m.invoke(null);
					System.out.printf("Test %s failed: no exception%n", m);
				} catch (Throwable wrappedExc) {
					Throwable exc = wrappedExc.getCause();
					int oldPassed = passed;
					Class<? extends Throwable>[] excTypes =
							m.getAnnotation(ExceptionClassTest.class).value();
					for (Class<? extends Throwable> excType : excTypes) {
						if (excType.isInstance(exc)) {
							passed++;
							break;
						}
					}
					if (passed == oldPassed)
						System.out.printf("Test %s failed: %s %n", m, exc);
				}
			}
		}
		System.out.printf("Passed: %d, Failed: %d%n",
				passed, tests - passed);
	}*/

	/**
	 * (4)
	 */
	/*public static void main(String[] args) throws Exception {
		int tests = 0;
		int passed = 0;
		Class testClass = Sample4.class;
		for (Method m : testClass.getDeclaredMethods()) {

			// Processing repeatable annotations (Page 187)
			if (m.isAnnotationPresent(ExceptionClassTest2.class)
					|| m.isAnnotationPresent(ExceptionTestContainer.class)) {
				tests++;
				try {
					m.invoke(null);
					System.out.printf("Test %s failed: no exception%n", m);
				} catch (Throwable wrappedExc) {
					Throwable exc = wrappedExc.getCause();
					int oldPassed = passed;
					ExceptionClassTest2[] excTests =
							m.getAnnotationsByType(ExceptionClassTest2.class);
					for (ExceptionClassTest2 excTest : excTests) {
						if (excTest.value().isInstance(exc)) {
							passed++;
							break;
						}
					}
					if (passed == oldPassed)
						System.out.printf("Test %s failed: %s %n", m, exc);
				}
			}
		}
		System.out.printf("Passed: %d, Failed: %d%n",
				passed, tests - passed);
	}*/

	/**
	 * (5)
	 */
	//NotSerializableException 가 생기는게 정상!
	//마커 인터페이스 같은 경우에는 컴파일 시점에 발견할 수 있다는 큰 장점이 있다.
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		SomeObject someObject = new SomeObject("rnthd2", "rnthd2@tmon.co.kr");
		someObject.serializableTest();
	}


}
/**
 * 갑분 리플렉션...
 * 리플렉션이란 객체를 통해 클래스의 정보를 분석해 내는 프로그램 기법을 말한다. 투영, 반사 라는 사전적인 의미를 지니고 있다
 *
 * 출처: https://gyrfalcon.tistory.com/entry/Java-Reflection [Minsub's Blog]
*/

/*
* 이 기능을 잘 활용한다면,
* 비즈니스 로직과는 별로도 시스템 설정과 관련된 부가적인 사항들은 @에게 위임하고 개발자는 비즈니스 로직 구현에 집중할 수 있습니다.
* 따라서 어노테이션을 통해 우리는 AOP(Aspect Oriented Programing; 관심지향프로그래밍)을 편리하게 구성할 수 있습니다.
* 어노테이션은 컴파일시기에 처리될 수도 있고 자바의 리플렉션을 거쳐서 런타임에 처리될 수도 있습니다.
* 리플렉션은 실행중인 자바 클래스의 정보를 볼 수 있게 하고, 그 클래스의 구성 정보로 기능을 수행할 수 있도록 합니다.
* 따라서 자바는 리플렉션 기능이 있기 때문에 어노테이션을 더욱 효율적으로 활용할 수 있습니다.
*
* 출처: http://www.nextree.co.kr/p5864/
*/

/**
 * (2)[p241] 특정 예외를 던져야만 성공하는 테스트를 지원하도록 해보자.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface ExceptionTest {
	//[p241] Throwable을 확장한 클래스의 Class 객체 라는 뜻이며,
	//따라서 모든 예외(와 오류)타입을 다 수용한다.
	Class<? extends Throwable> value();
}

class Sample2 {
	@ExceptionTest(ArithmeticException.class)
	public static void m1() {  // Test should pass
		int i = 0;
		i = i / i;
	}

	@ExceptionTest(ArithmeticException.class)
	public static void m2() {  // Should fail (wrong exception)
		int[] a = new int[0];
		int i = a[1];
	}

	@ExceptionTest(ArithmeticException.class)
	public static void m3() {
	}  // Should fail (no exception)
}

/**
 * (3)[p242] @ExceptionClassTest 애너테이션의 매개변수 타입을 Class 객체의 배열로 수정해보자.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface ExceptionClassTest {
	Class<? extends Exception>[] value();
}

class Sample3 {
	// This variant can process annotations whose parameter is a single element (identical to those on page 183)
	@ExceptionClassTest(ArithmeticException.class)
	public static void m1() {  // Test should pass
		int i = 0;
		i = i / i;
	}
	@ExceptionClassTest(ArithmeticException.class)
	public static void m2() {  // Should fail (wrong exception)
		int[] a = new int[0];
		int i = a[1];
	}
	@ExceptionClassTest(ArithmeticException.class)
	public static void m3() { }  // Should fail (no exception)

	// Code containing an annotation with an array parameter (Page 185)
	@ExceptionClassTest({ IndexOutOfBoundsException.class,
			NullPointerException.class })
	public static void doublyBad() {   // Should pass
		List<String> list = new ArrayList<>();

		// The spec permits this method to throw either
		// IndexOutOfBoundsException or NullPointerException
		list.addAll(5, null);
	}
}
/**
 * (4) [p243] 자바에서는 여러 개의 값을 받는 애너테이션을 다른 방식으로도 만들 수 있다.
 * @Repeatable을 단 애너테이션은 하나의 프로그램 요소에 여러 번 달 수 있다.
 *
 * 단, 주의할 점이 있다.
 * @Repeatable을 단 애너테이션을 반환하는 '컨테이너 애너테이션을 하나 더 정의하고,
 * @Repeatable에 이 컨테이너 애너테이션의 class 객체를 매개변수로 전달해야 한다.
 *
 * 컨테이너 애너테이션은 내부 애너테이션 타입의 배열을 반환하는 value 메서드를 정의해야한다.
 *
 * 컨테이너 애너테이션 타입에는 적절한 보존 정책(@Retention)과 적용대상(@Target)을 명시해야 한다.
 *
 * 위 주위점을 안지키면 컴파일 에러가 발생 할 수 있다.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ExceptionTestContainer.class)
@interface ExceptionClassTest2 {
	Class<? extends Throwable> value();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface ExceptionTestContainer {
	ExceptionClassTest2[] value();
}

// Program containing repeatable annotations (Page 186)
class Sample4 {
	@ExceptionClassTest2(ArithmeticException.class)
	public static void m1() {  // Test should pass
		int i = 0;
		i = i / i;
	}

	@ExceptionClassTest2(ArithmeticException.class)
	public static void m2() {  // Should fail (wrong exception)
		int[] a = new int[0];
		int i = a[1];
	}

	@ExceptionClassTest2(ArithmeticException.class)
	public static void m3() { }  // Should fail (no exception)

	// Code containing a repeated annotation (Page 186)
	@ExceptionClassTest2(IndexOutOfBoundsException.class)
	@ExceptionClassTest2(NullPointerException.class)
	public static void doublyBad() {
		List<String> list = new ArrayList<>();

		// The spec permits this staticfactory to throw either
		// IndexOutOfBoundsException or NullPointerException
		list.addAll(5, null);
	}
}


/**
 * [p248] 재정의한 모든 메서드에 @Override 애너테이션을 의식적으로 달면 여러분의 실수를 컴파일러가 바로 알려줄 것이다.
 * 예외는 한 가지뿐이다. 구체 클래스에서 상위 클래스의 추상 메서드를 재정의한 경우엔 이 애너테이션을 달지 않아도 된다(단다고 해서 해로울 것도 없다).
 */


/**
 * (5)
 *
 * 두 번째 갑분 ㅠ.ㅠ 오늘 개념 왜 이렇게 많은거쥬
 *
 * 직렬화란?
 * 객체의 직렬화는 객체의 내용을 바이트 단위로 변환하여 파일 또는 네트워크를 통해서 스트림(송수신)이 가능하도록 하는 것을 의미한다.
 *
 * 객체의 내용을 자바 I/O가 자동으로 바이트 단위로 변환하여 저장이나 전송을 해주게 된다.
 * 자바에서 직렬화는 자동으로 처리해주는 것이기 때문에, 운영체제가 달라도 전혀 문제되지 않는다.
 *
 * 출처: https://weicomes.tistory.com/63 [25%]
 */

//자바에서 객체 직렬화는 Serializable 이라는 인터페이스를 implements 하면 된다.
// 그런데 이 인터페이스는 구현해야 할 메소드가 하나도 없다.
// 단지, 객체가 직렬화 대상임을 알려주는 일종의 마커(Marker) 역할을 한다.
// 그래서 이렇게 메소드를 하나도 가지지 않는 인터페이스를 마커인터페이스(Marker Interface)라 부른다.
// 출처: https://blog.doortts.com/153 [여름으로 가는 문]

// 한 마디로, 마커 인터페이스로 Serializable이 있다~ 얘는 직렬화에 사용하는 도구이라능!


//예시 출처 : https://woovictory.github.io/2019/01/04/Java-What-is-Marker-interface/
interface SomethingObject{
}


//직렬화하지 못한 예에~~ 마커 인터페이스인 Serializable을 사용하지 않은 예에~
class SomeObject {
	private String name;
	private String email;
	//생성자 및 기타 메서드 생략

	public SomeObject(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public static void serializableTest() throws IOException, ClassNotFoundException {
		File f= new File("a.txt");
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(f));
		//writeObject -> writeObject0 에서 NotSerializableException 를 볼 수 있다.
		objectOutputStream.writeObject(new SomeObject("wonwoo", "test@test.com"));

		SomeObject someObject = new SomeObject("rnthd2", "rnthd2@tmon.co.kr");
		final SomeAnnotation someAnnotation = someObject.getClass().getAnnotation(SomeAnnotation.class);
	}
}

//씀!
class SerializableSomeObject implements Serializable {
	private String name;
	private String email;
}

//야는 마커 어노테이션!
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface SomeAnnotation {
}

/**
 * [p251] 마커 인터페이스와 마커애너테이션은 각자의 쓰임이 있다
 * 새로 추가하는 메서드 없이 단지 타입 정의가 목적이라면 마커 인터페이스를 선택하자.
 *
 * 클래스나 인터페이스 외의 프로그램 요소에 마킹해야 하거나
 * 애너테이션을 적극 활용하는 프레임워크의 일부로 그 마커를 편입시키고자 한다면
 * 마커 애너테이션이 올바른 선택이다.
 *
 * 시마이!
 */
