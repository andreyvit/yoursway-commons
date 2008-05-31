package com.yoursway.utils.gemstones;

public abstract class FaceletImpl<G extends Gemstone<G>> implements Facelet<G> {
    
    private final G gemstone;

    public FaceletImpl(G gemstone) {
        if (gemstone == null)
            throw new NullPointerException("gemstone is null");
        this.gemstone = gemstone;
    }

    public void initializeFacelet() {
    }

    public G gemstone() {
        return gemstone;
    }
    
}
