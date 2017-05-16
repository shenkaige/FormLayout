package xyz.codedream.lib.form.sample;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import xyz.codedream.lib.form.FieldOptions.EnumOptions;
import xyz.codedream.lib.form.FormField;
import xyz.codedream.lib.form.FormLayout;
import xyz.codedream.lib.form.FormLayout.ParseFieldFilter;
import xyz.codedream.lib.form.annotion.FField;
import xyz.codedream.lib.form.annotion.FField.FieldType;
import xyz.codedream.lib.form.annotion.FFieldInput;
import xyz.codedream.lib.form.annotion.FFieldLength;
import xyz.codedream.lib.form.annotion.FFieldNumberRange;
import xyz.codedream.lib.form.annotion.FFieldOptions;
import xyz.codedream.lib.form.annotion.Form;

@Form
public class MainActivity extends AppCompatActivity implements OnClickListener {
    @FField(id = 0, titleId = R.string.form_name)
    @FFieldLength(range = {2, 10})
    private String name;

    @FField(id = 1, titleId = R.string.form_gender, type = FieldType.SPINNER)
    @FFieldOptions(EnumOptions.class)
    private Gender gender;

    @FField(id = 2, titleId = R.string.form_job, notNull = false)
    @FFieldLength(range = {2, 20})
    private String job;

    @FField(id = 3, titleId = R.string.form_salary)
    @FFieldInput(type = FFieldInput.TYPE_CLASS_NUMBER)
    @FFieldNumberRange(range = {0.0f, 100000.0f})
    private float salary;

    public enum Gender {
        male, female;
    }

    private FormLayout mFormLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        mFormLayout = (FormLayout) findViewById(R.id.formlayout);
        mFormLayout.parseAndBindField(this, new ParseFieldFilter() {
            @Override
            public boolean isSkip(int id) {
                return false;
            }

            @Override
            public void onFieldCreated(int id, FormField<?> f) {
            }
        });
    }

    private void submit() {
        if (mFormLayout.validateForm()) {
            StringBuilder sb = new StringBuilder();
            sb.append("FormlayoutResult:\n\n");
            sb.append("Name  : ").append(name).append('\n');
            sb.append("Gender  : ").append(gender).append('\n');
            sb.append("Job  : ").append(job).append('\n');
            sb.append("Salary  : ").append(salary).append('\n');
            new AlertDialog.Builder(this)
                    .setMessage(sb.toString())
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                submit();
                break;
        }
    }


}
