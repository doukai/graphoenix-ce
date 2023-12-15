package io.graphoenix.spi.graphql.common;

import jakarta.json.JsonString;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import javax.lang.model.element.AnnotationValue;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static io.graphoenix.spi.utils.DocumentUtil.getStringValue;

public class StringValue implements ValueWithVariable, JsonString {
    private final STGroupFile stGroupFile = new STGroupFile("stg/common/StringValue.stg");

    private String value;

    public StringValue(AnnotationValue value) {
        this.value = (String) value.getValue();
    }

    public StringValue(Character value) {
        this.value = value.toString();
    }

    public StringValue(String value) {
        this.value = value;
    }

    public StringValue(LocalDate localDate) {
        this.value = localDate.toString();
    }

    public StringValue(LocalTime localTime) {
        this.value = localTime.toString();
    }

    public StringValue(LocalDateTime localDateTime) {
        this.value = localDateTime.toString();
    }

    public StringValue(TerminalNode value) {
        this.value = getStringValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getString() {
        return value;
    }

    @Override
    public CharSequence getChars() {
        return value;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.STRING;
    }

    @Override
    public boolean isString() {
        return true;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("stringValueDefinition");
        st.add("stringValue", this);
        return st.render();
    }
}