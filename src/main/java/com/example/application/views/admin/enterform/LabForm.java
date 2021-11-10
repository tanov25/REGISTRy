package com.example.application.views.admin.enterform;

import com.example.application.data.entity.Lab;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LabForm extends FormLayout {
    TextField name= new TextField("Название");
    ComboBox<Integer> opened = new ComboBox<>();
    ComboBox<Integer> closed = new ComboBox<>();
    List<TextField> listT = new LinkedList<>();
    List<ComboBox> listC= new LinkedList<>();
    private Lab lab;
    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отмена");
    private boolean nullDoc = true;

    public LabForm() {
        addClassName("lab-form");
        add(name, opened, closed,
                createButtonsLayout());
        Collections.addAll(listT, name);
        Collections.addAll(listC, opened, closed);
        opened.setItems(fillBoxes());
        closed.setItems(fillBoxes());
    }

    private List<Integer> fillBoxes(){
        int start = 10;
        List<Integer> list = new LinkedList<>();
        for(int i = 1; i < 11; i++){
            list.add(start);
            start++;
        }
        return list;
    }


    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, lab)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        return new HorizontalLayout(save, delete, close);
    }


    public void clear(){
        for(TextField t: listT){
            t.clear();
            t.setPlaceholder("");
            t.setInvalid(false);
        }

        for(ComboBox t: listC){
            t.clear();
            t.setInvalid(false);
        }
    }

    public void setData(Lab lab){
        name.setPlaceholder(lab.getName());
        opened.setValue(lab.getOpen());
        closed.setValue(lab.getClose());
    }

    public void setNullDoc(boolean set){
        nullDoc = set; delete.setVisible(!set);
    }


    private void validateAndSave() {
        if(!nullDoc) {
            if (!name.getValue().isEmpty()) {
                this.lab.setName(name.getValue());
            }
            if (opened.getValue() != null) {
                this.lab.setOpen(opened.getValue());
            }
            if (closed.getValue() != null) {
                this.lab.setClose(closed.getValue());
            }
            if(this.lab.getClose() < this.lab.getOpen()){
                return;

            }


            fireEvent(new SaveEvent(this, this.lab));
        }
        else{
            boolean check = false;
            for (TextField t : listT) {
                if (t.getValue().isEmpty()) check = true;
            }
            for (ComboBox t : listC) {
                if (t.getValue() == null) check = true;
            }
            if (check) {
                for (TextField t : listT) {
                    if (t.getValue().isEmpty()) t.setInvalid(true);
                }
                for (ComboBox t : listC) {
                    if (t.getValue() == null) t.setInvalid(true);
                }
                return;
            }
            this.lab.setName(name.getValue());
            this.lab.setOpen(opened.getValue());
            this.lab.setClose(closed.getValue());
            this.lab.setDays("пн-пт");
            if(this.lab.getClose() < this.lab.getOpen()){
                return;
            }
            fireEvent(new SaveEvent(this, this.lab));
        }
    }

    public void setLab(Lab lab) {
        this.lab = lab;
    }

    public static abstract class LabFormEvent extends ComponentEvent<LabForm> {
        private Lab Lab;
        protected LabFormEvent(LabForm source, Lab Lab) {
            super(source, false);
            this.Lab = Lab;
        }

        public Lab getLab() {
            return Lab;
        }
    }

    public static class SaveEvent extends LabFormEvent {
        SaveEvent(LabForm source, Lab Lab) {
            super(source, Lab);
        }
    }
    public static class DeleteEvent extends LabFormEvent {
        DeleteEvent(LabForm source, Lab Lab) {
            super(source, Lab);
        }

    }
    public static class CloseEvent extends LabFormEvent {
        CloseEvent(LabForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
