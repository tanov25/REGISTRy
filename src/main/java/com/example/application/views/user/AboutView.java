package com.example.application.views.user;

import com.example.application.views.login.LoginView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.application.views.main.MainView;
import com.vaadin.flow.router.RouterLink;

@Route(value = "about", layout = MainView.class)
@PageTitle("О проекте")
public class AboutView extends VerticalLayout {

    public AboutView() {
        setId("about-view");
        add(new H2("О проекте"),
                new Paragraph("Автор: Новик ИКПИ-84, Тема: Медицинские услуги. Регистратура – расписание приёма врачей, работы лабораторий, запись на приём. 2020")
        );
    }

}
