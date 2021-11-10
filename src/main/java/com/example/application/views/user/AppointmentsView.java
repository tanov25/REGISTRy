package com.example.application.views.user;

import com.example.application.data.entity.enums.Role;
import com.example.application.data.service.*;
import com.example.application.views.admin.ScheduleView;
import com.example.application.views.admin.enterform.DoctorForm;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.example.application.views.main.MainView;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.flow.server.VaadinSession;


import com.example.application.data.entity.*;
import com.example.application.data.entity.Patient;
import com.example.application.data.entity.enums.Speciality;
import com.example.application.data.service.PatientService;
import com.example.application.data.service.PatientService;
import com.example.application.views.admin.enterform.PatientForm;
import com.example.application.views.admin.enterform.PatientForm;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.application.views.main.MainView;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.notification.Notification;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.print.Doc;
import javax.validation.ConstraintViolationException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Route(value ="Register")
@PageTitle("Register")
//@Route(layout = MainView.class)
@UIScope
public class AppointmentsView extends Div {
    private PatientService patientService;
    private UserService userService;
    private Patient patMain;
    private PatientForm form;
    private User user;
    VerticalLayout content;

    public AppointmentsView(PatientService patientService, UserService userService) {
        setId("patient-private-edit-view");
        this.patientService = patientService;
        this.userService = userService;
        VaadinSession session = VaadinSession.getCurrent();
        if(session == null){
            return;
        }
        user = session.getAttribute(User.class);
        if(user == null) {
            form = new PatientForm();
            form.setRegister();
            form.addListener(PatientForm.SaveEvent.class, this::savePatient);
            this.patMain = new Patient(new User());
            this.patMain.getUser().setRole(Role.USER);
        }
        else {
            this.patMain = patientService.findByUser(user);
        }
        if(user!=null && user.getRole() == Role.USER){
            form = new PatientForm();
            form.addListener(PatientForm.SaveEvent.class, this::savePatient);
            form.addListener(PatientForm.DeleteEvent.class, this::deletePatient);
        }

     //   form.addListener(PatientForm.CloseEvent.class, e-> new RouterLink("Schedule", ScheduleView.class));

        form.setMaxWidth("400px");
        form.setCancelHidden();
        content = new VerticalLayout(form);
        content.addClassName("content");
        content.setSizeFull();
        setSizeFull();
        add(content);
        if(user == null){
            content.add(new RouterLink("На главную", GlavView.class), new Paragraph(" "), new Paragraph(" "));
        }
       // this.getStyle().set("overflow", "hidden");
        editPatient(this.patMain);
    }


    public void editPatient(Patient patient) {
            form.clear();
            if(user!=null) {
                form.setNullDoc(false);
            }
            else{
                form.setNullDoc(true);
            }
            form.setPatient(patient);
            if(user != null) {
                form.setData(patient);
            }
            form.setVisible(true);
            addClassName("editing");

    }

    private void savePatient(PatientForm.SaveEvent event) {
        form.clear();
        try{
            userService.save(event.getPatient().getUser());
        }
        catch (DataIntegrityViolationException e1){
            Notification.show("Пользователь с таким никнеймом уже есть в базе данных ").setPosition(Notification.Position.MIDDLE);
            UI.getCurrent().getPage().reload();
            return;
        }
        try {
            patientService.save(event.getPatient());
            Notification.show("Сохранено");
        }
        catch (DataIntegrityViolationException e1){
            Notification.show("Пользователь с таким паспортом уже есть в базе данных ").setPosition(Notification.Position.MIDDLE);
            if(user == null) {
                userService.delete(event.getPatient().getUser());
            }
            UI.getCurrent().getPage().reload();
            return;
        }
        if(user == null){
            UI.getCurrent().navigate("Main");
            return;
        }
        this.patMain = event.getPatient();
        editPatient(this.patMain);

    }

    private void deletePatient(PatientForm.DeleteEvent event) {
        form.clear();
        form.setPatient(this.patMain);
        editPatient(this.patMain);
    }


}