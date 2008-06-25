package com.yoursway.swt.additions;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Control;

public class FormDataBuilder {
    
    private FormData data;
    
    public FormDataBuilder() {
        this.data = new FormData();
    }
    
    public FormDataBuilder(FormData data) {
        this.data = data;
    }
    
    public FormData create() {
        return data;
    }
    
    public FormDataBuilder width(int width) {
        data.width = width;
        return this;
    }
    
    public FormDataBuilder height(int height) {
        data.height = height;
        return this;
    }
    
    public FormDataBuilder size(Point size) {
        size(size.x, size.y);
        return this;
    }
    
    public FormDataBuilder size(int width, int height) {
        data.width = width;
        data.height = height;
        return this;
    }
    
    public FormDataBuilder left(int numerator) {
        data.left = new FormAttachment(numerator);
        return this;
    }
    
    public FormDataBuilder left(int numerator, int offset) {
        data.left = new FormAttachment(numerator, offset);
        return this;
    }
    
    public FormDataBuilder left(int numerator, int denominator, int offset) {
        data.left = new FormAttachment(numerator, denominator, offset);
        return this;
    }
    
    public FormDataBuilder left(Control control) {
        data.left = new FormAttachment(control);
        return this;
    }
    
    public FormDataBuilder left(Control control, int offset) {
        data.left = new FormAttachment(control, offset);
        return this;
    }
    
    public FormDataBuilder left(Control control, int offset, int alignment) {
        data.left = new FormAttachment(control, offset, alignment);
        return this;
    }
    
    public FormDataBuilder right(int numerator) {
        data.right = new FormAttachment(numerator);
        return this;
    }
    
    public FormDataBuilder right(int numerator, int offset) {
        data.right = new FormAttachment(numerator, offset);
        return this;
    }
    
    public FormDataBuilder right(int numerator, int denominator, int offset) {
        data.right = new FormAttachment(numerator, denominator, offset);
        return this;
    }
    
    public FormDataBuilder right(Control control) {
        data.right = new FormAttachment(control);
        return this;
    }
    
    public FormDataBuilder right(Control control, int offset) {
        data.right = new FormAttachment(control, offset);
        return this;
    }
    
    public FormDataBuilder right(Control control, int offset, int alignment) {
        data.right = new FormAttachment(control, offset, alignment);
        return this;
    }
    
    public FormDataBuilder top(int numerator) {
        data.top = new FormAttachment(numerator);
        return this;
    }
    
    public FormDataBuilder top(int numerator, int offset) {
        data.top = new FormAttachment(numerator, offset);
        return this;
    }
    
    public FormDataBuilder top(int numerator, int denominator, int offset) {
        data.top = new FormAttachment(numerator, denominator, offset);
        return this;
    }
    
    public FormDataBuilder top(Control control) {
        data.top = new FormAttachment(control);
        return this;
    }
    
    public FormDataBuilder top(Control control, int offset) {
        data.top = new FormAttachment(control, offset);
        return this;
    }
    
    public FormDataBuilder top(Control control, int offset, int alignment) {
        data.top = new FormAttachment(control, offset, alignment);
        return this;
    }
    
    public FormDataBuilder bottom(int numerator) {
        data.bottom = new FormAttachment(numerator);
        return this;
    }
    
    public FormDataBuilder bottom(int numerator, int offset) {
        data.bottom = new FormAttachment(numerator, offset);
        return this;
    }
    
    public FormDataBuilder bottom(int numerator, int denominator, int offset) {
        data.bottom = new FormAttachment(numerator, denominator, offset);
        return this;
    }
    
    public FormDataBuilder bottom(Control control) {
        data.bottom = new FormAttachment(control);
        return this;
    }
    
    public FormDataBuilder bottom(Control control, int offset) {
        data.bottom = new FormAttachment(control, offset);
        return this;
    }
    
    public FormDataBuilder bottom(Control control, int offset, int alignment) {
        data.bottom = new FormAttachment(control, offset, alignment);
        return this;
    }
    
    public static FormDataBuilder formData() {
        return new FormDataBuilder();
    }
    
    public static FormDataBuilder formDataOf(Control control) {
        FormData data = (FormData) control.getLayoutData();
        if (data == null) {
            // a safeguard for those who will try using formDataOf in a non-standard way 
            data = new FormData();
            control.setLayoutData(data);
        }
        return new FormDataBuilder(data);
    }
    
}
