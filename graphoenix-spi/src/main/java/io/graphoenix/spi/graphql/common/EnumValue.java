package io.graphoenix.spi.graphql.common;

import com.google.common.base.CaseFormat;
import graphql.parser.antlr.GraphqlParser;
import jakarta.json.JsonString;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import javax.lang.model.element.AnnotationValue;

public class EnumValue implements ValueWithVariable, JsonString {

    private final STGroupFile stGroupFile = new STGroupFile("stg/common/EnumValue.stg");

    private String value;

    public EnumValue(String value) {
        this.value = value;
    }

    public EnumValue(AnnotationValue value) {
        this.value = value.getValue().toString();
    }

    public EnumValue(Enum<?> value) {
        this.value = value.name();
    }

    public EnumValue(GraphqlParser.EnumValueContext enumValueContext) {
        this.value = enumValueContext.getText();
    }

    public static EnumValue forName(String enumName) {
        return new EnumValue(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, enumName));
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
    public boolean isEnum() {
        return true;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("enumValueDefinition");
        st.add("enumValue", this);
        return st.render();
    }
}
