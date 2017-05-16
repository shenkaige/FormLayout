package xyz.codedream.lib.form;

import java.lang.reflect.Field;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * 筛选的类别
 * 
 * @author sky
 *
 */
public interface FormField<Result> {

	/**
	 * this field just show
	 * 
	 * @author skg
	 *
	 */
	public interface JustShowField {
	}

	public FormLayout getForm();

	public void bindField(Field field);

	public Field getBindField();

	public void setFieldOptions(FieldOptions<Result> opt);

	public FieldOptions<Result> getFieldOptions();

	public void onFormAttached(FormLayout form);

	/**
	 * 获取Id
	 * 
	 * @return
	 */
	public int getId();

	/**
	 * 获取选择类别名称
	 * 
	 * @return
	 */
	public String getTitle();

	/**
	 * 根据Index获取选择的结果
	 * 
	 * @return
	 */
	public Result getResult();

	public FormField<Result> resetResult();

	public View getShowView();

	public FormField<Result> setTitleColor(int color);

	public FormField<Result> setIcon(Drawable icon, int margin);

	public FormField<Result> setNullable(boolean nullable);

	public boolean isNullable();

	public FormField<Result> setValidator(String expression, String errorMessage);

	public FormField<Result> setValidator(Validator<Result> validator);

	public boolean validateResult();

	public void setDefaultValue(Result value);

	public enum FieldInputType {
		TEXT, NUMBER
	}

	public enum PasswordStyle {
		/** Not password */
		NONE,
		/** Visible password */
		VISIBLE,
		/** Hide password */
		HIDE
	}
}