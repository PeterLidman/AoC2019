package AoC_6;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AoC_6 {
	static List<String> _c = new ArrayList<String>();
	static List<String> _o = new ArrayList<String>();
	static HashMap<String, Integer> _orbs = new HashMap<>();

	public static void main(String[] args) throws IOException, FileNotFoundException {
		String cs;
		int count = 0;

		readInput();

		for (int i = 0; i < _o.size(); i++) {
			cs = getCenter(_o.get(i));
			while (!cs.equals("COM")) {
				count++;
				cs = getCenter(cs);
			}
			count++;
		}
		System.out.println("Orbits " + count);

		count = 0;
		cs = getCenter("YOU");
		while (!cs.equals("COM")) {
			count++;
			cs = getCenter(cs);
			_orbs.put(cs, count);
		}

		count = 0;
		cs = getCenter("SAN");
		while (!cs.equals("COM")) {
			count++;
			cs = getCenter(cs);
			if (_orbs.get(cs) != null) {
				System.out.println("You " + _orbs.get(cs) + " Santa " + count + " sum " + (_orbs.get(cs) + count));
				break;
			}
		}
	}

	private static String getCenter(String p) {
		if (p.equals("COM")) {
			return "COM";
		}

		for (int i = 0; i < _o.size(); i++) {
			if (_o.get(i).equals(p)) {
				return _c.get(i);
			}
		}
		return "XXX";
	}

	private static void readInput() throws FileNotFoundException, IOException {
		String str;
		FileInputStream in = new FileInputStream("C:\\git\\AoC2019\\AoC_6\\src\\AoC_6\\input.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		while ((str = br.readLine()) != null) {
			_c.add(str.substring(0, 3));
			_o.add(str.substring(4, 7));
		}
		br.close();
	}

}
