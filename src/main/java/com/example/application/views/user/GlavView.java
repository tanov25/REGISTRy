package com.example.application.views.user;

import com.example.application.views.login.LoginView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.application.views.main.MainView;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLink;

@Route(value = "Main", layout = MainView.class)
@PageTitle("Главная")
@CssImport("./styles/views/glav/glav-view.css")
@RouteAlias(value = "", layout = MainView.class)
public class GlavView extends VerticalLayout {


    public GlavView() {
        setId("glav-view");
        add(new H2("Добро пожаловать в регистратуру!"),
                new Paragraph("В приложении регистратура, вы можете очень просто зарегестрироваться. После регистрации вы можете записаться к врачу и посмотреть свои запланированные приемы."),
                new Paragraph("Если вы врач, после входа в систему вы можете посмотреть свое расписание."),
                new Paragraph("Если вы не хотите заводить лишний аккаунт, то записать к врачу вас может наш администратор."),
                new Paragraph("Также на сайте предоставлена информация о предоставляемых услугах и работе лабораторий."),
                new RouterLink("Войти в систему", LoginView.class)
        );


    }

}
