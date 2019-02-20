package enums;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by rnthd2 on 2019. 1. 21..
 */

//(2) 출력안됨
//interface FRUIT{
//	int APPLE = 1, PEACH = 2, BANANA = 3;
//}
//interface COMPANY{
//	int GOOGLE = 1, APPLE = 2, ORACLE = 3;
//}

//(3)switch 구분 안됨
//class Fruit{
//	public static final Fruit APPLE = new Fruit();
//	public static final Fruit PEACH = new Fruit();
//	public static final Fruit BANANA = new Fruit();
//}
//class Company{
//	public static final Company GOOGLE = new Company();
//	public static final Company APPLE = new Company();
//	public static final Company ORACLE = new Company();
//}

class EffectiveJava3_6 {

//	(1) 구분 어렵
//	public final static int FRUIT_APPLE = 1;
//	public final static int FRUIT_PEACH = 2;
//	public final static int FRUIT_BANANA = 3;

//	public final static int COMPANY_GOOGLE = 1;
//	public final static int COMPANY_APPLE = 2;
//	public final static int COMPANY_ORACLE = 3;


	public static void main(String[] args) {

//		(2)not compile error, logical and runtime error
//		[p209] 이렇게 하드코딩한 문자열에 오타가 있어도 컴파일러는 확인할 길이 없으니 자연스럽게 런타임 버그가 생긴다 문자열 비교에 따른 성능 저하 역시 당연한 결과다.
//		if(FRUIT.APPLE == COMPANY.APPLE){
//			//출력 안됨
//			System.out.println("과일 애플과 기업 애플은 같습니다.");
//		}

//		(3)compile error => complete!
//		[p210] 다른 타입의 값을 넘기려 하면 컴파일 오류가 난다.
		// 타입이 다른 열거타입 변수에 할당하려 하거나 다른 열거타입의 값끼리 ==연산자로 비교하려는 꼴이기 때문이다.
//		if(Fruit.APPLE == Company.APPLE){
//			//출력 안됨
//			System.out.println("과일 애플과 기업 애플은 같습니다.");
//		}
//		//Expression에는 char, byte, short, int, Character, Byte, Short, Integer, String
		// 그리고 enum 타입이 올 수 있으며, 기존에 허용되지 않던 String 타입이 추가되었습니다.
//		//http://mussebio.blogspot.com/2012/05/java7-switch.html
//		Fruit type = Fruit.BANANA;
//		switch (type){
//
//		}

		double operation = Operation.PLUS.apply(1, 2);
		System.out.println(operation);
		double operation2 = Operation2.MINUS.apply(3, 4);
		System.out.println(operation2);
		double payRollDay = PayrollDay.WEDNESDAY.pay(1, 1);
		System.out.println(payRollDay);
		double payRollDay2 = PayrollDay.SATURDAY.pay(1, 1);
		System.out.println(payRollDay2);
		Phase.Transition phase = Phase.Transition.from(Phase.GAS,Phase.LIQUID);
		System.out.println(phase);
		Phase2.Transition phase2 = Phase2.Transition.from(Phase2.GAS,Phase2.LIQUID);
		System.out.println(phase2);







	}
}

//[p210] 싱글턴은 원소가 하나뿐인 열거 타입이라 할 수 있고 거꾸로 열거 타입은 싱글턴을 일반화한 형태라고 볼 수 있다.
enum Operation {
//	public static final
	PLUS, MINUS, TIMES, DIVIDE;

	double apply(double x, double y) {
		switch (this) {
			case PLUS:
				return x + y;
			case MINUS:
				return x - y;
			case TIMES:
				return x * y;
			case DIVIDE:
				return x / y;
		}
		throw new AssertionError("Unknown op:" + this);
	}
}

//[p211] 열거 타입 상수 각각을 특정 데이터와 연결지으려면 생성자에서 데이터를 받아 인스턴스 필드에 저장하면 된다.
enum Operation2 {

	PLUS("+") {
		double apply(double x, double y) {
			return x + y;
		}
	},
	MINUS("-") {
		double apply(double x, double y) {
			return x - y;
		}
	},
	TIMES("*") {
		double apply(double x, double y) {
			return x * y;
		}
	},
	DIVIDE("/") {
		double apply(double x, double y) {
			return x / y;
		}
	};

	private final String symbol;

	abstract double apply(double x, double y);

