package io.graphoenix.spi.graphql.common;

import jakarta.json.JsonValue;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import javax.lang.model.element.AnnotationValue;

import static io.graphoenix.spi.utils.DocumentUtil.getBooleanValue;

public class BooleanValue implements ValueWithVariable, JsonValue {

    private final STGroupFile stGroupFile = new STGroupFile("stg/common/BooleanValue.stg");

    private Boolean value;

    public BooleanValue(AnnotationValue value) {
        this.value = (Boolean) value.getValue();
    }

    public BooleanValue(Boolean value) {
        this.value = value;
    }

    public BooleanValue(TerminalNode value) {
        this.value = getBooleanValue(value);
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public ValueType getValueType() {
        return value ? ValueType.TRUE : ValueType.FALSE;
    }

    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("booleanValueDefinition");
        st.add("booleanValue", this);
        return st.render();
    }
}
