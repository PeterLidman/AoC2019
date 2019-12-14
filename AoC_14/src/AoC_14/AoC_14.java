package AoC_14;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.util.Pair;

public class AoC_14 {
	static HashMap<String, Long> _quantities = new HashMap<>();
	static HashMap<String, List<Pair<String, Long>>> _reactions = new HashMap<>();
	static HashMap<String, Long> _bonus = new HashMap<>();
	static Long _oreForOneFuel;

	public static void main(final String[] args) {
		final AoC_14 a = new AoC_14();
		a.run();
	}

	public void run() {
		readInput(14, 0);
		del1();
		del2();
	}

	public void del2() {
		// ide dela med resultatet från del 1, men det blir lite waste över hela tiden
		// som kan ge mer fuel
		Long ore = 1_000_000_000_000L;
		Long mult = ore / _oreForOneFuel;
		// leta binärt mellan mult och mult*2
		long hi = mult * 2;
		long lo = mult;
//		System.out.println(getOrefromFuel(hi));
//		System.out.println(getOrefromFuel(lo));
		long mid = 0;
		while (lo < hi) {
			mid = (lo + hi + 1) / 2;
			Long orefromFuel = getOrefromFuel(mid);
//			System.out.println(orefromFuel + " with hi" + hi + " lo " + lo + " mid " + mid);
			if (orefromFuel <= ore) {
				lo = mid;
			} else {
				hi = mid - 1;
			}
		}
		System.out.println("del 2: " + lo);
	}

	public void del1() {
		System.out.println("Del 1: " + getOrefromFuel(1L));
	}

	private Long getOrefromFuel(Long inFuel) {
		String consume;
		Long mul = 0L;
//		System.out.println(_reactions);
//		System.out.println(_bonus);
		_quantities.clear();
		_quantities.put("FUEL", inFuel);
		HashMap<String, Long> tmpQuantities;

		// arbeta med_quantities tills det bara är ORE kvar
		while (!(consume = getFirstChemicalToProduce()).equals("")) {
			tmpQuantities = new HashMap<>();
			mul = _quantities.get(consume) / _bonus.get(consume);
			if (mul == 0) {// skapar waste
				mul = 1L;
			}
			_quantities.put(consume, _quantities.get(consume) - _bonus.get(consume) * mul);
			for (Pair<String, Long> p : _reactions.get(consume)) {
				tmpQuantities.put(p.getKey(), p.getValue() * mul);
			}
			// merge HashMaps
			for (Map.Entry<String, Long> me : tmpQuantities.entrySet()) {
				if (_quantities.get(me.getKey()) == null) { // new quantity
					_quantities.put(me.getKey(), 0L);
				}
				_quantities.put(me.getKey(), me.getValue() + _quantities.get(me.getKey()));
			}
		}
		_oreForOneFuel = _quantities.get("ORE").longValue();
		return _oreForOneFuel;
	}

	private String getFirstChemicalToProduce() {
		for (Map.Entry<String, Long> me : _quantities.entrySet()) {
			if (me.getValue() > 0 && !me.getKey().equals("ORE")) {
				return me.getKey();
			}
		}
		return "";
	}

	private static void readInput(int day, int suffix) {
		String str, splitStr[], splitStr2[];
		List<Pair<String, Long>> ret = new ArrayList<>();
		Pair<String, Long> tmp;
		FileInputStream in;
		try {
			in = new FileInputStream(
					"C:\\git\\AoC2019\\AoC_" + day + "\\src\\AoC_" + day + "\\input" + suffix + ".txt");
			final BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((str = br.readLine()) != null) {
				ret = new ArrayList<>();
				splitStr = str.split("=>");
				tmp = makePair(splitStr[1].trim());
				_bonus.put(tmp.getKey(), tmp.getValue());
				splitStr2 = splitStr[0].split(",");
				for (String s : splitStr2) {
					ret.add(makePair(s.trim()));
				}
				_reactions.put(tmp.getKey(), ret);
			}
			br.close();
		} catch (final IOException e) {
			System.out.println("Något sket sig vid inläsning");
		}
	}

	private static Pair<String, Long> makePair(String a) {
		String s[];
		s = a.split(" ");
		return new Pair<String, Long>(s[1], Long.valueOf(s[0]));
	}

}
