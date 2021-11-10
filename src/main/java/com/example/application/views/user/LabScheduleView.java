package com.example.application.views.user;

import com.example.application.data.entity.Lab;
import com.example.application.data.service.LabService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.application.views.main.MainView;

@Route(value = "LabSchedule", layout = MainView.class)
@PageTitle("Расписание работы лабораторий")
public class

LabScheduleView extends Div {
    LabService LabService;
    private Grid<Lab> grid = new Grid<>(Lab.class);
    public LabScheduleView(LabService LabService) {
        setId("doctors-view");
        this.LabService = LabService;
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        setSizeFull();
        configureGrid();
        add(grid);
        updateList();
    }
    private void configureGrid() {
        grid.addClassName("Lab-grid");
        grid.setSizeFull();
        grid.setColumns("name", "open", "close", "days");
        grid.getColumns().get(0).setHeader("Название");
        grid.getColumns().get(1).setHeader("Открытие");
        grid.getColumns().get(2).setHeader("Закрытие");
        grid.getColumns().get(3).setHeader("Дни работы");
    }

    private void updateList() {
        grid.setItems(LabService.findAll());
    }

}
