package xyz.codedream.lib.form.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.view.View;

/**
 * 基础校验
 * 
 * @author skg
 *
 */
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface FFieldLimit {
	public String msg() default "";

	public int msgResId() default View.NO_ID;

	public boolean msgFormat() default false;

	public int[] length();

	public double[] values();
}
