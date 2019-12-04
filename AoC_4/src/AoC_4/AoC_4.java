package AoC_4;

import java.io.IOException;

public class AoC_4 {

	public static void main(String[] args) throws IOException {
		int start = 128392;
		int stop = 643281;
		int antal = 0;

		for (int i = start; i <= stop; i++) {
			if (check(i)) {
				System.out.println("pass: " + i);
				antal++;
			}
		}
		System.out.println("Nr of ok passwords: " + antal);
	}

	private static boolean check(int i) {
		String[] s = String.valueOf(i).split("");
		int s1 = Integer.valueOf(s[0]);
		int s2 = Integer.valueOf(s[1]);
		int s3 = Integer.valueOf(s[2]);
		int s4 = Integer.valueOf(s[3]);
		int s5 = Integer.valueOf(s[4]);
		int s6 = Integer.valueOf(s[5]);

		if (s1 > s2 || s2 > s3 || s3 > s4 || s4 > s5 || s5 > s6) {
			return false;
		}
//		if (!(s1 == s2 || s2 == s3 || s3 == s4 || s4 == s5 || s5 == s6)) { //Part 1
//			return false;
//		}
		if (!((s1 == s2 && s2 != s3) || (s2 == s3 && s1 != s2 && s3 != s4) || (s3 == s4 && s2 != s3 && s4 != s5)
				|| (s4 == s5 && s3 != s4 && s5 != s6) || (s5 == s6 && s4 != s5))) {
			return false;
		}
		return true;
	}

}
