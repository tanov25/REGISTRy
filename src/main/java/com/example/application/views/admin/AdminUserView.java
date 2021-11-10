package com.example.application.views.admin;

import com.example.application.data.entity.Doctor;
import com.example.application.data.entity.User;
import com.example.application.data.service.DoctorService;
import com.example.application.data.service.UserService;
import com.example.application.views.admin.enterform.DoctorForm;
import com.example.application.views.login.DeleteAdminView;
import com.example.application.views.login.RegisterView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.application.views.main.MainView;
import com.vaadin.flow.router.RouterLink;

//@Route(layout = MainView.class)
@PageTitle("adminUsers")
public class AdminUserView extends Div {
    private UserService userService;
    private Grid<User> grid = new Grid<>(User.class);
   // private DoctorForm form;

    public AdminUserView( UserService userService) {
        setId("doctors-view");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        this.userService = userService;

        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        setSizeFull();

        configureGrid();
        add(getToolbar(), content);
        updateList();
        // closeEditor();
    }
    private void configureGrid() {
        grid.addClassName("Doctor-grid");
        grid.setSizeFull();
        grid.setColumns("username", "passwordHash", "role");
        grid.getColumns().get(0).setHeader("Логин");
        grid.getColumns().get(1).setHeader("Пароль");
        grid.getColumns().get(2).setHeader("Роль");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

    }

    private HorizontalLayout getToolbar() {
        Button addDoctorButton = new Button("Add contact");

        HorizontalLayout toolbar = new HorizontalLayout(addDoctorButton, new RouterLink("Создать нового админа", RegisterView.class), new RouterLink("Удалить админа", DeleteAdminView.class));
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        grid.setItems(userService.findAll());

    }
}
