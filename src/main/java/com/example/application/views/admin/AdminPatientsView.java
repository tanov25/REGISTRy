package com.example.application.views.admin;

import com.example.application.data.entity.Patient;
import com.example.application.data.entity.Schedule;
import com.example.application.data.entity.User;
import com.example.application.data.entity.enums.Role;
import com.example.application.data.service.PatientService;
import com.example.application.data.service.ScheduleService;
import com.example.application.data.service.UserService;
import com.example.application.views.admin.enterform.PatientForm;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.application.views.main.MainView;
import org.springframework.dao.DataIntegrityViolationException;
import com.vaadin.flow.component.UI;

import javax.validation.ConstraintViolationException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

//@Route(value = "Mainp")
@PageTitle("adminPatients")
public class AdminPatientsView extends Div {
    private PatientService patientService;
    private UserService userService;
    private ScheduleService scheduleService;
    private Grid<Patient> grid = new Grid<>(Patient.class);
    private PatientForm form;

    public AdminPatientsView(PatientService PatientService, UserService userService, ScheduleService scheduleService) {
        setId("patients-view");
        this.patientService = PatientService;
        this.userService = userService;
        this.scheduleService = scheduleService;
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        form = new PatientForm();
        form.addListener(PatientForm.SaveEvent.class, this::savePatient);
        form.addListener(PatientForm.DeleteEvent.class, this::deletePatient);
        form.addListener(PatientForm.CloseEvent.class, e -> closeEditor());
        form.setMaxWidth("400px");
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.addClassName("content");
        content.setSizeFull();
        setSizeFull();

        configureGrid();
        add(getToolbar(), content);
        updateList();
        closeEditor();
    }
    private void configureGrid() {
        grid.addClassName("Patient-grid");
        grid.setSizeFull();
        grid.setColumns("surname", "name", "otchestvo", "passport");
        grid.addColumn(patient -> {
            Date date = patient.getBirthdate();
            DateFormat formatDate = new SimpleDateFormat("yyyy M d");
            return date == null ? "-" : formatDate.format(date);
        });
        grid.addColumn(patient -> {
            User user = patient.getUser();
            return user == null ? "-" : user.getUsername();
        });
        grid.getColumns().get(0).setHeader("Фамилия");
        grid.getColumns().get(1).setHeader("Имя");
        grid.getColumns().get(2).setHeader("Отчество");
        grid.getColumns().get(3).setHeader("Пасспорт");
        grid.getColumns().get(4).setHeader("Дата рождения");
        grid.getColumns().get(5).setHeader("Логин");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editPatient(event.getValue())
        );
    }

    private HorizontalLayout getToolbar() {
        Button addPatientButton = new Button("Добавить пациента");
        addPatientButton.addClickListener(click -> {addPatient();});
        HorizontalLayout toolbar = new HorizontalLayout(addPatientButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    void addPatient() {
        grid.asSingleSelect().clear();
        Patient patient = new Patient();
        patient.setUser(new User());
        patient.getUser().setRole(Role.USER);
        editPatient(patient);
    }

    public void editPatient(Patient patient) {
        form.clear();
        if (patient == null) {
            closeEditor();
        }
        else {
            form.setPatient(patient);
            form.setVisible(true);
            if(patient.getName() == null){
                form.clear();
                form.setNullDoc(true);
            }
            else{
                if(patient.getName().equals("null")){
                    closeEditor();
                }
                form.setData(patient);
                form.setNullDoc(false);
            }
            addClassName("editing");
        }
    }


    private void savePatient(PatientForm.SaveEvent event) {
        try{
            userService.save(event.getPatient().getUser());
        }
        catch (DataIntegrityViolationException e1){
                editPatient(event.getPatient());
                Notification.show("Пользователь с таким никнеймом уже есть в базе данных ").setPosition(Notification.Position.MIDDLE);
            closeEditor();
            UI.getCurrent().getPage().reload();
                return;
        }
        try {
            patientService.save(event.getPatient());
        }

        catch (DataIntegrityViolationException e){
            Notification.show("Пользователь с таким паспортом уже есть в базе данных " + patientService.findByPassport(event.getPatient().getPassport())).setPosition(Notification.Position.MIDDLE);;
            if(form.isNullDoc()) {
                userService.delete(event.getPatient().getUser());
            }
            closeEditor();
            UI.getCurrent().getPage().reload();
            return;
        }
        updateList();
        closeEditor();
    }

    private void deletePatient(PatientForm.DeleteEvent event) {
        for(Schedule s: scheduleService.findAll(event.getPatient())){
            s.setPatient(patientService.nullPat());
            scheduleService.save(s);
        }
        patientService.delete(event.getPatient());
        userService.delete(event.getPatient().getUser());

        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setPatient(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(patientService.findAll());

    }
}
