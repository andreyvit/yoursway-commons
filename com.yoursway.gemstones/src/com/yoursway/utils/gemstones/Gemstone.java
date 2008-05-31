package com.yoursway.utils.gemstones;

public interface Gemstone<G extends Gemstone<G>> {
    
    <F extends Facelet<G>> F get(Slot<F> slot);
    
}
