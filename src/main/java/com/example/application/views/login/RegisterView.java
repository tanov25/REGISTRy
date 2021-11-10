package com.example.application.views.login;


import com.example.application.data.service.AuthService;
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
import org.springframework.dao.DataIntegrityViolationException;

@PageTitle("adminAdd")
public class RegisterView extends Div {

    private final AuthService authService;

    public RegisterView(AuthService authService) {
        this.authService = authService;
        TextField username = new TextField("Логин");
        PasswordField password1 = new PasswordField("Пароль");
        PasswordField password2 = new PasswordField("Подтвердить пароль");
        add(new VerticalLayout(
                new H2("Создать админа"),
                username,
                password1,
                password2,
                new Button("Зарегестрировать", event -> {
                    try {
                        register(
                                username.getValue(),
                                password1.getValue(),
                                password2.getValue()
                        );
                    }
                    catch (DataIntegrityViolationException e){
                        Notification.show("В базе уже есть такой пользователь");
                    }
                })
        )) ;
    }

    private void register(String username, String password1, String password2) {
        if (username.trim().isEmpty()) {
            Notification.show("Введите логин");
        } else if (password1.isEmpty()) {
            Notification.show("Введите пароль");
        } else if (!password1.equals(password2)) {
            Notification.show("Пароли не совпадают");
        } else {
            authService.register(username, password1);
            Notification.show("Успешная регистрация");
        }
    }
}
