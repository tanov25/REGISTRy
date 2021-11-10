package com.example.application.views.login;


import com.example.application.data.entity.User;
import com.example.application.data.entity.enums.Role;
import com.example.application.data.service.AuthService;
import com.example.application.data.service.UserService;
import com.example.application.views.admin.AdminUserView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;

@PageTitle("adminDelete")
public class DeleteAdminView extends Div {

    private final AuthService authService;
    private final UserService userService;
    public DeleteAdminView(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
        ComboBox<User> admins = new ComboBox<>();
        admins.setItems(userService.findByRole(Role.ADMIN));
        admins.setItemLabelGenerator(User::getUsername);
        admins.setLabel("Админы");
        add(new VerticalLayout(
                new H2("Удалить админа"),
                admins,
                new Button("Удалить", event -> {
                    if(userService.findByRole(Role.ADMIN).size() == 1){Notification.show("Невозможно удалить админа");return;}
                    if(VaadinSession.getCurrent().getAttribute(User.class).getUsername().equals(admins.getValue().getUsername())){
                        Notification.show("Невозможно удалить админа");return;
                    }
                    try{userService.delete(admins.getValue());}
                    catch (Exception e){
                        Notification.show("Невозможно удалить админа");
                        return;
                    }}
                ),
                new RouterLink("Назад", AdminUserView.class)
        ));
    }
}
