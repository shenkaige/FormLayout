package xyz.codedream.lib.form;

import java.lang.reflect.Field;
import java.util.List;

import xyz.codedream.lib.form.validator.BaseValidator;
import xyz.codedream.lib.form.validator.ExpressionValidator;
import xyz.codedream.lib.form.validator.NotNullValidator;

import android.content.Context;

/**
 * 基础实现
 * 
 * @author skg
 *
 * @param <Result>
 */
public abstract class AbstractFormField<Result> implements FormField<Result> {

	private final int mId;
	private String mTitle;
	private Validator<Result> mValidator;
	private FormLayout mForm;

	public AbstractFormField(int id) {
		mId = id;
	}

	@Override
	public int getId() {
		return mId;
	}

	protected void setTitle(String title) {
		mTitle = title;
	}

	@Override
	public String getTitle() {
		return mTitle;
	}

	private Field mField;

	@Override
	public void bindField(Field field) {
		mField = field;
	}

	@Override
	public Field getBindField() {
		return mField;
	}

	private FieldOptions<Result> mFieldOptions;

	@Override
	public void setFieldOptions(FieldOptions<Result> opt) {
		mFieldOptions = opt;
	}

	@Override
	public FieldOptions<Result> getFieldOptions() {
		return mFieldOptions;
	}

	@Override
	public AbstractFormField<Result> setNullable(boolean nullable) {
		if (nullable) {
			if (mValidator != null) {
				if (mValidator instanceof NotNullValidator) {
					List<Validator<Result>> sub = mValidator.getSubsequent();
					if (sub == null || sub.isEmpty()) {
						mValidator = null;
					} else {
						// FIXME
						mValidator = new BaseValidator<Result>() {
							@Override
							public boolean validate(Result result) {
								return true;
							}

							@Override
							public String getErrorMessage(Context context, String fieldTitle) {
								return null;
							}
						};
						for (Validator<Result> v : sub) {
							mValidator.addSubsequent(v);
						}
					}
				}
			}
		} else {
			// FIXME
		}
		return this;
	}

	@Override
	public boolean isNullable() {
		if (mValidator == null) {
			return true;
		}
		return mValidator.findByClass(NotNullValidator.class) == null;
	}

	@Override
	public AbstractFormField<Result> setValidator(String expression, String errorMessage) {
		ExpressionValidator<Result> v = new ExpressionValidator<Result>();
		v.setErrorTemplate(errorMessage, false);
		mValidator = v;
		return this;
	}

	@Override
	public AbstractFormField<Result> setValidator(Validator<Result> validator) {
		mValidator = validator;
		return this;
	}

	@Override
	public boolean validateResult() {
		final String title = getTitle();
		if (mValidator == null) {
			return true;
		}
		return validate(title, getResult(), mValidator);
	}

	private boolean validate(String title, Result result, Validator<Result> v) {
		if (v.validate(result)) {
			List<Validator<Result>> sub = v.getSubsequent();
			if (sub == null || sub.isEmpty()) {
				return true;
			} else {
				for (Validator<Result> nv : sub) {
					boolean r = validate(title, result, nv);
					if (!r) {
						mForm.tipsError(nv.getErrorMessage(mForm.getContext(), title));
						return false;
					}
				}
				return true;
			}
		} else {
			mForm.tipsError(v.getErrorMessage(mForm.getContext(), title));
			return false;
		}
	}

	@Override
	public void onFormAttached(FormLayout form) {
		mForm = form;
	}

	@Override
	public FormLayout getForm() {
		return mForm;
	}

	public boolean isEmptyString(Result result) {
		if (result instanceof String) {
			String str = result.toString();
			str = str.trim();
			return str.length() <= 0;
		} else {
			return false;
		}
	}

}
