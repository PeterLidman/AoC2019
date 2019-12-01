package AoC_1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AoC_1 {
	static List<Integer> frekvensJusteringar = new ArrayList<Integer>();

	public static void main(String[] args) throws IOException {
		String str;
		FileInputStream in = new FileInputStream("C:\\git\\AoC2019\\AoC_1\\src\\AoC_1\\input.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		while ((str = br.readLine()) != null) {
			frekvensJusteringar.add(Integer.parseInt(str));
		}
		br.close();

		System.out.println("f" + calc(12));
		System.out.println("f" + calc(14));
		System.out.println("f" + calc(1969));
		System.out.println("f" + calc(100756));

		del1();

		System.out.println("f" + reccalc(14));
		System.out.println("f" + reccalc(1969));
		System.out.println("f" + reccalc(100756));

		del3();
	}

	private static int reccalc(int a) {
		int b = a, sum = 0;

		while (0 < (b = calc(b))) {
			sum += b;
		}
		return sum;
	}

	private static int calc(int a) {
		return a / 3 - 2;
	}

	public static void del1() {
		int sum = 0;

		for (Integer frekvens : frekvensJusteringar) {
			sum += calc(frekvens);
		}
		System.out.println("Frekvens: " + sum);
	}

	public static void del3() {
		int sum = 0;

		for (Integer frekvens : frekvensJusteringar) {
			sum += reccalc(frekvens);
		}
		System.out.println("Frekvens: " + sum);
	}

}
