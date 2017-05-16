package xyz.codedream.lib.form.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.text.InputType;

@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FFieldInput {
	public String accept() default "";

	public static final int TYPE_CLASS_NUMBER = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
	public static final int TYPE_CLASS_PHONE = InputType.TYPE_CLASS_PHONE;

	/**
	 * {@link InputType}
	 * 
	 * @return
	 */
	public int type() default InputType.TYPE_NULL;
}
