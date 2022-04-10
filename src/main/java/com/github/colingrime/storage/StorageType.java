package com.github.colingrime.storage;

public enum StorageType {

	YAML("YAML"),
	MYSQL("MySQL");
	// TODO add more storage types

	private final String name;

	StorageType(String name) {
		this.name = name;
	}

	public static StorageType parse(String name) {
		for (StorageType type : StorageType.values()) {
			if (type.getName().equalsIgnoreCase(name)) {
				return type;
			}
		}

		// default storage type
		return StorageType.YAML;
	}

	public String getName() {
		return name;
	}
}
