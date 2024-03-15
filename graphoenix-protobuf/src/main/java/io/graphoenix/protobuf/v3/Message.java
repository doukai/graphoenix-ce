package io.graphoenix.protobuf.v3;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.List;

public class Message {

    private String name;

    private List<Field> fields;

    private String description;

    private List<Option> options;

    public String getName() {
        return name;
    }

    public Message setName(String name) {
        this.name = name;
        return this;
    }

    public List<Field> getFields() {
        return fields;
    }

    public Message setFields(List<Field> fields) {
        this.fields = fields;
        return this;
    }

    public Message addField(Field field) {
        if (this.fields == null) {
            this.fields = new ArrayList<>();
        }
        this.fields.add(field);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Message setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<Option> getOptions() {
        return options;
    }

    public Message setOptions(List<Option> options) {
        this.options = options;
        return this;
    }

    @Override
    public String toString() {
        STGroupFile stGroupFile = new STGroupFile("stg/v3/Message.stg");
        ST st = stGroupFile.getInstanceOf("messageDefinition");
        st.add("message", this);
        String render = st.render();
        stGroupFile.unload();
        return render;
    }
}
