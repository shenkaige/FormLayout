package xyz.codedream.lib.form.field;

import xyz.codedream.lib.form.BaseViewFormField;
import xyz.codedream.lib.form.FormField.JustShowField;
import xyz.codedream.lib.form.R;

import android.content.Context;

/**
 * Title Field
 * 
 * @author skg
 *
 */
public class FormFieldTitle extends BaseViewFormField<String> implements JustShowField {

	public FormFieldTitle(int id, String title) {
		super(id);
		setTitle(title);
	}

	@Override
	protected void onCreateView(Context context) {
		setContentView(R.layout.field_title);
	}

	@Override
	public String getResult() {
		return "";
	}

	@Override
	public FormFieldTitle resetResult() {
		return this;
	}

	@Override
	public void setDefaultValue(String value) {
	}

}
