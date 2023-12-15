package io.graphoenix.spi.graphql.common;

import jakarta.json.JsonNumber;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import javax.lang.model.element.AnnotationValue;
import java.math.BigDecimal;
import java.math.BigInteger;

import static io.graphoenix.spi.utils.DocumentUtil.getFloatValue;

public class FloatValue implements ValueWithVariable, JsonNumber {

    private final STGroupFile stGroupFile = new STGroupFile("stg/common/FloatValue.stg");

    private Number value;

    public FloatValue(AnnotationValue value) {
        this.value = (Number) value.getValue();
    }

    public FloatValue(Number value) {
        this.value = value;
    }

    public FloatValue(TerminalNode value) {
        this.value = getFloatValue(value);
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    @Override
    public boolean isIntegral() {
        return false;
    }

    @Override
    public int intValue() {
        return value.intValue();
    }

    @Override
    public int intValueExact() {
        return value.intValue();
    }

    @Override
    public long longValue() {
        return value.longValue();
    }

    @Override
    public long longValueExact() {
        return value.longValue();
    }

    @Override
    public BigInteger bigIntegerValue() {
        return BigInteger.valueOf(value.longValue());
    }

    @Override
    public BigInteger bigIntegerValueExact() {
        return BigInteger.valueOf(value.longValue());
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return BigDecimal.valueOf(value.doubleValue());
    }

    @Override
    public ValueType getValueType() {
        return ValueType.NUMBER;
    }

    @Override
    public boolean isFloat() {
        return true;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("floatValueDefinition");
        st.add("floatValue", this);
        return st.render();
    }
}
