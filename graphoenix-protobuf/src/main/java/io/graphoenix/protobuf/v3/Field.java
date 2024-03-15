package io.graphoenix.protobuf.v3;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.List;

public class Field {

    private Boolean optional;

    private Boolean repeated;

    private String name;

    private String type;

    private Integer number;

    private String description;

    private List<Option> options;

    public Boolean getOptional() {
        return optional;
    }

    public Field setOptional(Boolean optional) {
        this.optional = optional;
        if (optional) {
            this.setRepeated(false);
        }
        return this;
    }

    public Boolean getRepeated() {
        return repeated;
    }

    public Field setRepeated(Boolean repeated) {
        this.repeated = repeated;
        if (repeated) {
            this.setOptional(false);
        }
        return this;
    }

    public String getName() {
        return name;
    }

    public Field setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public Field setType(String type) {
        this.type = type;
        return this;
    }

    public Integer getNumber() {
        return number;
    }

    public Field setNumber(Integer number) {
        this.number = number;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Field setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<Option> getOptions() {
        return options;
    }

    public Field setOptions(List<Option> options) {
        this.options = options;
        return this;
    }

    @Override
    public String toString() {
        STGroupFile stGroupFile = new STGroupFile("stg/v3/Field.stg");
        ST st = stGroupFile.getInstanceOf("fieldDefinition");
        st.add("field", this);
        String render = st.render();
        stGroupFile.unload();
        return render;
    }
}
