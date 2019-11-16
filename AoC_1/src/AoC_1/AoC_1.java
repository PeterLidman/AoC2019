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
		del1();
		del2();
	}

	public static void del1() {
		int sum = 0;

		for (Integer frekvens : frekvensJusteringar) {
			sum += frekvens;
		}
		System.out.println("Frekvens: " + sum);
	}

	public static void del2() {
		int sum = 0;
		Set<Integer> frekvenser = new HashSet<>();
		boolean found = false;
		frekvenser.add(0);

		do {
			for (Integer frekvens : frekvensJusteringar) {
				sum += frekvens;
				if (found = !frekvenser.add(sum)) {
					break;
				}
			}
		} while (!found);
		System.out.println("Första dubbletten: " + sum);
	}

}
