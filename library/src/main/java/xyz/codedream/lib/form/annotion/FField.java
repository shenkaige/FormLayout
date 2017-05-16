package xyz.codedream.lib.form.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import xyz.codedream.lib.form.FormField.PasswordStyle;

import android.view.View;

@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FField {
	/**
	 * 字段的id表示，并会用作字段排序
	 * 
	 * @return
	 */
	public int id();

	public int titleId();

	public FieldType type() default FieldType.EDIT;

	/**
	 * 仅仅是{@code FieldType#EDIT}有效
	 * 
	 * @return
	 */
	public boolean hintStyle() default false;

	/**
	 * 仅仅是{@code FieldType#EDIT}有效
	 * 
	 * @return
	 */
	public PasswordStyle pwdStyle() default PasswordStyle.NONE;

	/**
	 * 目前只有Edit支持
	 * 
	 * @return
	 */
	public String endUnit() default "";

	public boolean notNull() default true;

	public String nullMsg() default NULL_MSG_EMPTY;

	public int nullMsgId() default NULL_MSG_ID_EMPTY;

	public static final int NULL_MSG_ID_EMPTY = View.NO_ID;

	public static final String NULL_MSG_EMPTY = "";

	public static enum FieldType {
		EDIT, SPINNER, MONEY, TITLE, DIVIDER
	}
}