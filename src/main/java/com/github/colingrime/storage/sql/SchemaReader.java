package com.github.colingrime.storage.sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class SchemaReader {

	private SchemaReader() {}

	public static List<String> getQueries(InputStream is) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			String query = reader.lines().collect(Collectors.joining("\n"));
			return Arrays.asList(query.split(";"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
	}
}
