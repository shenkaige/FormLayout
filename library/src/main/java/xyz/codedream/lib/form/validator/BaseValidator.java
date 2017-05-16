package xyz.codedream.lib.form.validator;

import java.util.ArrayList;
import java.util.List;

import xyz.codedream.lib.form.Validator;

/**
 * 基础实现
 * 
 * @author skg
 *
 * @param <T>
 */
public abstract class BaseValidator<T> implements Validator<T> {
	private List<Validator<T>> subsequent;
	private String mErrorTemplate;
	private boolean mErroNeedFormat;

	@Override
	public String getErrorTemplate() {
		return mErrorTemplate;
	}

	@Override
	public boolean isTempalteNeedFormat() {
		return mErroNeedFormat;
	}

	@Override
	public void setErrorTemplate(String msg, boolean format) {
		mErrorTemplate = msg;
		mErroNeedFormat = format;
	}

	@Override
	public List<Validator<T>> getSubsequent() {
		return subsequent;
	}

	@Override
	public void addSubsequent(Validator<T> v) {
		if (subsequent == null) {
			subsequent = new ArrayList<Validator<T>>();
		}
		subsequent.add(v);
	}

	@Override
	public Validator<T> findByClass(Class<?> validatorClz) {
		if (validatorClz.isAssignableFrom(this.getClass())) {
			return this;
		} else if (subsequent != null && !subsequent.isEmpty()) {
			for (Validator<T> v : subsequent) {
				Validator<T> r = v.findByClass(validatorClz);
				if (r != null) {
					return r;
				}
			}
		}
		return null;
	}

	@Override
	public boolean removeValidator(RemoveValidatorResult<T> callback, Class<?> validatorClz) {
		throw new UnsupportedOperationException("unsuported method");
	}

}
