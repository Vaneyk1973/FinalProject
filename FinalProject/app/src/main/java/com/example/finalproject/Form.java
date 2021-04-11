package com.example.finalproject;

public class Form extends Component {
    private final int form;

    public Form(String name, int form) {
        super(name);
        this.form = form;
    }

    public int getForm() {
        return form;
    }
}

