package AoC_18;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

public class AoC_18 {
	static HashMap<Point, Integer> _space = new HashMap<>();
	static HashMap<Point, String> _keysAndDoors = new HashMap<>();
	static HashMap<String, String> _neighbors = new HashMap<>();
	static HashMap<String, Integer> _keyDistances = new HashMap<>();
	static Integer _bestDistance = Integer.MAX_VALUE;
	static Integer _numberOfKeys = 0;
	private Graph _graf;

	public static void main(final String[] args) {
		final AoC_18 a = new AoC_18();
		a.run();
	}

	public void run() {
		// del1();
//		del1b();
		del1c();
	}

	public void del1c() {
		readInput(18, 4);
//		System.out.println(_space);
//		System.out.println(_keysAndDoors);
//		System.out.println("nycklar&dörrar: " + printKeys(_keysAndDoors));
		print(_space, _keysAndDoors);
//		Point start = null;
//		for (Entry<Point, String> entry : _keysAndDoors.entrySet()) {
//			if (entry.getValue().equals("@")) {
//				start = entry.getKey();
//			}
//		}
//		System.out.println(start);
		getNeighbors();
//		System.out.println("neighbors " + _neighbors);
		getDistances();
//		System.out.println(_keyDistances);
		_graf = new Graph();
		for (Entry<String, String> entry : _neighbors.entrySet()) { // skapa noder
			_graf.createNode(new Node(entry.getKey()));
		}
		int id = 1;
		for (Entry<String, String> entry : _neighbors.entrySet()) { // anslut dem
			for (int i = 0; i < entry.getValue().length(); i++) {
				String thisNode = entry.getKey();
				String otherNode = entry.getValue().substring(i, i + 1);
				int dist = _keyDistances.get(thisNode + otherNode);
				Node thisN = _graf.getNode(thisNode);
				Node otherN = _graf.getNode(otherNode);
				Edge e1 = new Edge(thisN, otherN, dist, id++);
				// Edge e2 = new Edge(otherN, thisN , dist , id++);
				thisN.addNeighbour(e1);
				// otherN.addNeighbour(e2);
			}
		}
//		System.out.println(_neighbors.get("@"));
//		System.out.println(graf.getNumberOfNodes());

		// gör istället en rekursiv sökning BFS

//		String visitedNodes = "";
//		String res = "";
//		res = graf.getNode("@").getUnvisitedNeighbours(visitedNodes);
//		System.out.println("svar: " + res);
//		visitedNodes += "@";
//
//		String res2 = "";
//		for (int i = 0; i < res.length(); i++) {
//			res2 += graf.getNode(res.substring(i, i + 1)).getUnvisitedNeighbours(visitedNodes);
//		}
//		
//		System.out.println("svar: " + res2);
//		
//		visitedNodes += res2;
//		
//		
////		String clean = cleanStringFromString("@", res2);
////		System.out.println("clean svar: " + clean);
//		String undup = cleanStringFromDublicates(res2);
//		
//		String res3 = "";
//		for (int i = 0; i < res2.length(); i++) {
//			res3 += graf.getNode(res2.substring(i, i + 1)).getUnvisitedNeighbours(visitedNodes);
//		}
//		
//		String undup2 = cleanStringFromDublicates(res3);
//		
//		
//		System.out.println("unduplicate svar: " + undup2);
//		System.out.println("just nodes svar: " + cleanStringFromVersals(undup2));

		System.out.println("Del 1: " + rec2("@", 0));
	}

