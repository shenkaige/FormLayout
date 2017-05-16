package xyz.codedream.lib.form.field;

import xyz.codedream.lib.form.BaseViewFormField;
import xyz.codedream.lib.form.FormField.JustShowField;
import xyz.codedream.lib.form.R;

import android.content.Context;

/**
 * Divider Form field
 * 
 * @author skg
 *
 */
public class FormFieldDivider extends BaseViewFormField<String> implements JustShowField {

	public FormFieldDivider(int id) {
		super(id);
	}

	@Override
	protected void onCreateView(Context context) {
		setContentView(R.layout.field_divider);
	}

	@Override
	public String getResult() {
		return "";
	}

	@Override
	public FormFieldDivider resetResult() {
		return this;
	}

	@Override
	public void setDefaultValue(String value) {
	}

}
