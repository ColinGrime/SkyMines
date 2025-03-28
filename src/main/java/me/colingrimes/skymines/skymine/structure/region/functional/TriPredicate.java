package me.colingrimes.skymines.skymine.structure.region.functional;

@FunctionalInterface
public interface TriPredicate<A, B, C> {

	boolean test(A a, B b, C c);
}
