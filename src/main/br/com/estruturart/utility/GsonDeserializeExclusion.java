package br.com.estruturart.utility;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import java.text.NumberFormat;

public class GsonDeserializeExclusion implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getDeclaredClass() == NumberFormat.class;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) { return false; }
}