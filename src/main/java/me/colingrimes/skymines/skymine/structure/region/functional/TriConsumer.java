package me.colingrimes.skymines.skymine.structure.region.functional;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface TriConsumer<A, B, C> {

    void accept(@Nonnull A a, @Nonnull B b, @Nonnull C c);
}
