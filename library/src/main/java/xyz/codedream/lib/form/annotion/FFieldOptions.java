package xyz.codedream.lib.form.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import xyz.codedream.lib.form.FieldOptions;

@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FFieldOptions {
	public Class<? extends FieldOptions<?>> value();

	// public String[] showArray();
	//
	// public int[] intValues() default {};
	//
	// public float[] floatValues() default {};
	//
	// public boolean[] booleanValues() default {};
	//
	// public byte[] byteValues() default {};
	//
	// public double[] doubleValues() default {};
	//
	// public char[] charValues() default {};
	//
	// public long[] longValues() default {};
	//
	// public short[] shortValues() default {};
	//
	// public String[] stringValues() default {};
	//
	// public Class<?>[] classValues() default {};
	//
	// public boolean enumValues() default false;
}