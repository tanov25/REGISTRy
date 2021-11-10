package com.example.application.views.admin;

import com.example.application.data.entity.Doctor;
import com.example.application.data.entity.Patient;
import com.example.application.data.entity.Schedule;
import com.example.application.data.entity.User;
import com.example.application.data.entity.enums.Role;
import com.example.application.data.service.DoctorService;
import com.example.application.data.service.PatientService;
import com.example.application.data.service.ScheduleService;
import com.example.application.data.service.UserService;
import com.example.application.views.admin.enterform.DoctorForm;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.application.views.main.MainView;
import com.vaadin.server.VaadinSession;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

//@Route(layout = MainView.class)
@PageTitle("adminDoctors")
@Transactional
public class AdminDoctorsView extends Div {
    private DoctorService doctorService;
    private UserService userService;
    private ScheduleService scheduleService;
    private PatientService patientService;
    private Grid<Doctor> grid = new Grid<>(Doctor.class);
    private DoctorForm form;

    public AdminDoctorsView(DoctorService DoctorService, UserService userService, PatientService patientService, ScheduleService scheduleService) {
        setId("doctors-view");
        this.doctorService = DoctorService;
        this.userService = userService;
        this.scheduleService = scheduleService;
        this.patientService = patientService;
            form = new DoctorForm();
            form.addListener(DoctorForm.SaveEvent.class, this::saveDoctor);
            form.addListener(DoctorForm.DeleteEvent.class, this::deleteDoctor);
            form.addListener(DoctorForm.CloseEvent.class, e -> closeEditor());
            form.setMaxWidth("400px");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
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
        grid.addClassName("Doctor-grid");
        grid.setSizeFull();
        grid.setColumns("surname", "name", "otchestvo", "speciality", "experience");
        grid.addColumn(doctor -> {
            User user = doctor.getUser();
            return user == null ? "-" : user.getUsername();
        });
        grid.getColumns().get(0).setHeader("Фамилия");
        grid.getColumns().get(1).setHeader("Имя");
        grid.getColumns().get(2).setHeader("Отчество");
        grid.getColumns().get(3).setHeader("Специальность");
        grid.getColumns().get(4).setHeader("Стаж");
        grid.getColumns().get(5).setHeader("Логин");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editDoctor(event.getValue())
        );
    }

    private HorizontalLayout getToolbar() {
        Button addDoctorButton = new Button("Добавить врача");
        addDoctorButton.addClickListener(click -> addDoctor());
        HorizontalLayout toolbar = new HorizontalLayout(addDoctorButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    void addDoctor() {
        grid.asSingleSelect().clear();
        Doctor doctor = new Doctor();
        doctor.setUser(new User());
        doctor.getUser().setRole(Role.DOCTOR);
        editDoctor(doctor);
    }

    public void editDoctor(Doctor doctor) {
        form.clear();
        if (doctor == null) {
            closeEditor();
        } else {
            form.setDoctor(doctor);
            form.setVisible(true);
            if(doctor.getName()== null){
                form.setNullDoc(true);
            }
            else{
                form.setData(doctor);
                form.setNullDoc(false);
            }
            addClassName("editing");
        }
    }

    private void saveDoctor(DoctorForm.SaveEvent event) {
        try{
            userService.save(event.getDoctor().getUser());
        }
        catch (DataIntegrityViolationException e1){
            editDoctor(event.getDoctor());
            Notification.show("Пользователь с таким никнеймом уже есть в базе данных ").setPosition(Notification.Position.MIDDLE);
            closeEditor();
            UI.getCurrent().getPage().reload();
            return;
        }
        doctorService.save(event.getDoctor());
        Patient nullpat = patientService.nullPat();
        Calendar start = Calendar.getInstance();
        start.add(Calendar.DATE, 1);
        start.set(Calendar.HOUR_OF_DAY, 10);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        for (int j = 0; j < 7; j++) {
            for (int k = 0; k < 6; k++) {
                Date date = start.getTime();
                start.add(Calendar.HOUR_OF_DAY, 1);
                scheduleService.save(new Schedule(date, event.getDoctor(), nullpat));
            }
            start.add(Calendar.DATE, 1);
            start.set(Calendar.HOUR_OF_DAY, 10);
        }
        updateList();
        closeEditor();
    }

    private void deleteDoctor(DoctorForm.DeleteEvent event) {
        scheduleService.delete(event.getDoctor());
        doctorService.delete(event.getDoctor());
        userService.delete(event.getDoctor().getUser());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setDoctor(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(doctorService.findAll());

    }
}