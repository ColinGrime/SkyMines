package com.github.colingrime.storage.sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class SchemaReader {

	public static List<String> getQueries(InputStream is) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			String query = reader.lines().collect(Collectors.joining("\n"));
			return List.of(query.split(";"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
	}

	private SchemaReader() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
