package annotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by rnthd2 on 2019. 1. 28..
 */
public class EffectiveJava_Enum_3_6_2 {

//	ITEM34 : 확장 가능한 enum을 만들어야 한다면 인터페이스를 이용하라
//	ENUM 형은 기본적으로 계승이 안된다!!
//	계승이 되는거처럼 흉내 내려면 인터페이스를 만들어서 같은 인터페이스를 implements 할수 있다.

	public interface Operation {
		double apply(double x, double y);
	}

	public enum Operation2 implements Operation{

		PLUS("+") {
			public double apply(double x, double y) {
				return x + y;
			}
		},
		MINUS("-") {
			public double apply(double x, double y) {
				return x - y;
			}
		},
		TIMES("*") {
			public double apply(double x, double y) {
				return x * y;
			}
		},
		DIVIDE("/") {
			public double apply(double x, double y) {
				return x / y;
			}
		};

		private final String symbol;

		Operation2(String symbol) {
			this.symbol = symbol;
		}

		@Override public String toString() {
			return symbol;
		}
	}

	public enum ExtendedOperation implements Operation{

		EXP("^") {
			public double apply(double x, double y) {
				return Math.pow(x, y);
			}
		},
		REMAINDER("%") {
			public double apply(double x, double y) {
				return x % y;
			}
		};

		private final String symbol;

		ExtendedOperation(String symbol){
			this.symbol = symbol;
		}

		@Override public String toString() {
			return symbol;
		}

		public static void main(String[] args) {
			double x = Double.parseDouble(args[0]);
			double y = Double.parseDouble(args[1]);

		}

		//opSet은 이넘과 오퍼레이션 인터페이스를 구현한 것만 가능하다
		//[p235] EnumSet 과 EnumMap을 사용하지 못한다
		private static <T extends  Enum<T> & Operation> void test(Class<T> opSet, double x, double y){
			for (Operation op : opSet.getEnumConstants()) {
				System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
			}
		}

		//EnumSet과 EnumMap 유리하다는데
		private static void test2(Collection<? extends  Operation> opSet, double x, double y){
			for (Operation op : opSet) {
				System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
			}
		}
	}

	public static void main(String[] args) {

		//두개의 결과가 동일하다.
		ExtendedOperation.test(Operation2.class, 1, 2);

		List<Operation2> operation2List = new ArrayList(Arrays.asList(Operation2.values()));
		ExtendedOperation.test2(operation2List, 1, 2);
	}

	//[p235] 인터페이스를 이용해 확장 가능한 열거 타입을 흉내 내는 방식에도 한가지 사소한 문제가 있다.
	//열거 타입끼리 구현을 상속 할 수 없다는 점이다.
	//아무상태에도 의존하지 않는 경우에는 디폴트 구현을 이용해 인터페이스에 추가하는 방법이 있다.

	/**
	 *
	 * 추가로 위의 상황을 이용하면 좋겠다는 경우가 생겼지만 사용하지 못한 케이스
	 *
	 *
	 * */
	enum PlanningType {
		CATEGORY_PLANNING("CAT", "카테고리 기획전"),
		MARKETING_PLANNING("MKT", "마케팅 기획전"),
		BRAND_PLANNING("BRD", "브랜드 기획전"),
		OPENMARKET_PLANNING("OM", "오픈마켓 기획전");

		private String code;
		private String desc;

		PlanningType(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

	}
	enum PlanningMainType {
		INTEGRATION_PLANNING("INTEGRATION", "통합 기획전(MKT, CAT)"),
		BRAND_MALL_PLANNING("BRAND_MALL", "브랜드몰 기획전"),
		ALL_PLANNING("ALL", "모든 기획전");

		private String code;
		private String desc;

		PlanningMainType(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}
	}
}