	Operation2(String symbol) {
		this.symbol = symbol;
	}

	@Override public String toString() {
		return symbol;
	}
}

enum PayrollDay {

	MONDAY(PayType.WEEKDAY), TUESDAY(PayType.WEEKDAY), WEDNESDAY(PayType.WEEKDAY),
	THURSDAY(PayType.WEEKDAY), FRIDAY(PayType.WEEKDAY), SATURDAY(PayType.WEEKEND),
	SUNDAY(PayType.WEEKEND),HOLIDAY(PayType.WEEKEND);

	private final PayType payType;

	PayrollDay(PayType payType) {
		this.payType = payType;
	}

	double pay(double hoursWorked, double payRate) {
		return payType.pay(hoursWorked, payRate);
	}

	private enum PayType {
		WEEKDAY {
			double overtimePay(double hours, double payRate) {
				return hours <= HOURS_PER_SHIFT ? 0 : (hours - HOURS_PER_SHIFT) * payRate / 2;
			}
		},
		WEEKEND {
			double overtimePay(double hours, double payRate) {
				return hours * payRate / 2;
			}
		};

		private static final int HOURS_PER_SHIFT = 8;

		abstract double overtimePay(double hrs, double payRate);

		double pay(double hoursWorked, double payRate) {
			double basePay = hoursWorked * payRate;
			return basePay + overtimePay(hoursWorked, payRate);
		}
	}
}


//(1)EnumSet
//그 전에 책의 이해를 위한 비트연산
/*
비트연산 == 2진수 표기법

1 == 0001
2 == 0010
4 == 0100
8 == 1000

BOLD = 0001
ITALIC = 0010
UNDERLINE = 0100
STRIKE = 1000

BOLD | ITALIC == 0001 or 0010 == 0011 == 3
BOLD & ITALIC == 0001 and 0010 == 0010 == 0

가독성 너무 떨어짐...괴로움...
*/
/** * Simple Java Program to demonstrate how to use EnumSet.
 * Using Enum with EnumSet will give you far better
 * performance than using Enum with HashSet, or LinkedHashSet.
 * @author Javin Paul
출처: https://hamait.tistory.com/383 [HAMA 블로그]*/

class EnumSetDemo {
	private enum Color {
		RED(255, 0, 0),
		GREEN(0, 255, 0),
		BLUE(0, 0, 255);

		private int r;
		private int g;
		private int b;

		private Color(int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}

		public int getR() {
			return r;
		}

		public int getG() {
			return g;
		}

		public int getB() {
			return b;
		}
	}

	public static void main(String args[]) {
		// this will draw line in yellow color
		EnumSet<Color> yellow = EnumSet.of(Color.RED, Color.GREEN);
		drawLine(yellow); // RED + GREEN + BLUE = WHITE

		EnumSet<Color> white = EnumSet.of(Color.RED, Color.GREEN, Color.BLUE);
		drawLine(white); // RED + BLUE = PINK

		EnumSet<Color> pink = EnumSet.of(Color.RED, Color.BLUE);
		drawLine(pink);

	}

	public static void drawLine(Set<Color> colors) {
		System.out.println("Requested Colors to draw lines : " + colors);
		for (Color c : colors) {
			System.out.println("drawing line in color : " + c);
		}
	}

	public static void hashSetPerformance(){
		Set<Color> colors = new HashSet<>();
		colors.add(Color.RED);
		colors.add(Color.BLUE);
		colors.add(Color.GREEN);
		drawLine(colors);
	}

	public static void linkedHashSetPerformance(){
		Set<Color> colors = new LinkedHashSet<>();
		colors.add(Color.RED);
		colors.add(Color.BLUE);
		colors.add(Color.GREEN);
		drawLine(colors);
	}
}
//[p225]EnumSet의 유일한 단점이라면(자바 9까지는 아직) 불변 EnumSet을 만들 수 없다는 것이다.
// 그래도 향후 릴리스에서 수정되리라 본다.
// 그때까지는 (명확성과 성능이 조금 희생되지만) Collections.unmodifiableSet으로 EnumSet을 감싸 사용할 수 있다.
// (옮긴이) 이책의 2판에도 언급되었지만, 자바 11까지도 이 수정은 이뤄지지 않았다.
// 조슈아 블로크의 바람과 달리 자바 개발팀은 불변 EnumSet이 그리 필요하지 않다고 보는 것 같다.

