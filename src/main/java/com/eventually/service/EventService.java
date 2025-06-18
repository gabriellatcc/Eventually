package com.eventually.service;

import java.time.LocalDate;
public class EventService {
    public boolean createEvent(String eventName, String description, String formato, int capacity,
                               LocalDate startDate, String startTime, LocalDate endDate, String endTime) {
        System.out.println("--- EventService: Recebida solicitação para criar evento ---");
        System.out.println("Nome: " + eventName);
        System.out.println("Formato: " + formato);
        System.out.println("Capacidade: " + capacity);
        System.out.println("Início: " + startDate + " às " + startTime);
        System.out.println("----------------------------------------------------------");
        return true;
    }
}
