package com.example.patientskydashoard.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import com.example.patientskydashoard.repository.AppointmentsRepo;
import com.example.patientskydashoard.models.Appointments;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.Duration;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    public AppointmentsRepo appointmentrepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Appointments> getAppointments() {
        try {
            return appointmentrepo.findAll();
        } catch (Exception e) {
            e.printStackTrace();
           
            throw e;
        }
    }

    @Override
    public Optional<Appointments> getAppointmentById(String appId) {
        try {
            return appointmentrepo.findById(appId);
        } catch (Exception e) {
            e.printStackTrace();
           
            throw e;
        }
    }

    @Override
    public Appointments bookAppointment(Appointments appointment) {
        try {
            return appointmentrepo.save(appointment);
        } catch (Exception e) {
            e.printStackTrace();
            
            throw e;
        }
    }

    @Override
public HashMap<String, Object> findAvailableTime(String calendarIds, int duration, LocalDateTime startdateTime, LocalDateTime endDateTime) {
    try {
        String[] pOutMsg = new String[1];
        String[] pFlag = new String[1];
       // Initializing LocalDateTime variables
LocalDateTime startdate = LocalDateTime.now(); // or provide a specific date and time
LocalDateTime enddate = LocalDateTime.now();   // or provide a specific date and time
LocalDateTime startdateTimeNew = LocalDateTime.now(); // or provide a specific date and time
LocalDateTime endDateTimeNew = LocalDateTime.now();   // or provide a specific date and time
LocalDateTime wSlotTime = LocalDateTime.now();        // or provide a specific date and time
Boolean vflag=true;

// Initializing Duration variable
Duration wDiff = Duration.ZERO;  // or provide a specific duration, e.g., Duration.ofMinutes(10)

        int count = 0;
        HashMap<String, Object> result = new HashMap<>();
        Map<String, Object> findMinAndMaxTime = appointmentrepo.findMinAndMaxTime(startdateTime, endDateTime, calendarIds);

        System.out.println("findMinAndMaxTime size: " + findMinAndMaxTime.size());

        if (!findMinAndMaxTime.isEmpty()) {
            // Check and extract "TotalCount" from the map
            BigDecimal resultFromQuery = findMinAndMaxTime.get("TotalCount") instanceof BigDecimal ?
                    (BigDecimal) findMinAndMaxTime.get("TotalCount") : BigDecimal.ZERO;
        
            count = resultFromQuery.intValue();
        System.out.println("count is"+count);
            // Check and extract "StartDate" from the map
            Timestamp timestampFromQuery = findMinAndMaxTime.get("StartDate") instanceof Timestamp ?
                    (Timestamp) findMinAndMaxTime.get("StartDate") : null;
        
            startdate = timestampFromQuery != null ? timestampFromQuery.toLocalDateTime() : null;
        
            // Check and extract "EndDate" from the map
            Timestamp timestampFromQueryend = findMinAndMaxTime.get("EndDate") instanceof Timestamp ?
                    (Timestamp) findMinAndMaxTime.get("EndDate") : null;
        
            enddate = timestampFromQueryend != null ? timestampFromQueryend.toLocalDateTime() : null;
        }
        if (count==1) {
            Map<String, Object> findDuration = appointmentrepo.findDuration(startdateTime, endDateTime, calendarIds);

            if (!findDuration.isEmpty()) {
                Timestamp timestampFromQuery = findMinAndMaxTime.get("StartDate") instanceof Timestamp ?
                    (Timestamp) findMinAndMaxTime.get("StartDate") : null;
        
            startdate = timestampFromQuery != null ? timestampFromQuery.toLocalDateTime() : null;
        
            // Check and extract "EndDate" from the map
            Timestamp timestampFromQueryend = findMinAndMaxTime.get("EndDate") instanceof Timestamp ?
                    (Timestamp) findMinAndMaxTime.get("EndDate") : null;
        
            enddate = timestampFromQueryend != null ? timestampFromQueryend.toLocalDateTime() : null;
            vflag=false;
            }
        }
        else if(count==0)
        {
            wSlotTime = startdateTime.plus(Duration.ofMinutes(duration));
            pOutMsg[0] = "Available slot: " + startdateTime + " TO " + wSlotTime + " Do you wish to Continue";
            result.put("pOutMsg", pOutMsg[0]);
            System.out.println("inside"+result.entrySet());
        }
if(null!=startdate)
{
        if (startdateTime.isBefore(startdate)) {
            wDiff = Duration.between(startdateTime, startdate);
            startdateTimeNew = startdateTime.plus(wDiff);
            wSlotTime = startdateTime.plus(Duration.ofMinutes(duration));

            if (wSlotTime.isAfter(startdateTimeNew)) {
                pOutMsg[0] = " The slot is fully booked.";
            } else {
                pOutMsg[0] = "Available slot: " + startdateTime + " TO " + wSlotTime + " Do you wish to Continue";
            }

            pFlag[0] = "F";
        }  if (endDateTime.isAfter(enddate)) {
            wDiff = Duration.between(enddate, endDateTime);
            endDateTimeNew = endDateTime.minus(wDiff);
            wSlotTime = endDateTimeNew.plus(Duration.ofMinutes(duration));

            if (wSlotTime.isAfter(endDateTime)) {
                pOutMsg[0] = " The slot is fully booked.";
            } else {
                pOutMsg[0] = "Available slot: " + endDateTimeNew + " to " + wSlotTime + " Do you wish to Continue";
                pFlag[0] = "F";
            }
        }

        if(vflag)
        {
            pOutMsg[0] = " The slot is fully booked.";
        }
       
        result.put("pOutMsg", pOutMsg[0]);
        result.put("pFlag", pFlag[0]);
        System.out.println("result" + result);
    }
        return result;
    } catch (Exception e) {
        e.printStackTrace();
        throw e;
    }
}

}
