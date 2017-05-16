package xyz.codedream.lib.form;

import java.util.List;

import android.content.Context;

/**
 * 表单验证
 * 
 * @author skg
 *
 * @param <Result>
 */
public interface Validator<Result> {

	public void setErrorTemplate(String msg, boolean format);

	public String getErrorTemplate();

	public boolean isTempalteNeedFormat();

	public boolean validate(Result result);

	public String getErrorMessage(Context context, String fieldTitle);

	public List<Validator<Result>> getSubsequent();

	public void addSubsequent(Validator<Result> v);

	public Validator<Result> findByClass(Class<?> validatorClz);

	public interface RemoveValidatorResult<T> {
		public void onRemoveSuccess(Class<?> validatorClz, Validator<T> removed, Validator<T> newRootValidator);

		public void onValidatorNotFound(Class<?> validatorClz);
	}

	public boolean removeValidator(RemoveValidatorResult<Result> callback, Class<?> validatorClz);
}