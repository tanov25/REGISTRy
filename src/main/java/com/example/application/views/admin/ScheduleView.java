package com.example.application.views.admin;

import com.example.application.data.entity.*;
import com.example.application.data.entity.Schedule;
import com.example.application.data.entity.enums.Speciality;
import com.example.application.data.service.DoctorService;
import com.example.application.data.service.PatientService;
import com.example.application.data.service.ScheduleService;
import com.example.application.data.service.ScheduleService;
import com.example.application.views.admin.enterform.PatientForm;
import com.example.application.views.admin.enterform.ScheduleForm;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.application.views.main.MainView;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinSession;

import javax.persistence.criteria.CriteriaBuilder;
import javax.print.Doc;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

//@Route(layout = MainView.class)
@PageTitle("Расписание приемов")
public class ScheduleView extends Div {
    private Date dt = null;
    private Doctor dc = null;
    private ScheduleService scheduleService;
    private DoctorService doctorService;
    private PatientService patientService;
    private Grid<Schedule> grid = new Grid<>(Schedule.class);
    private Patient patMain;
    private List<Patient> patList = new LinkedList<>();
    private Calendar calendar;
    private ComboBox<String> day = new ComboBox<>();
    private ComboBox<Speciality> speciality = new ComboBox<>();
    private ComboBox<String> doctors = new ComboBox<>();
    private Checkbox checkBoxFree = new Checkbox();
    private Checkbox checkBoxNotFree = new Checkbox();
    private ScheduleForm form;