	private int rec2(String path, int bestDist) {
		
		String currentNode = path.substring(path.length() - 1, path.length());
		String visit = "";

		for (int i = 0; i < path.length(); i++) {
			visit += _graf.getNode(path.substring(i, i + 1)).getNeighbours(true);
		}

//		System.out.println(visit);
		visit = cleanStringFromString(path, visit);
//		System.out.println(visit);

		visit = cleanStringFromDublicates(visit);
//		System.out.println(visit);


		int bestDistFromThisNode = Integer.MAX_VALUE;

		for (int i = 0; i < visit.length(); i++) {
			
			String n = visit.substring(i, i + 1);
//			System.out.println("curr " + currentNode + " n " + n);
			int dist = _keyDistances.get(currentNode + n);
//			System.out.println("dist: " + dist);
			
			bestDistFromThisNode = Math.min(bestDistFromThisNode, rec2(path + n, dist));
		}
		return bestDistFromThisNode;
	}

	public void del1b() {
		readInput(18, 0);
//		System.out.println(_space);
//		System.out.println(_keysAndDoors);
//		System.out.println("nycklar&dörrar: " + printKeys(_keysAndDoors));
//		print(_space, _keysAndDoors);
		Point start = null;
		for (Entry<Point, String> entry : _keysAndDoors.entrySet()) {
			if (entry.getValue().equals("@")) {
				start = entry.getKey();
			}
		}
//		System.out.println(start);
		getNeighbors();
//		System.out.println("neighbors " + _neighbors);
		getDistances();
//		System.out.println("key dist " + _keyDistances);
		String keysAndDoors = printKeys(_keysAndDoors);
		for (int i = 0; i < keysAndDoors.length(); i++) {
			if (keysAndDoors.substring(i, i + 1).equals(keysAndDoors.substring(i, i + 1).toLowerCase())) {
				_numberOfKeys++;
			}
		}
		System.out.println("no keys: " + _numberOfKeys);
		System.out.println("Del 1: " + rec("@", "", 0));
	}

	private String cleanStringFromString(String a, String b) {
		String ret = "";
		for (int i = 0; i < b.length(); i++) {
			String letter = b.substring(i, i + 1);
			if (!a.contains(letter)) {
				ret += letter;
			}
		}
		return ret;
	}

	private String cleanStringFromDublicates(String a) {
		String ret = "";
		for (int i = 0; i < a.length(); i++) {
			String letter = a.substring(i, i + 1);
			if (!ret.contains(letter)) {
				ret += letter;
			}
		}
		return ret;
	}

	private String cleanStringFromVersals(String a) {
		String ret = "";
		for (int i = 0; i < a.length(); i++) {
			String letter = a.substring(i, i + 1);
			if (letter.equals(letter.toLowerCase())) {
				ret += letter;
			}
		}
		return ret;
	}

	private int rec(String key, String visitedKeys, int dist) {
		Set<String> nowVisible = new HashSet<>();
		int insight = 0;
		if (dist >= _bestDistance) {// om vi kommit hit och dist är > bestDistance, avbryt direkt, BFS
//			System.out.println("cut tree at dist: " + dist + " best: " + _bestDistance);
			return Integer.MAX_VALUE;
		}
		String outVisitedKeys = visitedKeys + key;
		if (outVisitedKeys.length() == _numberOfKeys) {// sista nyckeln
			if (dist < _bestDistance) { // bättre
				_bestDistance = dist;
			}
		}
		// bygg upp vilka nästa tänkbara nycklar är
		// slå ihop alla neighbors från besökta
		// och ta bort besökta keys, och dubletter
		// expandera borttagna dörrar, dvs dörrar i urvalet ovan som ingår i
		// visitedKeys, fortsätt tills inga fler dörrar kan expanderas
		for (int i = 0; i < outVisitedKeys.length(); i++) {
			String grannar = _neighbors.get(outVisitedKeys.substring(i, i + 1));
			for (int j = 0; j < grannar.length(); j++) {
				nowVisible.add(grannar.substring(j, j + 1));
			}
		}
		do {
			insight = nowVisible.size();
			for (int i = 0; i < outVisitedKeys.length(); i++) {
				String granne = outVisitedKeys.substring(i, i + 1);
				if (nowVisible.contains(granne.toUpperCase())) { // vi ser en dörr som öppnats
					String grann = _neighbors.get(granne.toUpperCase()); // vad ser dörren
					boolean found = false;
					for (int j = 0; j < grann.length(); j++) {
						if (outVisitedKeys.contains(grann.substring(j, j + 1))) {
							found = true;
						}
					}
					if (found) {
						for (int j = 0; j < grann.length(); j++) {
							if (!outVisitedKeys.contains(grann.substring(j, j + 1))) {
								nowVisible.add(grann.substring(j, j + 1));
							}
						}
					}
				}
			}
		} while (insight != nowVisible.size());
		// ta bort besökta nycklar
		for (int i = 0; i < outVisitedKeys.length(); i++) {
			nowVisible.remove(outVisitedKeys.substring(i, i + 1));
		}
		// ta bort dörrar som tänkbara kandidater på nycklar
		nowVisible.removeIf(s -> s.equals(s.toUpperCase()));

//		System.out.println("ser: " +nowVisible);

		// sort and call shortest
		int returnShortest = Integer.MAX_VALUE;
		while (nowVisible.size() > 0) {
			int shortest = Integer.MAX_VALUE;
			String keyNext = "";
			for (String s : nowVisible) {
//				System.out.println("key " + key + " s " + s);
				int distance = _keyDistances.get(key + s);
				if (shortest > distance) {
					keyNext = s;
					shortest = distance;
				}
			}
			nowVisible.remove(keyNext);
			returnShortest = Math.min(returnShortest, rec(keyNext, outVisitedKeys, dist + shortest));
		}
		return returnShortest;
	}

