package AoC_18;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AoC_18 {
	static HashMap<Point, Integer> _space = new HashMap<>();
	static int _middleX = 0;
	static int _middleY = 0;
	static String _lastRobotPosition[] = new String[4];

	static HashMap<Point, String> _keysAndDoors = new HashMap<>();
	static HashMap<String, String> _neighbors = new HashMap<>();

	static HashMap<String, Integer> _keyDistances = new HashMap<>();

	static HashMap<Set<String>, Set<String>> _memoizationReachableKeys = new HashMap<>();

	// static HashMap<Set<String>, Integer> _memoizationStateToDistance = new
	// HashMap<>();
	static HashMap<String, HashMap<Set<String>, Integer>> _memoizationCurrentKeyAndStateToDistance = new HashMap<>();

	static HashMap<String, String> _memoization = new HashMap<>();
	static Integer _bestDistance = Integer.MAX_VALUE;
	static Integer _numberOfKeys = 0;
	static Integer _numberOfMultipleKeys[] = new Integer[5];
	private Graph _graf;
	private String _keysToFind;
	private String _bestRoute = "";

	private Set<String> _takenKeys1 = new HashSet<>();
	private Set<String> _takenKeys2 = new HashSet<>();
	private Set<String> _takenKeys3 = new HashSet<>();
	private Set<String> _takenKeys4 = new HashSet<>();

	public static void main(final String[] args) {
		final AoC_18 a = new AoC_18();
		a.run();
	}

	public void run() {
//		del1();//YES!!
		del2();//YES!
	}

	public void del1() {
			readInput(18, 0);
			getNeighbors();
			getDistances();
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
			String keysAndDoors = printKeys(_keysAndDoors);
			_keysToFind = "";
			for (int i = 0; i < keysAndDoors.length(); i++) {
				if (keysAndDoors.substring(i, i + 1).equals(keysAndDoors.substring(i, i + 1).toLowerCase())) {
					_numberOfKeys++;
					_keysToFind += keysAndDoors.substring(i, i + 1);
				}
			}
			System.out.println("Number of keys: " + _numberOfKeys);
			System.out.println("all keys: " + _keysToFind);
			for (int i = 0; i < _keysToFind.length(); i++) { // init memo hashmap
				_memoizationCurrentKeyAndStateToDistance.put(_keysToFind.substring(i, i + 1),
						new HashMap<Set<String>, Integer>());
			}
	
	//		System.out.println("all keys: " + _keyDistances);
	//		System.out.println(_graf.getNode("G").getNeighbours(false));
	//		System.out.println("Dist GH : " + _keyDistances.get("GH"));
	//		System.out.println("Dist im : " + _keyDistances.get("im"));
	//		System.out.println("Dist lp : " + _keyDistances.get("lp"));
	//		Set<String> test = new HashSet<>();
	//		test.add("@");
	//		test.add("f");
	//		test.add("a");
	//		test.add("c");
	//		test.add("i");
	//		test.add("d");
	//		test.add("g");
	//		test.add("e");
	//		test.add("b");
	//		test.add("h");
	//		System.out.println("test " + test);
	//		System.out.println("memo " + getPossibleDestinationMemoization(test));
			System.out.println("nu då??" + rec3("@", 0));
		}

	public void del2() {
//		readInput(18, 9);// 6 7 8 9 // 8 går inte, men skiter i det, testet liknar inte input
		readInput(18, 0);// 40, 40
		_space.remove(new Point(40, 40));
		_space.remove(new Point(39, 40));
		_space.remove(new Point(41, 40));
		_space.remove(new Point(40, 39));
		_space.remove(new Point(40, 41));
		_keysAndDoors.remove(new Point(40, 40));// bort med @
		_keysAndDoors.put(new Point(41, 39), "1");
		_keysAndDoors.put(new Point(39, 39), "2");
		_keysAndDoors.put(new Point(39, 41), "3");
		_keysAndDoors.put(new Point(41, 41), "4");
		
		print(_space, _keysAndDoors);
		getNeighbors();
		System.out.println(_neighbors);
		getDistances();
		System.out.println(_keyDistances);
		String keysAndDoors = printKeys(_keysAndDoors);
		System.out.println(keysAndDoors);
		_keysToFind = "";
		for (int i = 0; i < keysAndDoors.length(); i++) {
			if (keysAndDoors.substring(i, i + 1).equals(keysAndDoors.substring(i, i + 1).toLowerCase())) {
				_numberOfKeys++;
//				_numberOfMultipleKeysKeys[]++;

				_keysToFind += keysAndDoors.substring(i, i + 1);
			}
		}
		System.out.println("Number of keys: " + _numberOfKeys);
		System.out.println("all keys: " + _keysToFind);

		for (int i = 0; i < _keysToFind.length(); i++) { // init memo hashmap
			_memoizationCurrentKeyAndStateToDistance.put(_keysToFind.substring(i, i + 1),
					new HashMap<Set<String>, Integer>());
		}
//		Point start = null;
//		for (Entry<Point, String> entry : _keysAndDoors.entrySet()) {
//			if (entry.getValue().equals("@")) {
//				start = entry.getKey();
//			}
//		}
//		System.out.println(start);

//		Set<String> test = new HashSet<>();
//		test.add("1");
//		test.add("2");
//		test.add("3");
//		test.add("4");
//		test.add("a");
//		System.out.println("test " + test);
//		System.out.println("memo " + getPossibleDestinationMemoization(test));

		Set<String> s = new HashSet<>();
		s.add("1");
		s.add("2");
		s.add("3");
		s.add("4");
		System.out.println("Del 2: " + rec4(s, "1234", 0));
	}

	private int rec4(Set<String> state, String currentRobotPosition, int bestDist) {
		System.out.println(
				"state= " + state + " currentRobotPosition= " + currentRobotPosition + " bestdist= " + bestDist);

		if (state.size() == _numberOfKeys) {
			System.out.println("DONE: dist: " + bestDist);
			return bestDist;
		}

		Integer bestDistFromThisNode;
		HashMap<Set<String>, Integer> hashMap = _memoizationCurrentKeyAndStateToDistance.get(currentRobotPosition);
		if (hashMap == null) {// post init
			_memoizationCurrentKeyAndStateToDistance.put(currentRobotPosition, new HashMap<Set<String>, Integer>());
		} else {
			bestDistFromThisNode = hashMap.get(state);
			if (bestDistFromThisNode != null) {
				System.out.println("I FOUND MEMO!");
				return bestDistFromThisNode + bestDist;
			}
		}
		bestDistFromThisNode = Integer.MAX_VALUE / 2;

		Set<String> visit = getPossibleDestinationMemoization(state);

		System.out.println("Tänkbara nycklar att besöka: " + visit);

		if (visit.isEmpty()) {
			if (state.size() < _numberOfKeys) {
				System.out.println("Error deadlocked maze! currentNode= " + currentRobotPosition);
				return Integer.MAX_VALUE / 2;
			}
			System.out.println("ETT LÖV!!");// dead code
			return bestDist;
		}

		for (String s : visit) {
			// välj key i currentRobotPosition som är i samma kvadrant som s
			// uppdatera currentRobotPosition och state innan rekursivt anrop

			int dist = 0;
			String newRobotPosition = "";
			int sKvadrant = getKvadrantFromNode(s);
			for (int i = 0; i < currentRobotPosition.length(); i++) {
				String fromKey = currentRobotPosition.substring(i, i + 1);
				if (sKvadrant == getKvadrantFromNode(fromKey)) {
					dist = _keyDistances.get(fromKey + s);
					newRobotPosition += s; // uppdatera med ny currentNode
				} else {
					newRobotPosition += fromKey;
				}
			}
			Set<String> newState = new HashSet<>(state);
			newState.add(s);

			System.out.println("Testar currentnode= " + newRobotPosition + " avstånd= " + dist);

			bestDistFromThisNode = Math.min(bestDistFromThisNode, rec4(newState, newRobotPosition, bestDist + dist));
		}
		System.out.println("Memo save: currentNodeIn= " + currentRobotPosition + " key= " + state + " value= "
				+ (bestDistFromThisNode - bestDist));
		_memoizationCurrentKeyAndStateToDistance.get(currentRobotPosition).put(state, bestDistFromThisNode - bestDist);
		return bestDistFromThisNode;
	}

	private Set<String> getSetFromString(String inStr) {
		Set<String> ret = new TreeSet<>();
		for (int i = 0; i < inStr.length(); i++) {
			ret.add(inStr.substring(i, i + 1));
		}
		return ret;
	}

	private Set<String> getRemainingStateFromPath(String path) {
		Set<String> ret = new TreeSet<>();
//		System.out.println(path);
		for (int i = 0; i < _keysToFind.length(); i++) {
			String tkn = _keysToFind.substring(i, i + 1);
			if (!path.contains(tkn)) {
				ret.add(tkn);
			}
		}
//		System.out.println(ret);
		return ret;
	}

	private static int getKvadrantFromNode(String node) {
		Point start = null;
		for (Entry<Point, String> entry : _keysAndDoors.entrySet()) {
			if (entry.getValue().equals(node)) {
				start = entry.getKey();
			}
		}
		System.out.println(start);
		if (start.y > _middleY) { // undre
			if (start.x > _middleX) {// högre
				return 4;
			} else { // vänstra
				return 3;
			}
		} else { // övre
			if (start.x > _middleX) {// högre
				return 1;
			} else {// vänstra
				return 0;
			}
		}
	}

	private int rec3(String path, int bestDist) {
		String currentNode = path.substring(path.length() - 1, path.length());
		String pathBefore = path.substring(0, path.length() - 1);
		Set<String> stateBefore = getSetFromString(pathBefore);

		Set<String> state = getSetFromString(path);
		if (state.size() == _numberOfKeys) {
			System.out.println("DONE: dist: " + bestDist);
			return bestDist;
		}
		Integer bestDistFromThisNode = _memoizationCurrentKeyAndStateToDistance.get(currentNode).get(stateBefore);
		if (bestDistFromThisNode != null) {
			System.out.println("I FOUND MEMO!");

			return bestDistFromThisNode + bestDist;
		}

		bestDistFromThisNode = Integer.MAX_VALUE / 2;
		Set<String> visit = getPossibleDestinationMemoization(state);

		System.out.println("visit: " + visit);

		if (visit.isEmpty()) {
			if (state.size() < _numberOfKeys) {
				System.out.println("IMPOSSIBLE ROUTE!");
				return Integer.MAX_VALUE / 2;
			}
			System.out.println("ETT LÖV!!");
			return bestDist;
		}

		for (String s : visit) {
			int dist = _keyDistances.get(currentNode + s);
			System.out.println("path " + path + " visiting " + s + " dist: " + dist + " bestdist " + bestDist);
			bestDistFromThisNode = Math.min(bestDistFromThisNode, rec3(path + s, bestDist + dist));
		}

		System.out.println(
				"I SAVE MEMO! key: " + path.length() + currentNode + " value " + (bestDistFromThisNode - bestDist));

		_memoizationCurrentKeyAndStateToDistance.get(currentNode).put(stateBefore, bestDistFromThisNode - bestDist);

		return bestDistFromThisNode;
	}

	private Set<String> getPossibleDestinationMemoization(Set<String> takenKeys) {
		Set<String> reach = _memoizationReachableKeys.get(takenKeys);
		if (reach != null) {
			return reach;
		}

		String openDoors = "";
		for (String key : takenKeys) {
			openDoors += key;
		}
		openDoors = openDoors.toUpperCase();
		final String finalOpenDoors = openDoors;

		reach = new HashSet<>();
		for (String key : takenKeys) {
			String n = _neighbors.get(key);
			for (int i = 0; i < n.length(); i++) {
				reach.add(n.substring(i, i + 1));
			}
		}
//		System.out.println("reach before " + reach);
		reach.removeIf((String element) -> { // remove closed doors
			return (!finalOpenDoors.contains(element) && element.equals(element.toUpperCase()));
		});
//		System.out.println("reach before " + reach);
//öppna dörrar, akta för cirkulär loop
		Set<String> previousReach;
		do {
			previousReach = new HashSet<>(reach);
			for (String key : previousReach) {
				if (openDoors.contains(key)) {// it's a door, and it's open
					String n = _neighbors.get(key); // open door sees
					for (int i = 0; i < n.length(); i++) {
						String tkn = n.substring(i, i + 1);
						// if (tkn.equals(key)) { // lägg inte till dörren vi öppnar igen
						reach.add(tkn);
						// }
					}
				}
			}
			// rensa ev stängda dörrar vi lagt till
			reach.removeIf((String element) -> { // remove closed doors
				return (!finalOpenDoors.contains(element) && element.equals(element.toUpperCase()));
			});
		} while (previousReach.size() != reach.size());
//		System.out.println("bef fin reach " + reach);
		for (String key : takenKeys) {// remove open doors and visited keys
			reach.remove(key.toUpperCase());
			reach.remove(key);
		}
//		System.out.println("final reach " + reach);
		// remember this crazy computation
		_memoizationReachableKeys.put(takenKeys, reach);
//		System.out.println("Memo size: " + _memoizationReachableKeys.size());
		return reach;
	}

	public static String sortString1(String inputString) {
		char tempArray[] = inputString.toCharArray();
		Arrays.sort(tempArray);
		return new String(tempArray);
	}

	public static String sortString2(String inputString) {
		Character tempArray[] = new Character[inputString.length()];
		for (int i = 0; i < inputString.length(); i++) {
			tempArray[i] = inputString.charAt(i);
		}
		Arrays.sort(tempArray, new Comparator<Character>() {
			@Override
			public int compare(Character c1, Character c2) {
				// ignoring case
				return Character.compare(Character.toLowerCase(c1), Character.toLowerCase(c2));
			}
		});
		StringBuilder sb = new StringBuilder(tempArray.length);
		for (Character c : tempArray)
			sb.append(c.charValue());
		return sb.toString();
	}

	public static String sortString3(String inputString) {
		return Stream.of(inputString.split("")).sorted().collect(Collectors.joining());
	}

	public void del1d() {
		readInput(18, 4);
		getNeighbors();
		getDistances();
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

		String keysAndDoors = printKeys(_keysAndDoors);
		String keysToFind = "";

		for (int i = 0; i < keysAndDoors.length(); i++) {
			if (keysAndDoors.substring(i, i + 1).equals(keysAndDoors.substring(i, i + 1).toLowerCase())) {
				_numberOfKeys++;
				keysToFind += keysAndDoors.substring(i, i + 1);
			}
		}
//		System.out.println("no keys: " + _numberOfKeys);
//		System.out.println("all keys: " + keysToFind);
		System.out.println("all keys: " + _keyDistances);

		System.out.println("Del 1: " + distanceToCollectKeys("@", keysToFind));

	}

	private int distanceToCollectKeys(String currentKey, String keys) {
		int result = Integer.MAX_VALUE / 2;
		if (keys.length() == 0) {
			return 0;
		}
		String visit = _neighbors.get(currentKey);
		visit = stringIntersection(visit, keys);
		for (int i = 0; i < visit.length(); i++) {
			String key = visit.substring(i, i + 1);
			int dist = _keyDistances.get(currentKey + key);

			int d = dist + distanceToCollectKeys(key, cleanStringFromString(key, keys));
			result = Math.min(result, d);
		}
		return result;
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

		int bestDistFromThisNode = Integer.MAX_VALUE / 2;

		for (int i = 0; i < visit.length(); i++) {

			String n = visit.substring(i, i + 1);
//			System.out.println("curr " + currentNode + " n " + n);
			int dist = _keyDistances.get(currentNode + n);
//			System.out.println("dist: " + dist);

			bestDistFromThisNode = Math.min(bestDistFromThisNode, rec2(path + n, bestDist + dist));
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

	private String stringIntersection(String a, String b) {
		String ret = "";
		for (int i = 0; i < b.length(); i++) {
			String letter = b.substring(i, i + 1);
			if (a.contains(letter)) {
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
					// s += "" + (c % 10);
					s += ".";
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
				_middleX = str.length() / 2;
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
			_middleY = y / 2;
			br.close();
		} catch (final IOException e) {
			System.out.println("Något sket sig vid inläsning");
		}
	}

}
