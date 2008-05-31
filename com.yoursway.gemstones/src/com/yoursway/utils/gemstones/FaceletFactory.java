package com.yoursway.utils.gemstones;

public interface FaceletFactory<F extends Facelet<G>, G extends Gemstone<G>> {
    
    F create(G gemstone);
    
}