	private void getDistances() {
		for (Entry<Point, String> entry : _keysAndDoors.entrySet()) {
//			if (entry.getValue().equals(entry.getValue().toLowerCase())) { // ingen dörr
			boolean full = false;
			int dist = 0;
			_space.put(entry.getKey(), 1);
			while (!full) {
				full = true;// nollställs om vi uppdaterat något
				dist++;
				Point punkt;
				for (Entry<Point, Integer> ent : _space.entrySet()) {// flooding
					if (ent.getValue().equals(dist)) { // hittat det som skall expandera i 4 riktningar
						punkt = ent.getKey();
						Point sidoPunkter[] = new Point[4];
						sidoPunkter[0] = new Point(punkt.x + 1, punkt.y);
						sidoPunkter[1] = new Point(punkt.x - 1, punkt.y);
						sidoPunkter[2] = new Point(punkt.x, punkt.y + 1);
						sidoPunkter[3] = new Point(punkt.x, punkt.y - 1);
						for (int i = 0; i < 4; i++) {
							Point sido = sidoPunkter[i];
							if (_space.get(sido) != null) {// är det ens i grottan?
								String maybe;
								maybe = _keysAndDoors.get(sido);
								if (maybe != null) { // här ligger det visst en nyckel eller dörr
//									if (maybe.equals(maybe.toLowerCase())) { // key
										if (!maybe.equals(entry.getValue())) {
											if (_keyDistances.get(entry.getValue() + maybe) == null) {// första
																										// träffen
												_keyDistances.put(entry.getValue() + maybe, dist);
												_keyDistances.put(maybe + entry.getValue(), dist);
											}
										}
//									}
								}
								if (_space.get(sido) == 0) {
									_space.put(sido, dist + 1);
									full = false;
								}
							}
						}
					}
				}
			}
//			}
//			print(_space, _keysAndDoors);
			for (Entry<Point, Integer> e : _space.entrySet()) {// rensa
				_space.put(e.getKey(), 0);
			}
		}
	}

