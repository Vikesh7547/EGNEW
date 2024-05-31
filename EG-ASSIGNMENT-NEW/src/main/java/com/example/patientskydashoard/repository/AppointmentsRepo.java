package com.example.patientskydashoard.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.example.patientskydashoard.models.Appointments;


@Repository
public interface AppointmentsRepo extends JpaRepository<Appointments, String>{
    
    @Query(value = "SELECT * FROM Appointments WHERE CALENDAR_ID = ?1", nativeQuery = true)
    List<Appointments> findTimeByCalenderId(String calid);

    @Query(value = "SELECT MIN(startdate) as StartDate, MAX(enddate) as EndDate, count(*) as TotalCount FROM appointments WHERE ((startdate BETWEEN ?1 AND ?2) or (enddate BETWEEN ?1 AND ?2) ) and calendar_id=?3", nativeQuery = true)
    Map<String,Object> findMinAndMaxTime(LocalDateTime startdate, LocalDateTime enddate, String calendar_id);
    
   
    @Query(value = "SELECT a.startdate as StartDate, a.enddate as EndDate FROM appointments a WHERE ((a.startdate BETWEEN ?1 AND ?2) or (a.enddate BETWEEN ?1 AND ?2) ) and a.calendar_id=?3 and rownum = 1", nativeQuery = true)
    Map<String,Object> findDuration(LocalDateTime startdate,LocalDateTime enddate,String calendar_id);
    
}