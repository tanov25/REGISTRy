package com.example.application.views.user;

import com.example.application.data.entity.MedService;
import com.example.application.data.service.MedServiceService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.application.views.main.MainView;

@Route(value = "Service", layout = MainView.class)
@PageTitle("Платные услуги")
public class ServicesView extends Div {

    MedServiceService MedServiceService;
    private Grid<MedService> grid = new Grid<>(MedService.class);
    public ServicesView(MedServiceService MedServiceService) {
        setId("doctors-view");
        this.MedServiceService = MedServiceService;
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        setSizeFull();
        configureGrid();
        add(grid);
        updateList();
    }
    private void configureGrid() {
        grid.addClassName("MedService-grid");
        grid.setSizeFull();
        grid.setColumns("name", "price");
        grid.getColumns().get(0).setHeader("Название");
        grid.getColumns().get(1).setHeader("Цена в рублях");
    }

    private void updateList() {
        grid.setItems(MedServiceService.findAll());
    }


}
