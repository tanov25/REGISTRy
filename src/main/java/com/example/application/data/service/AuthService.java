package com.example.application.data.service;

import com.example.application.data.entity.enums.Role;
import com.example.application.data.entity.User;
import com.example.application.data.service.repo.UserRepository;
import com.example.application.views.admin.AdminUserView;
import com.example.application.views.admin.AdminDoctorsView;
import com.example.application.views.admin.AdminLabScheduleView;
import com.example.application.views.admin.AdminPatientsView;
import com.example.application.views.admin.ScheduleView;
import com.example.application.views.admin.AdminServicesView;
import com.example.application.views.login.DeleteAdminView;
import com.example.application.views.login.RegisterView;
import com.example.application.views.logout.LogoutView;
import com.example.application.views.main.MainView;
import com.example.application.views.user.AppointmentsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {
    public static final String AUTHENTICATED_USER_NAME = "authenticatedUserName";
    public record AuthorizedRoute(String route, String name, Class<? extends Component> view) {

    }
    public class AuthException extends Exception {

    }
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void authenticate(String username, String password) throws AuthException {
        User user = userRepository.getByUsername(username);
        if (user != null && user.checkPassword(password)) {
            VaadinSession.getCurrent().setAttribute(User.class, user);
            VaadinSession.getCurrent().setAttribute(AUTHENTICATED_USER_NAME, user.getUsername());
            createRoutes(user.getRole());
        } else {
            throw new AuthException();
        }
    }

    public void register(String username, String password) {
        userRepository.save(new User(username, password, Role.ADMIN));
    }

    private void createRoutes(Role role) {
        getAuthorizedRoutes(role).stream()
                .forEach(route ->
                        RouteConfiguration.forSessionScope().setRoute(
                                route.route, route.view, MainView.class));
    }

    public List<AuthorizedRoute> getAuthorizedRoutes(Role role) {
        var routes = new ArrayList<AuthorizedRoute>();

        if (role.equals(Role.USER)) {
            routes.add(new AuthorizedRoute("schedule", "Записаться на прием", ScheduleView.class));
            routes.add(new AuthorizedRoute("planned_schedule", "Редактировать профиль", AppointmentsView.class));
            routes.add(new AuthorizedRoute("logout", "Выйти", LogoutView.class));

        } else if (role.equals(Role.ADMIN)) {
            routes.add(new AuthorizedRoute("admin_doctors", "Редактировать врачей", AdminDoctorsView.class));
            routes.add(new AuthorizedRoute("admin_patients", "Редактировать пациентов", AdminPatientsView.class));
            routes.add(new AuthorizedRoute("admin_appointments", "Пользователи", AdminUserView.class));
            routes.add(new AuthorizedRoute("admin_schedule", "Редактировать расписание приемов", ScheduleView.class));
            routes.add(new AuthorizedRoute("admin_lab_schedule", "Редактировать расписание лабораторий", AdminLabScheduleView.class));
            routes.add(new AuthorizedRoute("admin_service", "Редактировать услуги", AdminServicesView.class));
            routes.add(new AuthorizedRoute("delete_admin", "Удалить админа", DeleteAdminView.class));
            routes.add(new AuthorizedRoute("add_admin", "Добавить админа", RegisterView.class));
            routes.add(new AuthorizedRoute("logout", "Выйти", LogoutView.class));
        }
        else if (role.equals(Role.DOCTOR)) {
            routes.add(new AuthorizedRoute("schedule", "Расписание работы", ScheduleView.class));
            routes.add(new AuthorizedRoute("logout", "Выйти", LogoutView.class));
        }
        return routes;
    }

    public String returnFirstRoute(String username){
        User user = userRepository.getByUsername(username);
        Role role = user.getRole();
        String ret = "";
        switch (role){
            case USER, DOCTOR -> {
                ret = "schedule";
                break;
            }
            case ADMIN -> {
                ret = "admin_doctors";
                break;
            }
        }
        return ret;
    }
}
