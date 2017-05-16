package xyz.codedream.lib.form.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.view.View;

/**
 * 长度校验，只支持String
 * 
 * @author skg
 *
 */
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FFieldLength {
	public String msg() default "";

	public int msgResId() default View.NO_ID;

	public boolean msgFormat() default false;

	public int[] range();
}
