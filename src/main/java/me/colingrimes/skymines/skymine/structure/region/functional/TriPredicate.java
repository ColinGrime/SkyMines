package me.colingrimes.skymines.skymine.structure.region.functional;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface TriPredicate<A, B, C> {

	boolean test(@Nonnull A a, @Nonnull B b, @Nonnull C c);
}
