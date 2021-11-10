package com.example.application.views.admin;

import com.example.application.data.entity.MedService;
import com.example.application.data.entity.MedService;
import com.example.application.data.service.MedServiceService;
import com.example.application.views.admin.enterform.ServiceForm;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.application.views.main.MainView;

//@Route(layout = MainView.class)
@PageTitle("adminServices")
public class AdminServicesView extends Div {

   MedServiceService medServiceService;
    private Grid<MedService> grid = new Grid<>(MedService.class);
    private ServiceForm form;
    public AdminServicesView(MedServiceService medServiceService) {
        setId("doctors-view");
        this.medServiceService = medServiceService;
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        form = new ServiceForm();
        form.addListener(ServiceForm.SaveEvent.class, this::saveMedService);
        form.addListener(ServiceForm.DeleteEvent.class, this::deleteMedService);
        form.addListener(ServiceForm.CloseEvent.class, e -> closeEditor());
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
        grid.addClassName("MedService-grid");
        grid.setSizeFull();
        grid.setColumns("name", "price");
        grid.getColumns().get(0).setHeader("Название");
        grid.getColumns().get(1).setHeader("Цена в рублях");
    }

    private HorizontalLayout getToolbar() {
        Button addMedServiceButton = new Button("Добавить услугу");
        addMedServiceButton.addClickListener(click -> addMedService()
        );
        HorizontalLayout toolbar = new HorizontalLayout(addMedServiceButton);
        toolbar.addClassName("toolbar");
        grid.asSingleSelect().addValueChangeListener(event ->
                editMedService(event.getValue())
        );
        return toolbar;
    }

    void addMedService() {

        grid.asSingleSelect().clear();
        MedService medService = new MedService();
        editMedService(medService);
    }

    public void editMedService(MedService medService) {
        form.clear();
        if (medService == null) {
            closeEditor();
        } else {
            form.setMedService(medService);
            form.setVisible(true);
            if(medService.getName()== null){
                form.setNullDoc(true);
            }
            else{
                form.setData(medService);
                form.setNullDoc(false);
            }
            addClassName("editing");
        }
    }

    private void saveMedService(ServiceForm.SaveEvent event) {
        medServiceService.save(event.getMedService());
        updateList();
        closeEditor();
    }

    private void deleteMedService(ServiceForm.DeleteEvent event) {
        medServiceService.delete(event.getMedService());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setMedService(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(medServiceService.findAll());
    }

}
