package com.example.application.views.admin.enterform;

import com.example.application.data.entity.Patient;
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
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import org.aspectj.weaver.ast.Not;

import javax.persistence.criteria.CriteriaBuilder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PatientForm extends FormLayout {
    TextField surname= new TextField("Фамилия");
    TextField name= new TextField("Имя");
    TextField otchestvo  = new TextField("Отчество");
    TextField passport = new TextField("Паспорт");
    ComboBox<Integer> year = new ComboBox<Integer>("Год рождения");
    ComboBox<Integer> month = new ComboBox<Integer>("Месяц");
    ComboBox<Integer> day = new ComboBox<Integer>("День");

    public boolean isNullDoc() {
        return nullDoc;
    }

    TextField username = new TextField("Логин");
    TextField password = new TextField("Пароль");
    List<TextField> listT = new LinkedList<>();
    List<ComboBox> listC= new LinkedList<>();

    private User user;
    private Patient patient;
    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отмена");
    private boolean nullDoc = true;

    public PatientForm() {
        addClassName("patient-form");
        surname.setPattern("[А-Я]?[а-я]*");
        name.setPattern("[А-Я]?[а-я]*");
        otchestvo.setPattern("[А-Я]?[а-я]*");
        passport.setPattern("[0-9]?[0-9]?[0-9]?[0-9]?[\\s]?[0-9]?[0-9]?[0-9]?[0-9]?[0-9]?[0-9]?");
        passport.setValueChangeMode(ValueChangeMode.EAGER);
        passport.addValueChangeListener(e->{
           if(e.getValue().length() < 10){
               passport.setInvalid(true);
          //     Notification.show(e.getValue());
           }
           else{
               passport.setInvalid(false);
           }
        });
        name.setPreventInvalidInput(true);
        surname.setPreventInvalidInput(true);
        otchestvo.setPreventInvalidInput(true);
        passport.setPreventInvalidInput(true);
        year.setItems(data_years());
        year.addValueChangeListener(e->{
           if(year.getValue() != null){
               month.setEnabled(true);
           }
           else{
               month.setEnabled(false);
           }
        });
        month.setItems(data_month());
        month.addValueChangeListener(e->{

            if(month.getValue() != null){
                 day.setEnabled(true);
            }
            else{
                 day.setEnabled(false);
                 return;
            }
            if(month.getValue() == 1 || month.getValue() == 3 || month.getValue() == 5 || month.getValue() == 7 || month.getValue() == 8 || month.getValue() == 10 || month.getValue() == 12){
                Integer days[] = new Integer[31];
                for(int i = 1; i < 32; i++){
                    days[i-1] = i;
                }
                day.setItems(days);
                return;
            }
            else if(month.getValue() == 2){
                if(year.getValue() % 4 == 0){
                    Integer days[] = new Integer[29];
                    for(int i = 1; i < 30; i++){
                        days[i-1] = i;
                    }
                    day.setItems(days);
                    return;
                }
                else{
                    Integer days[] = new Integer[28];
                    for(int i = 1; i < 29; i++){
                        days[i-1] = i;
                    }
                    day.setItems(days);
                    return;
                }
            }
            else{
                Integer days[] = new Integer[30];
                for(int i = 1; i < 31; i++){
                    days[i-1] = i;
                }
                day.setItems(days);
                return;
            }

        });
        month.setEnabled(false);
        day.setEnabled(false);
        add(surname, name, otchestvo, year, month, day, passport, username, password,
                createButtonsLayout());
        Collections.addAll(listT, surname, name, otchestvo, passport, username, password);
        Collections.addAll(listC, year, month, day);

    }
    
    private Integer[] data_years(){
        Integer years[] = new Integer[105];
        for(int i = 1915; i < 2020; i++){
            years[i - 1915] = i;
        }
        return years;
    }

    public void setCancelHidden(){
        close.setVisible(false);
    }

    public void setRegister(){
        save.setText("Register");
        delete.setVisible(false);close.setVisible(false);
    }

    private Integer[] data_month(){
        Integer months[] = new Integer[12];
        for(int i = 1; i < 13; i++){
            months[i-1] = i;
        }
        return months;
    }


    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, patient)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));


    //    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
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

    public void setData(Patient patient){
        //username.clear();
        surname.setPlaceholder(patient.getSurname());
        name.setPlaceholder(patient.getName());
        otchestvo.setPlaceholder(patient.getOtchestvo());
        passport.setPlaceholder(patient.getPassport());
        Date date = patient.getBirthdate();
        DateFormat format = new SimpleDateFormat("yyyy");
        DateFormat format1 = new SimpleDateFormat("M");
        DateFormat format2 = new SimpleDateFormat("d");
        String sYear = format.format(date);
        String sMonth = format1.format(date);
        String sDay = format2.format(date);
        year.setPlaceholder(String.valueOf(Integer.parseInt(sYear)));
        month.setPlaceholder(String.valueOf(Integer.parseInt(sMonth)));
        day.setPlaceholder(String.valueOf(Integer.parseInt(sDay)));
        username.setPlaceholder(patient.getUser().getUsername());
        password.setPlaceholder("*******");
    }


    private void validateAndSave() {
        if (!nullDoc) {
            boolean check = false;
            Date date = this.patient.getBirthdate();
            DateFormat format = new SimpleDateFormat("yyyy");
            DateFormat format1 = new SimpleDateFormat("M");
            DateFormat format2 = new SimpleDateFormat("d");
            String sYear = format.format(date);
            String sDay = format2.format(date);
            String sMonth = format1.format(date);
            if (username.getValue().length() < 4 && username.getValue().length() > 0) {
                username.setInvalid(true);
                check = true;
            }
            if (password.getValue().length() < 4 && password.getValue().length() > 0) {
                password.setInvalid(true);
                check = true;
            }
            if (!username.getValue().isEmpty()) {
                this.patient.getUser().setUsername(username.getValue());
            }
            if (!password.getValue().isEmpty()) {
                this.patient.getUser().setPassword(password.getValue());
            }
            if (!surname.getValue().isEmpty()) {
                this.patient.setSurname(surname.getValue());
            }
            if (!name.getValue().isEmpty()) {
                this.patient.setName(name.getValue());
            }
            if (!otchestvo.getValue().isEmpty()) {
                this.patient.setOtchestvo(otchestvo.getValue());
            }
            
            if (year.getValue() != null) {
                sYear = year.getValue().toString();
            }
            if (month.getValue() != null) {
                sMonth = month.getValue().toString();
            }
            if (day.getValue() != null) {
                sDay = day.getValue().toString();
            }
            String newDate = sYear + " " + sMonth + " " + sDay;
            DateFormat formatDate = new SimpleDateFormat("yyyy M d");
            try {
                date = formatDate.parse(newDate);
            } catch (ParseException e) {
                Notification.show("Wrong date");
                return;
            }
            this.patient.setBirthdate(date);
            if (!passport.getValue().isEmpty() && passport.getValue().length() == 10) {
                     this.patient.setPassport(passport.getValue());
            }
            if (check) {
                return;
            }
            fireEvent(new SaveEvent(this, this.patient));
           // Notification.show("Hello " + name.getValue());
        } else {
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
            if (username.getValue().length() < 4 && username.getValue().length() > 0) {
                username.setInvalid(true);
                return;
            }
            if (password.getValue().length() < 4 && password.getValue().length() > 0) {
                password.setInvalid(true);
                return;
            }
            this.patient.getUser().setUsername(username.getValue());
            this.patient.getUser().setPassword(password.getValue());
            this.patient.setSurname(surname.getValue());
            this.patient.setName(name.getValue());
            this.patient.setOtchestvo(otchestvo.getValue());
            this.patient.setPassport(passport.getValue());
            String str = year.getValue().toString() + " " + month.getValue().toString() + " " + day.getValue().toString();
            DateFormat format = new SimpleDateFormat("yyyy M d");
            Date date;
            try {
                date = format.parse(str);
            } catch (ParseException e) {
                return;
            }
            this.patient.setBirthdate(date);
            fireEvent(new PatientForm.SaveEvent(this, this.patient));
          //  Notification.show("Hello " + name.getValue());
        }
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public static abstract class PatientFormEvent extends ComponentEvent<PatientForm> {
        private Patient Patient;
        protected PatientFormEvent(PatientForm source, Patient Patient) {
            super(source, false);
            this.Patient = Patient;
        }



        public Patient getPatient() {
            return Patient;
        }
    }

    public static class SaveEvent extends PatientFormEvent {
        SaveEvent(PatientForm source, Patient Patient) {
            super(source, Patient);
        }
    }

    public static class DeleteEvent extends PatientFormEvent {
        DeleteEvent(PatientForm source, Patient Patient) {
            super(source, Patient);
        }

    }

    public static class CloseEvent extends PatientFormEvent {
        CloseEvent(PatientForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
