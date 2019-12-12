package AoC_12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AoC_12 {

	private static int sum;

	public static void main(String[] args) {
		del1();
		del2();
	}

	private static long gcd(long n1, long n2) {
		if (n1 == 0 || n2 == 0) {
			return n1 + n2;
		} else {
			long absN1 = Math.abs(n1);
			long absN2 = Math.abs(n2);
			long big = Math.max(absN1, absN2);
			long small = Math.min(absN1, absN2);
			return gcd(big % small, small);
		}
	}

	private static long lcm(long n1, long n2) {
		return (n1 == 0 || n2 == 0) ? 0 : Math.abs(n1 * n2) / gcd(n1, n2);
	}

	static void del2() {
		// idé: alla kroppar har omloppsbanor i respektive dimensioner som repeteras
		// svaret är produkten av minsta gemensamma nämnaren av dessa banor
		List<Integer> _p = new ArrayList<>(Arrays.asList(1, -4, 3, -14, 9, -4, -4, -6, 7, 6, -9, -11));// del1
		List<Integer> _s = new ArrayList<>(Arrays.asList(1, -4, 3, -14, 9, -4, -4, -6, 7, 6, -9, -11));// del1 start
//		List<Integer> _p = new ArrayList<>(Arrays.asList(-1, 0, 2, 2, -10, -7, 4, -8, 8, 3, 5, -1));//test1
//		List<Integer> _s = new ArrayList<>(Arrays.asList(-1, 0, 2, 2, -10, -7, 4, -8, 8, 3, 5, -1));//test1 start
//		List<Integer> _p = new ArrayList<>(Arrays.asList(-8, -10, 0, 5, 5, 10, 2, -7, 3, 9, -8, -3));// test2
//		List<Integer> _s = new ArrayList<>(Arrays.asList(-8, -10, 0, 5, 5, 10, 2, -7, 3, 9, -8, -3));// test2 start
		List<Integer> _v = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		List<Long> _r = new ArrayList<>(Arrays.asList(0L, 0L, 0L));

		for (int s = 1; s <= 1000000; s++) {
			// apply gravity
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (i != j) {
						for (int k = 0; k < 3; k++) {// each dimension
							if (_p.get(i * 3 + k) < _p.get(j * 3 + k)) {
								_v.set(i * 3 + k, _v.get(i * 3 + k) + 1);
							} else if (_p.get(i * 3 + k) > _p.get(j * 3 + k)) {
								_v.set(i * 3 + k, _v.get(i * 3 + k) - 1);
							}
						}
					}

				}
			}
			// apply velocity
			for (int i = 0; i < 12; i++) {
				_p.set(i, _p.get(i) + _v.get(i));
			}
//			System.out.println("Step: " + s);
//			System.out.println(_p);
//			System.out.println(_v);
			// find complete rotations
			for (int i = 0; i < 3; i++) {
				if (_v.get(i) == 0 && _v.get(i + 3) == 0 && _v.get(i + 6) == 0 && _v.get(i + 9) == 0) {
					if (_p.get(i) == _s.get(i) && _p.get(i + 3) == _s.get(i + 3) && _p.get(i + 6) == _s.get(i + 6)
							&& _p.get(i + 9) == _s.get(i + 9)) {
						_r.set(i, (long) s);
					}
				}
			}
			// kolla om vi är klara?
			if (_r.get(0) != 0 && _r.get(1) != 0 && _r.get(2) != 0) {
				break;
			}
		}
//		for (int i = 0; i < 3; i++) {
//			System.out.println(_r.get(i));
//		}
		System.out.println("del 2: " + lcm(lcm(_r.get(0), _r.get(1)), _r.get(2)));
	}

	static void del1() {
		List<Integer> _p = new ArrayList<>(Arrays.asList(1, -4, 3, -14, 9, -4, -4, -6, 7, 6, -9, -11));// del1
		// List<Integer> _p = new ArrayList<>(Arrays.asList(-1, 0, 2, 2, -10, -7, 4, -8,
		// 8, 3, 5, -1));//test1
		// List<Integer> _p = new ArrayList<>(Arrays.asList(-8, -10, 0,5, 5, 10,2, -7,
		// 3,9, -8, -3));//test2
		List<Integer> _v = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));

		for (int s = 1; s <= 1000; s++) {
			// apply gravity
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (i != j) {
						for (int k = 0; k < 3; k++) {// each dimension
							if (_p.get(i * 3 + k) < _p.get(j * 3 + k)) {
								_v.set(i * 3 + k, _v.get(i * 3 + k) + 1);
							} else if (_p.get(i * 3 + k) > _p.get(j * 3 + k)) {
								_v.set(i * 3 + k, _v.get(i * 3 + k) - 1);
							}
						}
					}

				}
			}
			// apply velocity
			for (int i = 0; i < 12; i++) {
				_p.set(i, _p.get(i) + _v.get(i));
			}
//			System.out.println("Step: " + s);
//			System.out.println(_p);
//			System.out.println(_v);
			// kinetic
			int pot, kin;
			sum = 0;
			for (int i = 0; i < 4; i++) {
				pot = 0;
				kin = 0;
				for (int j = 0; j < 3; j++) {
					pot += Math.abs(_p.get(i * 3 + j));
					kin += Math.abs(_v.get(i * 3 + j));
				}
//				System.out.println("pot: " + pot + " kin: " + kin + " total:" + (pot * kin));
				sum += (pot * kin);
			}
		}
		System.out.println("Del 1: " + sum);
	}
}
