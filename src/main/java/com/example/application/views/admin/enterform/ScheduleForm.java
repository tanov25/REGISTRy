package com.example.application.views.admin.enterform;


import com.example.application.data.entity.Schedule;
import com.example.application.data.entity.Patient;
import com.example.application.data.entity.Schedule;
import com.example.application.data.entity.User;
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

import java.util.List;

public class ScheduleForm extends FormLayout {

    private Schedule schedule;
    ComboBox<Patient> patients = new ComboBox<>();
    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отмена");
    public ScheduleForm(List<Patient> pats) {
        addClassName("schedule-form");
        add(patients, createButtonsLayout());
        patients.setItems(pats);
        patients.setItemLabelGenerator(Patient::toString);
    }


    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, schedule)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        return new HorizontalLayout(save, delete, close);
    }


    public void clear(){
       patients.clear();
    }

    public void setData(Schedule schedule){
       patients.setPlaceholder(schedule.getPatient().toString());
    }


    private void validateAndSave() {
        if(patients.getValue() != null) {
            this.schedule.setPatient(patients.getValue());
            fireEvent(new SaveEvent(this, this.schedule));
            //   Notification.show("Hello " + name.getValue());
        }
        else{
            return;
        }
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public static abstract class ScheduleFormEvent extends ComponentEvent<ScheduleForm> {
        private Schedule Schedule;
        protected ScheduleFormEvent(ScheduleForm source, Schedule Schedule) {
            super(source, false);
            this.Schedule = Schedule;
        }

        public Schedule getSchedule() {
            return Schedule;
        }
    }

    public static class SaveEvent extends ScheduleFormEvent {
        SaveEvent(ScheduleForm source, Schedule Schedule) {
            super(source, Schedule);
        }
    }
    public static class DeleteEvent extends ScheduleFormEvent {
        DeleteEvent(ScheduleForm source, Schedule Schedule) {
            super(source, Schedule);
        }

    }
    public static class CloseEvent extends ScheduleFormEvent {
        CloseEvent(ScheduleForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
