package com.example.application.views.admin.enterform;

import com.example.application.data.entity.Doctor;
import com.example.application.data.entity.Schedule;
import com.example.application.data.entity.User;
import com.example.application.data.entity.enums.Role;
import com.example.application.data.entity.enums.Speciality;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.shared.Registration;
import com.vaadin.ui.MaskedTextField;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DoctorForm extends FormLayout {
    TextField surname= new TextField("Фамилия");
    TextField name= new TextField("Имя");
    TextField otchestvo  = new TextField("Отчество");
    ComboBox<Speciality> speciality = new ComboBox<Speciality>("Специальность");
    TextField experience = new TextField("Стаж");

    public boolean isNullDoc() {
        return nullDoc;
    }

    TextField username = new TextField("Логин");
    TextField password = new TextField("Пароль");
    List<TextField> listT = new LinkedList<>();
    List<ComboBox> listC= new LinkedList<>();
    private User user;
    private Doctor doctor;
    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отмена");
    private boolean nullDoc = true;


    public DoctorForm() {
        addClassName("doctor-form");
        surname.setPattern("[А-Я]?[а-я]*");
        name.setPattern("[А-Я]?[а-я]*");
        otchestvo.setPattern("[А-Я]?[а-я]*");
        experience.setPattern("[1-5]?[0-9]?");

        add(surname, name, otchestvo, speciality, experience, username, password,
                createButtonsLayout());
        name.setPreventInvalidInput(true);
        surname.setPreventInvalidInput(true);
        otchestvo.setPreventInvalidInput(true);
        experience.setPreventInvalidInput(true);
        Collections.addAll(listT, surname, name, otchestvo, experience, username, password);
        Collections.addAll(listC, speciality);
        speciality.setItems(Speciality.values());
    }


    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, doctor)));
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

    public void setData(Doctor doctor){
        surname.setPlaceholder(doctor.getSurname());
        name.setPlaceholder(doctor.getName());
        otchestvo.setPlaceholder(doctor.getOtchestvo());
        speciality.setValue(doctor.getSpeciality());
        experience.setPlaceholder(doctor.getExperience());
        username.setPlaceholder(doctor.getUser().getUsername());
        password.setPlaceholder("*******");
    }


    private void validateAndSave() {
        if(!nullDoc) {
            boolean check = false;
            if (username.getValue().length() < 4 && username.getValue().length() > 0) {
                username.setInvalid(true);
                check = true;
            }
            if (password.getValue().length() < 4 && password.getValue().length() > 0) {
                password.setInvalid(true);
                check = true;
            }
            if (!username.getValue().isEmpty()) {
                this.doctor.getUser().setUsername(username.getValue());
            }
            if (!password.getValue().isEmpty()) {
                this.doctor.getUser().setPassword(password.getValue());
            }
            if (!surname.getValue().isEmpty()) {
                this.doctor.setSurname(surname.getValue());
            }
            if (!name.getValue().isEmpty()) {
                this.doctor.setName(name.getValue());
            }
            if (!otchestvo.getValue().isEmpty()) {
                this.doctor.setOtchestvo(otchestvo.getValue());
            }
            if (speciality.getValue() != null) {
                this.doctor.setSpeciality(speciality.getValue());
            }
            if (!experience.getValue().isEmpty()) {
                this.doctor.setExperience(experience.getValue());
            }
            if(check){
                return;
            }
            fireEvent(new SaveEvent(this, this.doctor));
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
            if (username.getValue().length() < 4 && username.getValue().length() > 0) {
                username.setInvalid(true);
                return;
            }
            if (password.getValue().length() < 4 && password.getValue().length() > 0) {
                password.setInvalid(true);
               return;
            }
                this.doctor.getUser().setUsername(username.getValue());
                this.doctor.getUser().setPassword(password.getValue());
                this.doctor.setSurname(surname.getValue());
                this.doctor.setName(name.getValue());
                this.doctor.setOtchestvo(otchestvo.getValue());
                this.doctor.setSpeciality(speciality.getValue());
                this.doctor.setExperience(experience.getValue());

            fireEvent(new SaveEvent(this, this.doctor));
        }
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public static abstract class DoctorFormEvent extends ComponentEvent<DoctorForm> {
        private Doctor Doctor;
        private User user;
        protected DoctorFormEvent(DoctorForm source, Doctor Doctor) {
            super(source, false);
            this.Doctor = Doctor;
        }

        public Doctor getDoctor() {
            return Doctor;
        }
    }

    public static class SaveEvent extends DoctorFormEvent {
        SaveEvent(DoctorForm source, Doctor Doctor) {
            super(source, Doctor);
        }
    }
    public static class DeleteEvent extends DoctorFormEvent {
        DeleteEvent(DoctorForm source, Doctor Doctor) {
            super(source, Doctor);
        }

    }
    public static class CloseEvent extends DoctorFormEvent {
        CloseEvent(DoctorForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
