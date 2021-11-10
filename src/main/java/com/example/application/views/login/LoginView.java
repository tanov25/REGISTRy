package com.example.application.views.login;


import com.example.application.data.service.AuthService;
import com.example.application.views.main.MainView;
import com.example.application.views.user.AppointmentsView;
import com.example.application.views.user.GlavView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route(value = "Login")
@PageTitle("Login")

public class LoginView extends VerticalLayout {
    private AuthService authService;
    private TextField username;
    private PasswordField password;
    public LoginView(AuthService authService) {
        setId("login-view");
        this.authService = authService;
        username = new TextField("Логин");
        password = new PasswordField("Пароль");
        add(
                new H1("Добро пожаловать"),
                username,
                password,
                new Button("Войти"
                        , event -> {
                  try {
                        this.authService.authenticate(username.getValue(), password.getValue());
                        String route = this.authService.returnFirstRoute(username.getValue());
                        UI.getCurrent().navigate(route);
                    } catch (AuthService.AuthException e) {
                        Notification.show("Неверный пароль или логин");
                    }
                }),
                new RouterLink("На главную", GlavView.class) ,
                new RouterLink("Регистрация", AppointmentsView.class),
                new Paragraph("Если вы забыли пароль, обращайтесь к администратору")
        );
    }
}
