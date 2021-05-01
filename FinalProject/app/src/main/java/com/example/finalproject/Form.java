package com.example.finalproject;

public class Form extends Component {
    private final int form;

    public Form(String name, int form, boolean available) {
        super(name, available);
        this.form = form;
    }

    public Form(Form form){
        super(form.getName(), form.isAvailable());
        this.form=form.form;
    }

    public int getForm() {
        return form;
    }
}

