package AoC_18;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class AoC_18 {
	static HashMap<Point, Integer> _space = new HashMap<>();
	static HashMap<Point, String> _keysAndDoors = new HashMap<>();

	public static void main(final String[] args) {
		final AoC_18 a = new AoC_18();
		a.run();
	}

	public void run() {
		del1();
	}

	public void del1() {
		readInput(18, 4);
		Point start = null;
		for (Entry<Point, String> entry : _keysAndDoors.entrySet()) {
			if (entry.getValue().equals("@")) {
				start = entry.getKey();
			}
		}
		System.out.println("Del 1: " + recGetKeys(start, _keysAndDoors));
	}

	// leta igenom alla hittade nycklar, spawna ny sökning, samla ihop avstånd från
	// alla och välj det minsta
	private int recGetKeys(Point from, HashMap<Point, String> keysLeft) {
		Scanner scan = new Scanner(System.in);
		HashMap<Point, String> keysLeftEgen = new HashMap<>();
		HashMap<Point, Integer> spaceEgen = new HashMap<>();
		HashMap<Point, Integer> incSpaceAfter = new HashMap<>();
		HashMap<Point, Integer> alternativeKeys = new HashMap<>();

		// deepcopy
		Point egenPunkt = new Point(from.x, from.y);
		for (Entry<Point, String> entry : keysLeft.entrySet()) {
			keysLeftEgen.put(new Point(entry.getKey().x, entry.getKey().y), entry.getValue());
		}
		for (Entry<Point, Integer> entry : _space.entrySet()) {
			spaceEgen.put(new Point(entry.getKey().x, entry.getKey().y), entry.getValue());
		}
		printKeys(keysLeftEgen);		
		String key;
		int distance = Integer.MAX_VALUE/2;
		int cDistance = 0;
		boolean full = false;
		// remove key and open door at point from (if key is start only remove key)
		key = keysLeftEgen.get(egenPunkt);
		if (keysLeftEgen.size() == 1) { // om det är sista nyckeln return 0
//			System.out.println("sista nyckeln");
			String text = scan.nextLine();
			return 0;
		}
		if (key == null) {
			System.out.println("ingen key at " + egenPunkt);
		} else if (key.equals("@")) { // startpunkten
//			System.out.println("tar bort @");
			keysLeftEgen.remove(egenPunkt);
		} else { // ta bort nyckel & kanske dörr
			String nyckel = keysLeftEgen.get(egenPunkt);

			Point door = null;
			for (Entry<Point, String> entry : keysLeftEgen.entrySet()) {
				if (entry.getValue().equals(key.toUpperCase())) {
//					System.out.println("hittade dörr " + entry.getValue());
					door = entry.getKey();
				}
			}
			System.out.println("tar bort key " + nyckel );
			keysLeftEgen.remove(egenPunkt);
			if (door != null) {
				keysLeftEgen.remove(door);
//				System.out.println("tar bort dörr");
			}
		}
//		System.out.println("NY INSTANS startar med size " + keysLeftEgen.size());
//		System.out.println("NY INSTANS startar med size " + egenPunkt);
//		print(spaceEgen,keysLeftEgen);
		
		// fyll ut space anropa om vi hittar nycklar
		spaceEgen.put(egenPunkt, 1);
		while (!full) {
			full = true;// nollställs om vi uppdaterat något
			cDistance++;
//			System.out.println("cdist " + cDistance);
			Point punkt;
			for (Entry<Point, Integer> entry : spaceEgen.entrySet()) {
				if (entry.getValue().equals(cDistance)) { // hittat det som skall expandera i 4 riktningar
					punkt = entry.getKey();
//					System.out.println("vi skall expandera här " + punkt);
					Point sidoPunkter[] = new Point[4];
					sidoPunkter[0] = new Point(punkt.x + 1, punkt.y);
					sidoPunkter[1] = new Point(punkt.x - 1, punkt.y);
					sidoPunkter[2] = new Point(punkt.x, punkt.y + 1);
					sidoPunkter[3] = new Point(punkt.x, punkt.y - 1);

					for (int i = 0; i < 4; i++) {
						Point sido = sidoPunkter[i];
						if (spaceEgen.get(sido) != null) {// är det ens i grottan?
//							System.out.println("expanderar i " + sido);
							// kolla om där är en nyckel eller dörr
							String maybe;
							maybe = keysLeftEgen.get(sido);
							if (maybe != null) {
								if (maybe.equals(maybe.toLowerCase())) { // key, för dörr ökar vi inte = stoppar
//									System.out.println("vi köar upp borttagning " + maybe + " dist" + cDistance);
									alternativeKeys.put(sido, cDistance);
									// incSpaceAfter.put(sido, cDistance + 1);// ökar här för att fortsätta rakt
									// igenom = fel
									// print(_space, _keysLeftEgen);
								}
							} else { // vi kan öka här
								if (spaceEgen.get(sido) == 0) {
									incSpaceAfter.put(sido, cDistance + 1);
									full = false;
								}
							}
						}
					}
				}
			}
			// uppdatera
			for (Entry<Point, Integer> entry : incSpaceAfter.entrySet()) {
				spaceEgen.put(entry.getKey(), entry.getValue());
			}
		}
		// anropa de val som finns
//		System.out.println("vi har " + alternativeKeys.size() + " rekursiva anrop att göra");
		for (Entry<Point, Integer> entry : alternativeKeys.entrySet()) {
//			System.out.println("!!anropar rekursivt " + entry.getKey() + " med distance " + entry.getValue());
			int tempdist = entry.getValue() + recGetKeys(entry.getKey(), keysLeftEgen);
			if (tempdist < distance) {
				System.out.println("better dist " + tempdist + " < " + distance);
				distance = tempdist;
			} else {
				System.out.println("same or worse dist " + tempdist + " < " + distance);				
			}
		}
		// System.out.println(keysLeftEgen);
		return distance;
	}
	
	

	public static void del2() {
		// readInput(16, 0);
	}

	void printKeys(HashMap<Point, String> keys) {
		String s = "";
		for (Entry<Point, String> entry : keys.entrySet()) {
			s += entry.getValue();
		}
		System.out.println(s);
	}

	void print(HashMap<Point, Integer> cave, HashMap<Point, String> keys) {
		String s = "", k = "";
		Integer c = 0;
		Scanner scan = new Scanner(System.in);

		int maxx = 0, maxy = 0;
		for (Point key : cave.keySet()) {
			maxx = Math.max(maxx, key.x);
			maxy = Math.max(maxy, key.y);
		}

		for (int y = 0; y <= maxy + 1; y++) {
			for (int x = 0; x <= maxx + 1; x++) {
				c = cave.get(new Point(x, y));
				k = keys.get(new Point(x, y));
				if (c == null) {
					s += "#";
				} else if (k == null) {
					s += ".";
				} else {
					s += k;
				}
			}
			System.out.println(s);
			s = "";
		}
		String text = scan.nextLine();
	}

	private void clearSpace() {
		for (Point key : _space.keySet()) {
			_space.put(key, 0);
		}
	}

	private static void readInput(int day, int suffix) {
		String str;
		int y = 0;
		_space.clear();
		_keysAndDoors.clear();
		FileInputStream in;
		try {
			in = new FileInputStream(
					"C:\\git\\AoC2019\\AoC_" + day + "\\src\\AoC_" + day + "\\input" + suffix + ".txt");
			final BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((str = br.readLine()) != null) {
				String tmp = "";
				for (int x = 0; x < str.length(); x++) {
					tmp = str.substring(x, x + 1);
					if (tmp.equals("#")) {
						// ignore walls
					} else if (tmp.equals(".")) {
						_space.put(new Point(x, y), 0);
					} else { // keys, doors or starting point
						_space.put(new Point(x, y), 0);
						_keysAndDoors.put(new Point(x, y), tmp);
					}
				}
				y++;
			}
			br.close();
		} catch (final IOException e) {
			System.out.println("Något sket sig vid inläsning");
		}
	}

}