	private void getNeighbors() {
		for (Entry<Point, String> entry : _keysAndDoors.entrySet()) {
			boolean full = false;
			int dist = 0;
			String see = "";
			_space.put(entry.getKey(), 1);
			while (!full) {
				full = true;// nollställs om vi uppdaterat något
				dist++;
				Point punkt;
				for (Entry<Point, Integer> ent : _space.entrySet()) {// flooding
					if (ent.getValue().equals(dist)) { // hittat det som skall expandera i 4 riktningar
						punkt = ent.getKey();
						Point sidoPunkter[] = new Point[4];
						sidoPunkter[0] = new Point(punkt.x + 1, punkt.y);
						sidoPunkter[1] = new Point(punkt.x - 1, punkt.y);
						sidoPunkter[2] = new Point(punkt.x, punkt.y + 1);
						sidoPunkter[3] = new Point(punkt.x, punkt.y - 1);
						for (int i = 0; i < 4; i++) {
							Point sido = sidoPunkter[i];
							if (_space.get(sido) != null) {// är det ens i grottan?
								String maybe;
								maybe = _keysAndDoors.get(sido);
								if (maybe != null) { // här ligger det visst en nyckel eller dörr
									if (!maybe.equals(entry.getValue())) {// ej sig själv
										see += maybe;

//												if(_neighborskeyDistances.get(entry.getValue() + maybe) == null) {//första träffen
//													_keyDistances.put(entry.getValue() + maybe, dist);
//													_keyDistances.put(maybe + entry.getValue(), dist);
//												}
									}
								} else {
									if (_space.get(sido) == 0) {
										_space.put(sido, dist + 1);
										full = false;
									}
								}
							}
						}
					}
				}
			}
			_neighbors.put(entry.getValue(), see);
//			print(_space, _keysAndDoors);
			for (Entry<Point, Integer> e : _space.entrySet()) {// rensa
				_space.put(e.getKey(), 0);
			}
		}

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
			keysLeftEgen.put(new Point(entry.getKey().x, entry.getKey().y), new String(entry.getValue()));
		}
		for (Entry<Point, Integer> entry : _space.entrySet()) {
			spaceEgen.put(new Point(entry.getKey().x, entry.getKey().y), new Integer(entry.getValue()));
		}

		System.out.println(printKeys(keysLeftEgen));
		print(spaceEgen, keysLeftEgen);

		String key;
		int distance = Integer.MAX_VALUE / 2;
		int cDistance = 0;
		boolean full = false;

		// remove key and open door at point from (if key is start only remove key)
		key = keysLeftEgen.get(egenPunkt);
		if (keysLeftEgen.size() == 1) { // om det är sista nyckeln return 0
//			System.out.println("sista nyckeln");
//			String text = scan.nextLine();
			return 0;
		}
		if (key == null) {
//			System.out.println("ingen key at " + egenPunkt);
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
//			System.out.println("tar bort key " + nyckel);
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
		System.out.println("klart med expansionen");
		print(spaceEgen, keysLeftEgen);
		// anropa de val som finns
		System.out.println("vi har " + alternativeKeys.size() + " rekursiva anrop att göra");
		for (Entry<Point, Integer> entry : alternativeKeys.entrySet()) {
//			System.out.println("!!anropar rekursivt " + entry.getKey() + " med distance " + entry.getValue());
			int tempdist = entry.getValue() + recGetKeys(entry.getKey(), keysLeftEgen);
			if (tempdist < distance) {
//				System.out.println("better dist " + tempdist + " < " + distance);
				distance = tempdist;
			} else {
//				System.out.println("same or worse dist " + tempdist + " < " + distance);
			}
		}
		System.out.println("kortaste dist efter rek anropen " + distance);
		System.out.println(keysLeftEgen);
		return distance;
	}

	public static void del2() {
		// readInput(16, 0);
	}

	private String printKeys(HashMap<Point, String> keys) {
		String s = "";
		for (Entry<Point, String> entry : keys.entrySet()) {
			s += entry.getValue();
		}
		return s;
	}

	void print(HashMap<Point, Integer> cave, HashMap<Point, String> keys) {
		String s = "", k = "";
		Integer c = 0;
//		Scanner scan = new Scanner(System.in);

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
					s += "" + (c % 10);
					// s += ".";
				} else {
					s += k;
				}
			}
			System.out.println(s);
			s = "";
		}
//		String text = scan.nextLine();
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
