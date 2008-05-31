package com.yoursway.utils.gemstones;

public interface Facelet<G extends Gemstone<G>> {
    
    G gemstone();
    
    void initializeFacelet();
    
}
