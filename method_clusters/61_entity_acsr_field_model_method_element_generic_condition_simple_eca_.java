public EntityEcaSetField(Element set) {
        this.fieldName = set.getAttribute("field-name");
        this.envName = set.getAttribute("env-name");
        this.value = set.getAttribute("value");
        this.format = set.getAttribute("format");
    }
--------------------

private EntityFindOptions getOptions(NameSpace ns, String name) throws ListBuilderException, UtilEvalError {
        Object obj = getVariable(ns, name);
        if (obj == null) {
            return DISTINCT_READ_OPTIONS;
        }
        if (obj instanceof EntityFindOptions) {
            return (EntityFindOptions) obj;
        }
        throw new ListBuilderException(ERR + "Field '" + name + "' must be an EntityFindOption.  I was passed " + obj.getClass().getName() + ".");
    }
--------------------

public ValidateMethodCondition(Element element) {
        this.mapAcsr = new ContextAccessor<Map<String, ? extends Object>>(element.getAttribute("map-name"));
        this.fieldAcsr = new ContextAccessor<Object>(element.getAttribute("field-name"));
        this.methodName = element.getAttribute("method");
        this.className = element.getAttribute("class");
    }
--------------------

