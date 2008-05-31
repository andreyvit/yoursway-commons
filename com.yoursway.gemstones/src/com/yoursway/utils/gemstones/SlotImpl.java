package com.yoursway.utils.gemstones;

public class SlotImpl<F extends Facelet<G>, G extends Gemstone<G>> implements Slot<F> {
    
    private final int index;
    private final String description;
    private final FaceletFactory<F, G> factory;

    public SlotImpl(int index, String description, FaceletFactory<F, G> factory) {
        if (description == null)
            throw new NullPointerException("description is null");
        if (factory == null)
            throw new NullPointerException("factory is null");
        this.index = index;
        this.description = description;
        this.factory = factory;
    }
    
    int index() {
        return index;
    }
    
    @Override
    public String toString() {
        return "#" + index + " " + description;
    }
    
    public F create(G gemstone) {
        return factory.create(gemstone);
    }
    
}
