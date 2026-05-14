package com.fuelprice.util;

import java.util.ArrayList;
import java.util.List;

public final class CsvUtils {
	private CsvUtils() {
	}

	public static List<String> split(String line) {
		List<String> result = new ArrayList<>();
		StringBuilder current = new StringBuilder();
		boolean inQuotes = false;
		char separator = detectSeparator(line);

		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);

			if (c == '"') {
				if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
					current.append('"');
					i++;
				} else {
					inQuotes = !inQuotes;
				}
			} else if (c == separator && !inQuotes) {
				result.add(clean(current.toString()));
				current.setLength(0);
			} else {
				current.append(c);
			}
		}

		result.add(clean(current.toString()));
		return result;
	}

	private static char detectSeparator(String line) {
		long pipes = line.chars().filter(c -> c == '|').count();
		long semicolons = line.chars().filter(c -> c == ';').count();
		long commas = line.chars().filter(c -> c == ',').count();

		if (pipes >= semicolons && pipes >= commas) {
			return '|';
		}

		if (semicolons >= commas) {
			return ';';
		}

		return ',';
	}

	private static String clean(String value) {
		return value == null ? null : value.trim().replace("\uFEFF", "");
	}
}
