package AoC_2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AoC_2 {
	static List<Integer> _moduleMass = new ArrayList<Integer>();

	public static void main(String[] args) throws IOException {
		readInput();

		System.out.println("f" + calc(12));
		System.out.println("f" + calc(14));
		System.out.println("f" + calc(1969));
		System.out.println("f" + calc(100756));
		System.out.println("Sum of fuel = " + del1());

		System.out.println("f" + reccalc(14));
		System.out.println("f" + reccalc(1969));
		System.out.println("f" + reccalc(100756));
		System.out.println("Sum of fuel = " + del2());
	}

	private static int reccalc(int a) {
		return calc(a) > 0 ? calc(a) + reccalc(calc(a)) : 0;
	}

	private static int calc(int a) {
		return a / 3 - 2;
	}

	public static int del1() {
		return _moduleMass.stream().map(i -> calc(i)).reduce(Integer::sum).get();
	}

	public static int del2() {
		return _moduleMass.stream().map(i -> reccalc(i)).reduce(Integer::sum).get();
	}

	private static void readInput() throws FileNotFoundException, IOException {
		String str;
		FileInputStream in = new FileInputStream("C:\\git\\AoC2019\\AoC_1\\src\\AoC_1\\input.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		while ((str = br.readLine()) != null) {
			_moduleMass.add(Integer.parseInt(str));
		}
		br.close();
	}

}