enum Phase {
	SOLID, LIQUID, GAS;
	public enum Transition {
		MELT, FREEZE, BOIL, CONDENSE, SUBLIME, DEPOSIT;
		//아래 배열의 행은 상전이 이전 enum, 열은 상전이 이후 enum를 나타냄
		private static final Transition[][] Transitions = {
				{null, MELT, SUBLIME},
				{FREEZE, null, BOIL},
				{DEPOSIT, CONDENSE, null}
		};

		public static Transition from(Phase src, Phase dst) {
			return Transitions[src.ordinal()][dst.ordinal()];
		}
	}
}

//(2)EnumMap : 맵에 키값으로 enum 필드를 사용할수 있는 자료구조
//todo EnumMap VS HashMap 비교
/**
 * EnumMap VS HashMap
 * enumMap의 index는 Enum의 내부 순서를 이용하므로 hashMap의 Hashing을 통한 index보다 효율적이다.
 * HashMap의 경우 일정한 이상의 자료가 저장 되면, 자체적으로 Resizing을 한다.
 * 그로 인해 성능 저하가 발생한다. 그러나 EnumMap의 경우 Enum의 갯수로 제한 되므로 Resizing에 대한 성능 저하는 없다.
 * put/get에 있어서 O(1)의 복잡도를 보장한다.(HashMap의 경우 보장 할 수 없다.)
 * 입력에 관계없이 실행하는 데 걸리는 시간은 동일하므로 O (1) 정의입니다.
 *
 */
enum Phase2 {
	SOLID, LIQUID, GAS;

	public enum Transition {
		MELT(SOLID, LIQUID), FREEZE(LIQUID, SOLID),
		BOIL(LIQUID, GAS), CONDENSE(GAS, LIQUID),
		SUBLIME(SOLID, GAS), DEPOSIT(GAS, SOLID);

		private final Phase2 src;
		private final Phase2 dst;

		private Transition(Phase2 src, Phase2 dst) {
			this.src = src;
			this.dst = dst;
		}

		private static final Map<Phase2, Map<Phase2, Transition>> m =
				new EnumMap<Phase2, Map<Phase2, Transition>>(Phase2.class);

		static {
			for (Phase2 p : Phase2.values()) {
				m.put(p, new EnumMap<Phase2, Transition>(Phase2.class));
			}
			values();
			for (Transition trans : Transition.values()) {
				m.get(trans.src).put(trans.dst, trans);
			}
		}

		public static Transition from(Phase2 src, Phase2 dst) {
			return m.get(src).get(dst);
		}
	}
	public void test(){
		switch (this){
			case SOLID :
			System.out.println("SOLID");
				break;
			case LIQUID :
//				...
		}
	}
}
//[p230] 2판의 코드가 더 장황하긴 하나 아마도 이해하기는 더 쉬울 것이다.


//이 내용은, 약간 논외인듯
//[p213] 일반 클래스와 마찬가지로, 그 기능을 클라이언트에 노출해야 할 합당한 이유가 없다면 private으로 혹은 package-private으로 선언하라.
//널리 쓰이는 열거 타입은 톱레벨 클래스로 만들고 특정 톱레벨 클래스에서만 쓰인다면 해당 클래스의 멤버 클래스로 만든다.
//예를 들어 소수 자릿수의 반올림 모드를 뜻하는 BigDecimal이 사용한다.
//그런데 반올림 모드는 BigDecimal과 관련 없는 영역에서도 융용한 개념이라 자바 라이브러리 설계자는 RoundingMode를 톱레벨로 올렸다.
//class packagePrivateStudy{
//	BigDecimal;
//	RoundingMode;
//}

//참고
//생활코딩 동영상
//우아한형제들 기술 블로그 http://woowabros.github.io/tools/2017/07/10/java-enum-uses.html
//http://www.nextree.co.kr/p11686/



/**
 *
 * 추가로 위의 상황을 이용하면 좋겠다는 경우가 생겼지만 사용하지 못한 케이스
 *
 *
 * */
//enum PlanningTypeDemo {
//	CATEGORY_PLANNING("CAT", "카테고리 기획전"),
//	MARKETING_PLANNING("MKT", "마케팅 기획전"),
//	BRAND_PLANNING("BRD", "브랜드 기획전"),
//	OPENMARKET_PLANNING("OM", "오픈마켓 기획전");
//
//	private String code;
//	private String desc;
//}