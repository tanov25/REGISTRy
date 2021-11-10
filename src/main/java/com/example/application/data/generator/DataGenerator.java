package com.example.application.data.generator;

import com.example.application.data.entity.*;
import com.example.application.data.entity.enums.Role;
import com.example.application.data.service.*;
import com.example.application.data.service.repo.*;
import com.vaadin.flow.spring.annotation.SpringComponent;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Repository
@SpringComponent
public class DataGenerator {

@Bean
    public CommandLineRunner loadData(UserRepository userRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, ScheduleRepository scheduleRepository, LabRepository labRepository, MedServiceRepository serviceRepository) {
       Random r = new Random(153);
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 3);
            calendar.set(Calendar.HOUR_OF_DAY, 10);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date date1 = calendar.getTime();
            //scheduleRepository.deleteAllAll();

            List<Schedule> list = scheduleRepository.findAll();
            for(Schedule l: list){
                if(l.getDate().compareTo(date1) < 0){
                scheduleRepository.deleteSch(l);
                  }
            }
            Date biggest;
            try {
                if(list.get(list.size() - 1).getDate().after(date1)){
                    biggest =  list.get(list.size() - 1).getDate();
                }
                else{
                  //  scheduleRepository.deleteAllAll();
                    biggest = date1;
                }
            }
            catch (IndexOutOfBoundsException e){
                biggest = new Date(0);
            }

            List<Doctor> allDocs = doctorRepository.findAll();
            calendar.set(Calendar.HOUR_OF_DAY, 10);
           // calendar.set(Calendar.MINUTE, 0);
           // calendar.set(Calendar.SECOND, 0);
            PatientService ps = new PatientService(patientRepository);
            Patient nullpat1 = ps.nullPat();
            for(Doctor d: allDocs){
                for(int j = 0; j < 7; j++){
                    for(int k = 0; k < 6; k++){
                        Date datedate = calendar.getTime();
                        if(datedate.after(biggest)) {
                            scheduleRepository.save(new Schedule(datedate, d, nullpat1));
                        }
                        calendar.add(Calendar.HOUR_OF_DAY, 1);
                    }
                    calendar.add(Calendar.DATE, 1);
                    calendar.set(Calendar.HOUR_OF_DAY, 10);
                }
                calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 3);
                calendar.set(Calendar.HOUR_OF_DAY, 10);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
            }
            List<User> users = userRepository.findAll();
            for(User u: users){
                if(u.getPatient() == null && u.getDoctor() == null && u.getRole() != Role.ADMIN){
                    userRepository.delete(u);
                }
            }

/*
            if (userRepository.count() != 0L) {
            //    userRepository.deleteAll();
                return;
            }
            if (doctorRepository.count() != 0L) {
            //    doctorRepository.deleteAll();
                return;
            }
            if(patientRepository.count() != 0L ){
            //    patientRepository.deleteAll();
                return;
            }

            if(labRepository.count() != 0L ){
             //   labRepository.deleteAll();
                return;
            }
            if(serviceRepository.count() != 0L ){
           //     serviceRepository.deleteAll();
                return;
            }

            MedRepository ser = new MedRepository("Прием у врача", (long) 1000);
            MedRepository ser1 = new MedRepository("УЗИ", (long) 600);
            serviceRepository.save(ser);
            serviceRepository.save(ser1);

            Lab lab = new Lab("Биохимическая", 12, 19, "пн-пт");
            Lab lab1 = new Lab("Бактериологическая", 17, 19);
            labRepository.save(lab);
            labRepository.save(lab1);

            User user = new User("user", "00000", Role.USER);
            User user1 = new User("user1", "11111", Role.USER);
            User user2 = new User("user2", "22222", Role.USER);
            User user3 = new User("user3", "33333", Role.USER);
            User user4 = new User("user4", "44444", Role.USER);

            User user5 = new User("user5", "55555", Role.DOCTOR);
            User user6 = new User("user6", "66666", Role.DOCTOR);

            User admin = new User("admin", "12345", Role.ADMIN);

            userRepository.save(admin);
            userRepository.save(user);
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user4);
            userRepository.save(user5);
            userRepository.save(user6);
           // User user7 = new User("user7", "77777", Role.DOCTOR);
          //  User user8 = new User("user8", "88888", Role.DOCTOR);
          //  User user9 = new User("user9", "99999", Role.DOCTOR);
            Patient pat = new Patient("Новик", "Татьяна", "Олеговна", new Date(0), "5555555555", user);
            Patient pat1 = new Patient("Иванов", "Иван", "Иванович", new Date(1), "6666666666", user1);
            Patient pat2 = new Patient("Петров", "Петр", "Иванович", new Date(2), "7777777777", user2);
            Patient pat3 = new Patient("Сидоров", "Сергей", "Иванович", new Date(3), "9999999999", user3);
            Patient pat4 = new Patient("Катамаров", "Алексей", "Иванович", new Date(4), "1010101", user4);
            Patient nullpat = new Patient("null", "null", "null", new Date(0), "null", null);
            patientRepository.save(nullpat);
            patientRepository.save(pat);
            patientRepository.save(pat1);
            patientRepository.save(pat2);
            patientRepository.save(pat3);
            patientRepository.save(pat4);

            Doctor doctor = new Doctor("Кульков", "Николай", "Осипович", Speciality.surgeon, "13", user5);
            Doctor doctor1 = new Doctor("Зола", "Анна", "Николаевна", Speciality.cardiologist, "7", user6);
            Doctor[] doc = new Doctor[2];
            doc[0] = doctor;
            doc[1] = doctor1;
            doctorRepository.save(doctor1);
            doctorRepository.save(doctor);


            Calendar start = Calendar.getInstance();
            start.add(Calendar.DATE, 1);
            start.set(Calendar.HOUR_OF_DAY, 10);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            // Calendar end = Calendar.getInstance();

 */


/*
            for(int i =0; i < 2; i++){
                for(int j = 0; j < 7; j++){
                    for(int k = 0; k < 6; k++){
                        Date date = start.getTime();
                        start.add(Calendar.HOUR_OF_DAY, 1);
                        scheduleRepository.save(new Schedule(date, doc[i], nullpat));
                    }
                    start.add(Calendar.DATE, 1);
                    start.set(Calendar.HOUR_OF_DAY, 10);
                }
                start = Calendar.getInstance();
                start.add(Calendar.DATE, 1);
                start.set(Calendar.HOUR_OF_DAY, 10);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
            }
*/

        };



    }




}
