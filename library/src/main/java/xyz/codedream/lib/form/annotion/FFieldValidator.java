package xyz.codedream.lib.form.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import xyz.codedream.lib.form.Validator;

import android.content.Context;

@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FFieldValidator {
	public Class<? extends Validator<?>> value();

	public String errorTemplate() default "";

	public boolean format() default false;

	public static class EmptyValidator implements Validator<Object> {

		@Override
		public void setErrorTemplate(String msg, boolean format) {
		}

		@Override
		public List<Validator<Object>> getSubsequent() {
			return null;
		}

		@Override
		public void addSubsequent(Validator<Object> v) {
		}

		@Override
		public boolean validate(Object result) {
			return true;
		}

		@Override
		public String getErrorMessage(Context context, String fieldTitle) {
			return "";
		}

		@Override
		public String getErrorTemplate() {
			return null;
		}

		@Override
		public boolean isTempalteNeedFormat() {
			return false;
		}

		@Override
		public Validator<Object> findByClass(Class<?> validatorClz) {
			return null;
		}

		@Override
		public boolean removeValidator(RemoveValidatorResult<Object> callback, Class<?> validatorClz) {
			return false;
		}
	}
}