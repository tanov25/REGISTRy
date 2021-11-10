package com.example.application.views.admin.enterform;

import com.example.application.data.entity.MedService;
import com.example.application.data.entity.MedService;
import com.example.application.data.entity.User;
import com.example.application.data.entity.enums.Speciality;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ServiceForm extends FormLayout {
    TextField name= new TextField("Название");
    TextField price= new TextField("Цена");
    List<TextField> listT = new LinkedList<>();
    List<ComboBox> listC= new LinkedList<>();

    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отмена");
    private boolean nullDoc = true;
    private MedService medService;

    public ServiceForm() {
        addClassName("medService-form");
        price.setPattern("[1-9]?[0-9]*");
        add( name, price,
                createButtonsLayout());
        Collections.addAll(listT, name, price);
        Collections.addAll(listC);
    }


    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, medService)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        return new HorizontalLayout(save, delete, close);
    }

    public void setNullDoc(boolean set){
        nullDoc = set; delete.setVisible(!set);
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

    public void setData(MedService medService){
        name.setPlaceholder(medService.getName());
        price.setPlaceholder(Long.toString(medService.getPrice()));
    }

    private void validateAndSave() {
        if(!nullDoc) {
            if(!name.getValue().isEmpty())this.medService.setName(name.getValue());
            if(!price.getValue().isEmpty())this.medService.setPrice(Long.parseLong(price.getValue()));
            fireEvent(new SaveEvent(this, this.medService));
        }
        else{
            boolean check = false;
            for(TextField t: listT){
              if(t.getValue().isEmpty()) check = true;
            }
            for(ComboBox t: listC){
                if(t.getValue() == null) check = true;
            }
            if(check){
                for(TextField t: listT){
                    if(t.getValue().isEmpty()) t.setInvalid(true);
                }
                for(ComboBox t: listC){
                    if(t.getValue() == null) t.setInvalid(true);
                }
                return;
            }
            this.medService.setName(name.getValue());
            this.medService.setPrice(Long.parseLong(price.getValue()));
            fireEvent(new SaveEvent(this, this.medService));
        }
    }

    public void setMedService(MedService medService) {
        this.medService = medService;
    }

    public static abstract class MedServiceFormEvent extends ComponentEvent<ServiceForm> {
        private MedService MedService;
        private User user;
        protected MedServiceFormEvent(ServiceForm source, MedService MedService) {
            super(source, false);
            this.MedService = MedService;
        }

        public MedService getMedService() {
            return MedService;
        }
    }

    public static class SaveEvent extends MedServiceFormEvent {
        SaveEvent(ServiceForm source, MedService MedService) {
            super(source, MedService);
        }
    }
    public static class DeleteEvent extends MedServiceFormEvent {
        DeleteEvent(ServiceForm source, MedService MedService) {
            super(source, MedService);
        }

    }
    public static class CloseEvent extends MedServiceFormEvent {
        CloseEvent(ServiceForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
