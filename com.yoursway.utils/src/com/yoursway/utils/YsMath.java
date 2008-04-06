package com.yoursway.utils;

public class YsMath {
    
    public static boolean eq(double a, double b, double eps) {
        return Math.abs(a - b) <= eps;
    }
    
    public static boolean neq(double a, double b, double eps) {
        return Math.abs(a - b) > eps;
    }
    
    public static boolean lt(double a, double b, double eps) {
        return a < b - eps;
    }
    
    public static boolean leq(double a, double b, double eps) {
        return a <= b + eps;
    }
    
    public static boolean gt(double a, double b, double eps) {
        return a - eps > b;
    }
    
    public static boolean gte(double a, double b, double eps) {
        return a + eps >= b;
    }
    
}
