package com.example.application.views.user;

import com.example.application.data.entity.Doctor;
import com.example.application.data.service.DoctorService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.application.views.main.MainView;

import javax.print.Doc;

@Route(value = "Doctor", layout = MainView.class)
@PageTitle("Врачи")
public class DoctorsView extends Div {
    private DoctorService DoctorService;
    private Grid<Doctor> grid = new Grid<>(Doctor.class);
    public DoctorsView(DoctorService DoctorService) {
        setId("doctors-view");
        this.DoctorService = DoctorService;
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        setSizeFull();
        configureGrid();
        add(grid);
        updateList();
    }
    private void configureGrid() {
        grid.addClassName("Doctor-grid");
        grid.setSizeFull();
        grid.setColumns("surname", "name", "otchestvo", "speciality", "experience");
        grid.getColumns().get(0).setHeader("Фамилия");
        grid.getColumns().get(1).setHeader("Имя");
        grid.getColumns().get(2).setHeader("Отчество");
        grid.getColumns().get(3).setHeader("Специальность");
        grid.getColumns().get(4).setHeader("Стаж");
    }

    private void updateList() {
        grid.setItems(DoctorService.findAll());
    
}
}
