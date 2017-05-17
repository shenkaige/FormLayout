### 为什么开发FormLayout？

在Android中如果一个表单里有很多个表单项(FormField)，甚至是多个页面都有类似的表单的需求，那么这些表单布局编写起来是很繁重的，而且需求变动，难以维护。即使编写不是问题，FormField的逐条验证，逐条提取数据绑定，也是非常头疼和难以维护的。为了解决这些问题，开发了FormLayout组建。

### FormLayout设计要求
1，支持根据model属性，自动生成FormField UI。
2，支持表单项内容合法性自动校验。
3，支持表单项从UI到model的反向赋值。
4，支持自定义验证规则。
5，支持model属性默认值配置（直接给变量赋值，即可解析为默认值）

### FormLayout的实现思路
A.根据Mode生成UI
通过注解描述model字段属性，包括数据源类型，数据校验规则，默认值等，然后由FormLayout解析并绑定到FormField UI。
Form表单UI支持扩展，添加，需要从FormField继承或者FormField的子类继承。

B.数据验证支持
数据校验依赖于Validator链式校验借口，可以组成校验连，每个链节点支持多个子节点。
Validator借口支持默认错误信息，自定义错误信息，模版错误信息。

C.数据校验工作流程
当FormLayout调用validateForm();的时候，FormLayout会遍历FormField，并拿到FormField的约束规则，自动校验，如发现任何一个条件失败，则终止校验并通知失败。

D.数据反填到model
在解析model属性的时候会同时获取model属性的java.lang.reflect.Field对象，在有了Field和model以后，就可以把FormField的value反填到model了。

### 常用注解
1，@Form注解标识model对象。
2，@FField描述为model里面哪个属性作为表单项，并声明数据源类型。
3，@FFieldLength(range = {2, 10})//字符长度限制
4，@FFieldOptions(EnumOptions.class)//Spinner数据源
5，@FFieldInput(type = FFieldInput.TYPE_CLASS_NUMBER)//输入类型
6，@FFieldNumberRange(range = {0.0f, 100000.0f})//数据值范围


### 代码用例

```java
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
```
### 效果图
![image](https://github.com/shenkaige/FormLayout/blob/master/screenshot/validate.png)
![image](https://github.com/shenkaige/FormLayout/blob/master/screenshot/result.png)