    public ScheduleView(ScheduleService scheduleService, DoctorService doctorService, PatientService patientService) {
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        setId("schedule-view");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        this.scheduleService = scheduleService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        com.vaadin.flow.server.VaadinSession session = VaadinSession.getCurrent();
        if(session == null){
            return;
        }
        var user = session.getAttribute(User.class);
        if(user == null) {
            return;
        }

        if(doctorService.findByUser(user) != null){
            dc = doctorService.findByUser(user);
            HorizontalLayout content = new HorizontalLayout(grid);
            content.addClassName("content");
            content.setSizeFull();
            setSizeFull();
            configureGrid();
            add(getToolBar(dc), content);
            this.getStyle().set("overflow", "hidden");
            boolean all = checkBoxFree.getValue() | checkBoxNotFree.getValue(); updateList(dt, dc, all);
            return;
        }

        patMain = patientService.findByUser(user);
        if(patMain != null) {
            Collections.addAll(patList, patMain);
            form = new ScheduleForm(patList);
        }
        else{
            form = new ScheduleForm(patientService.findAll());
        }

        form.addListener(ScheduleForm.SaveEvent.class, this::saveSchedule);
        form.addListener(ScheduleForm.DeleteEvent.class, this::deleteSchedule);
        form.addListener(ScheduleForm.CloseEvent.class, e -> closeEditor());
        form.setMaxWidth("400px");
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.addClassName("content");
        content.setSizeFull();
        setSizeFull();
        configureGrid();
        add(getToolBar(), content);
        this.getStyle().set("overflow", "hidden");
        boolean all = checkBoxFree.getValue() | checkBoxNotFree.getValue(); updateList(dt, dc, all);
        closeEditor();
    }
    private void configureGrid() {
        grid.addClassName("Doctor-grid");
        grid.setSizeFull();
        DateFormat df = new SimpleDateFormat("MMM dd E HH часов");
        grid.setColumns();
        grid.addColumn(schedule -> {
            Doctor doctor = schedule.getDoctor();
            return doctor == null ? "-" : doctor.getSurname()+" "+doctor.getName()+" "+doctor.getOtchestvo();
        }).setHeader("Доктор");
        grid.addColumn(schedule -> {
            Patient patient = schedule.getPatient();
            if(patient == null) return "-";
            return patient.getSurname().equals("null") ? "-" : patient.getSurname()+" "+patient.getName()+" "+patient.getOtchestvo();
        }).setHeader("Пациент");
        grid.addColumn(schedule -> {
            Date date = schedule.getDate();
            return date == null ? "-" : df.format(date);
        }).setHeader("Дата");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
        {if(this.form != null)editSchedule(event.getValue());}
        );

    }

    private HorizontalLayout getToolBar() {
        DateFormat df = new SimpleDateFormat("MMM dd");
        checkBoxFree = new Checkbox();
        checkBoxNotFree = new Checkbox();
        checkBoxNotFree.setLabel("Зарезервировано");
        checkBoxFree.setLabel("Свободно");
        checkBoxFree.setValue(false);
        checkBoxNotFree.setValue(false);
        checkBoxFree.addValueChangeListener(valueChangeEvent -> {
            if(checkBoxFree.getValue()){
                updateList(dt, dc, true);
                checkBoxNotFree.setEnabled(false);
            }
            else{
                updateList(dt, dc, false);
                checkBoxNotFree.setEnabled(true);
            }
        });
        checkBoxNotFree.addValueChangeListener(valueChangeEvent -> {
            if(checkBoxNotFree.getValue()){
                updateList(dt, dc, true);
                checkBoxFree.setEnabled(false);
            }
            else{
                updateList(dt, dc, false);
                checkBoxFree.setEnabled(true);
            }
        });
        LinkedList<Date> dates = new LinkedList<>();
        LinkedList<String> datesStr = new LinkedList<>();
        HashMap<String, Date> map = new HashMap<>();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        for(int j = 0; j < 7; j++){
            Date date = calendar.getTime();
            calendar.add(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 9);
            dates.add(date);
            datesStr.add(df.format(date));
            map.put(df.format(date), date);
        }
        day.setPlaceholder("Отсортировать по дню...");
        day.setClearButtonVisible(true);
        day.setItems(datesStr);
        day.addValueChangeListener(e -> {
            dt = map.get(e.getValue());
            boolean all = checkBoxFree.getValue() | checkBoxNotFree.getValue(); updateList(dt, dc, all);
        });

        List<Doctor> docs = doctorService.findAll();
        List<String> docsStr = new LinkedList<>();
        HashMap<String, Doctor> mapD = new HashMap<>();
        for(Doctor d: docs){
            String str = d.getSurname() + " " + d.getName() + " " + d.getOtchestvo();
            docsStr.add(str);
            mapD.put(str, d);
        }
        speciality.setItems(Speciality.values());
        speciality.addValueChangeListener(e->{
            docsStr.clear();
            if(e.getValue() != null) {
                for (Doctor d : docs) {
                    String str = d.getSurname() + " " + d.getName()+ " " + d.getOtchestvo();
                    if (d.getSpeciality() == e.getValue()) {
                        docsStr.add(str);
                        mapD.put(str, d);
                    }
                }
            }
            else{
                for (Doctor d : docs) {
                    String str = d.getSurname()+ " " + d.getName()+ " " + d.getOtchestvo();
                        docsStr.add(str);
                        mapD.put(str, d);

                }
            }
            doctors.setItems(docsStr);
        });
        speciality.setClearButtonVisible(true);
        speciality.setPlaceholder("Выбрать категорию врача");

        doctors.setPlaceholder("Отсортировать по врачу...");
        doctors.setClearButtonVisible(true);
        doctors.setItems(docsStr);
        doctors.addValueChangeListener(e -> { dc = mapD.get(e.getValue()); boolean all = checkBoxFree.getValue() | checkBoxNotFree.getValue(); updateList(dt, dc, all);});
        HorizontalLayout toolbar = new HorizontalLayout(day, speciality, doctors, checkBoxFree, checkBoxNotFree);
        toolbar.addClassName("toolbar");
        toolbar.setVerticalComponentAlignment(FlexComponent.Alignment.END, day, speciality, doctors, checkBoxFree, checkBoxNotFree);
        return toolbar;
    }


    private HorizontalLayout getToolBar(Doctor doctor) {
        if(doctor == null){
            return getToolBar();
        }
        DateFormat df = new SimpleDateFormat("MMM dd");
        checkBoxFree = new Checkbox();
        checkBoxNotFree = new Checkbox();
        checkBoxNotFree.setLabel("Зарезервировано");
        checkBoxFree.setLabel("Свободно");
        checkBoxFree.setValue(false);
        checkBoxNotFree.setValue(false);
        checkBoxFree.addValueChangeListener(valueChangeEvent -> {
            if(checkBoxFree.getValue()){
                updateList(dt, dc, true);
                checkBoxNotFree.setEnabled(false);
            }
            else{
                updateList(dt, dc, false);
                checkBoxNotFree.setEnabled(true);
            }
        });
        checkBoxNotFree.addValueChangeListener(valueChangeEvent -> {
            if(checkBoxNotFree.getValue()){
                updateList(dt, dc, true);
                checkBoxFree.setEnabled(false);
            }
            else{
                updateList(dt, dc, false);
                checkBoxFree.setEnabled(true);
            }
        });
        LinkedList<Date> dates = new LinkedList<>();
        LinkedList<String> datesStr = new LinkedList<>();
        HashMap<String, Date> map = new HashMap<>();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        for(int j = 0; j < 7; j++){
            Date date = calendar.getTime();
            calendar.add(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 9);
            dates.add(date);
            datesStr.add(df.format(date));
            map.put(df.format(date), date);
        }
        day.setPlaceholder("Отсортировать по дню...");
        day.setClearButtonVisible(true);
        day.setItems(datesStr);
        day.addValueChangeListener(e -> {
            dt = map.get(e.getValue());
            boolean all = checkBoxFree.getValue() | checkBoxNotFree.getValue(); updateList(dt, dc, all);
        });

        HorizontalLayout toolbar = new HorizontalLayout(day, checkBoxFree, checkBoxNotFree);
        toolbar.addClassName("toolbar");
        return toolbar;
    }


    private void updateList(Date date, Doctor doctor, boolean all) {
        List<Schedule> list = scheduleService.findAll(doctor);
        List <Schedule> list1 = new LinkedList<>();
        if(date != null) {
            for (Schedule sch : list) {
                int a = date.compareTo(sch.getDate());
                if (a < 0) {
                    list1.add(sch);
                }
            }
        }
        else{
            list1 = list;
        }
        if(!all) {
           List <Schedule> list2 =  new LinkedList<>(list1);
            for(Schedule sch: list1){
                Patient pat = sch.getPatient();
                if(patMain != null && !pat.getName().equals("null")  && !pat.getPassport().equals(patMain.getPassport())){
                        list2.remove(sch);
                }
            }
            list2.sort(new DocIdComparator());
            grid.setItems(list2);
        }
        else{
            if(checkBoxFree.getValue()){
                List <Schedule> list2 = new LinkedList<>();
                for(Schedule sch: list1){
                    Patient pat = sch.getPatient();
                    if(pat.getName().equals("null") ){
                        list2.add(sch);
                    }
                }
                list2.sort(new DocIdComparator());
                grid.setItems(list2);
            }
            else if(checkBoxNotFree.getValue()){
                List <Schedule> list2 = new LinkedList<>();
                for(Schedule sch: list1){
                    Patient pat = sch.getPatient();
                    if(patMain == null && !pat.getName().equals("null")  ){
                        list2.add(sch);
                    }
                    else if(patMain != null && pat.getPassport().equals(patMain.getPassport())){
                        list2.add(sch);
                    }
                }
                list2.sort(new DocIdComparator());
                grid.setItems(list2);
            }
            else{
                list1.sort(new DocIdComparator());
                grid.setItems(list1);
            }
        }
    }
    class DocIdComparator implements Comparator<Schedule>{

        public int compare(Schedule a, Schedule b){

            if(a.getDoctor().getId()> b.getDoctor().getId())
                return 1;
            else if(a.getDoctor().getId()< b.getDoctor().getId())
                return -1;
            else
                return 0;
        }
    }


    public void editSchedule(Schedule schedule) {
        if (schedule == null) {
            closeEditor();
        } else {
            form.clear();
            form.setSchedule(schedule);
            form.setData(schedule);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void saveSchedule(ScheduleForm.SaveEvent event) {
        form.clear();
     //   Notification.show("Пациент save");
        Schedule schedule = event.getSchedule();
        List<Schedule> list =  scheduleService.findAll(schedule.getPatient());
        for(Schedule t: list){
            if(schedule.getDoctor().toString().equals(t.getDoctor().toString())){
             //   event.getSchedule().setPatient(patientService.nullPat());
               // scheduleService.save(event.getSchedule());
         //       Notification.show("Пациент уже записан");
                boolean all = checkBoxFree.getValue() | checkBoxNotFree.getValue(); updateList(dt, dc, all);
                Notification.show("Пациент уже записан");
                closeEditor();
                return;
            }
            else{
             //   Notification.show("Пусто");

            }
        }
        scheduleService.save(event.getSchedule());
        boolean all = checkBoxFree.getValue() | checkBoxNotFree.getValue(); updateList(dt, dc, all);
        closeEditor();
    }

    private void deleteSchedule(ScheduleForm.DeleteEvent event) {
        event.getSchedule().setPatient(patientService.nullPat());
        scheduleService.save(event.getSchedule());
        boolean all = checkBoxFree.getValue() | checkBoxNotFree.getValue(); updateList(dt, dc, all);
        closeEditor();
    }

    private void closeEditor() {
        form.setSchedule(null);
        form.setVisible(false);
        removeClassName("editing");
    }


}