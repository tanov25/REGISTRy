package com.example.application.views.admin;

import com.example.application.data.entity.*;
import com.example.application.data.service.LabService;
import com.example.application.views.admin.enterform.LabForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Calendar;
import java.util.Date;

//@Route(layout = MainView.class)
@PageTitle("adminLabSchedule")
public class AdminLabScheduleView extends Div {
    private LabService labService;
    private LabForm form;
    private Grid<Lab> grid = new Grid<>(Lab.class);
    public AdminLabScheduleView(LabService LabService) {
        setId("labs-view");
        this.labService = LabService;
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        form = new LabForm();
        form.addListener(LabForm.SaveEvent.class, this::saveLab);
        form.addListener(LabForm.DeleteEvent.class, this::deleteLab);
        form.addListener(LabForm.CloseEvent.class, e -> closeEditor());
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
        grid.addClassName("Lab-grid");
        grid.setSizeFull();
        grid.setColumns("name", "open", "close", "days");
        grid.getColumns().get(0).setHeader("Название");
        grid.getColumns().get(1).setHeader("Открытие");
        grid.getColumns().get(2).setHeader("Закрытие");
        grid.getColumns().get(3).setHeader("Дни работы");
    }

    private HorizontalLayout getToolbar() {
        Button addLabButton = new Button("Добавить лабораторию");
        addLabButton.addClickListener(click -> {grid.asSingleSelect().clear(); addLab();});
        HorizontalLayout toolbar = new HorizontalLayout(addLabButton);
        toolbar.addClassName("toolbar");
        grid.asSingleSelect().addValueChangeListener(event ->
                { editLab(event.getValue());}

        );
        return toolbar;
    }

    void addLab() {
        grid.asSingleSelect().clear();
        Lab lab = new Lab();
        editLab(lab);
    }

    public void editLab(Lab lab) {
        form.clear();
        if (lab == null) {
            closeEditor();
        } else {
            form.setLab(lab);
            form.setVisible(true);
            if(lab.getName()== null){
                form.setNullDoc(true);
            }
            else{
                form.setData(lab);
                form.setNullDoc(false);
            }
            addClassName("editing");
        }
    }

    private void saveLab(LabForm.SaveEvent event) {
        labService.save(event.getLab());
        updateList();
        closeEditor();
    }

    private void deleteLab(LabForm.DeleteEvent event) {
        labService.delete(event.getLab());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.clear();
        form.setLab(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(labService.findAll());

    }

}
