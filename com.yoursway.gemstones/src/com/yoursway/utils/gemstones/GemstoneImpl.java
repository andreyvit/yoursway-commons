package com.yoursway.utils.gemstones;

public class GemstoneImpl<G extends Gemstone<G>> implements Gemstone<G> {
    
    private final Facelet<G>[] facelets;
    
    @SuppressWarnings("unchecked")
    public GemstoneImpl(GemstoneDefinition<G> definition) {
        facelets = definition.create((G) this);
        for (int i = 0; i < facelets.length; i++)
            facelets[i].initializeFacelet();
    }
    
    @SuppressWarnings("unchecked")
    public final <F extends Facelet<G>> F get(Slot<F> slot) {
        return (F) facelets[((SlotImpl<F, G>) slot).index()];
    }
    
}
