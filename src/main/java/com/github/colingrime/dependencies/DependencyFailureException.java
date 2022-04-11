package com.github.colingrime.dependencies;

public class DependencyFailureException extends Exception {

	public DependencyFailureException(String dependencyName) {
		super(dependencyName + " dependency has failed to load. Plugin will now be disabled...");
	}
}
