package sit707_tasks;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;

public class MeetingSchedulerTest2 {

    private MeetingScheduler scheduler;

    @Before
    public void setUp() {
        scheduler = new MeetingScheduler();
    }

    @Test
    public void scheduleMeeting_ValidDateTime_Successful() {
        List<String> participants = new ArrayList<>();
        participants.add("Alice");
        participants.add("Bob");

        scheduler.scheduleMeeting("Team Meeting", LocalDateTime.of(2024, 5, 10, 9, 0),
                LocalDateTime.of(2024, 5, 10, 10, 0), participants);

        assertEquals(1, scheduler.getMeetingsForParticipant("Alice").size());
        assertEquals(1, scheduler.getMeetingsForParticipant("Bob").size());
    }

    @Test
    public void scheduleMeeting_SameStartEndDateTime_Successful() {
        List<String> participants = new ArrayList<>();
        participants.add("Alice");

        scheduler.scheduleMeeting("Quick Meeting", LocalDateTime.of(2024, 5, 10, 11, 0),
                LocalDateTime.of(2024, 5, 10, 11, 0), participants);

        assertEquals(1, scheduler.getMeetingsForParticipant("Alice").size());
    }

    @Test
    public void scheduleMeeting_EndBeforeStart_ExceptionThrown() {
        List<String> participants = new ArrayList<>();
        participants.add("Alice");
        participants.add("Bob");

        assertThrows(IllegalArgumentException.class, () -> {
            scheduler.scheduleMeeting("Invalid Meeting", LocalDateTime.of(2024, 5, 10, 10, 0),
                    LocalDateTime.of(2024, 5, 10, 9, 0), participants);
        });
    }

    @Test
    public void scheduleMeeting_OverlappingTimeSlots_ExceptionThrown() {
        List<String> participants1 = new ArrayList<>();
        participants1.add("Alice");
        participants1.add("Bob");

        scheduler.scheduleMeeting("Meeting 1", LocalDateTime.of(2024, 5, 10, 9, 0),
                LocalDateTime.of(2024, 5, 10, 10, 0), participants1);

        List<String> participants2 = new ArrayList<>();
        participants2.add("Alice");
        participants2.add("Charlie");

        assertThrows(IllegalStateException.class, () -> {
            scheduler.scheduleMeeting("Meeting 2", LocalDateTime.of(2024, 5, 10, 9, 30),
                    LocalDateTime.of(2024, 5, 10, 10, 30), participants2);
        });
    }

    @Test
    public void scheduleMeeting_NoExistingMeetingsForParticipant_Successful() {
        List<String> participants = new ArrayList<>();
        participants.add("Alice");

        scheduler.scheduleMeeting("New Meeting", LocalDateTime.of(2024, 5, 15, 14, 0),
                LocalDateTime.of(2024, 5, 15, 15, 0), participants);

        assertEquals(1, scheduler.getMeetingsForParticipant("Alice").size());
    }

    @Test
    public void scheduleMeeting_ExistingMeetingsForParticipant_Successful() {
        List<String> participants1 = new ArrayList<>();
        participants1.add("Alice");

        scheduler.scheduleMeeting("Meeting 1", LocalDateTime.of(2024, 5, 15, 10, 0),
                LocalDateTime.of(2024, 5, 15, 11, 0), participants1);

        List<String> participants2 = new ArrayList<>();
        participants2.add("Alice");

        scheduler.scheduleMeeting("Meeting 2", LocalDateTime.of(2024, 5, 15, 12, 0),
                LocalDateTime.of(2024, 5, 15, 13, 0), participants2);

        assertEquals(2, scheduler.getMeetingsForParticipant("Alice").size());
    }

    @Test
    public void getMeetingsForParticipant_NoExistingMeetings_EmptyList() {
        List<MeetingScheduler.Meeting> meetings = scheduler.getMeetingsForParticipant("Bob");
        assertTrue(meetings.isEmpty());
    }

    @Test
    public void getMeetingsForParticipant_ExistingMeetings_ListOfMeetings() {
        List<String> participants = new ArrayList<>();
        participants.add("Bob");

        scheduler.scheduleMeeting("Team Meeting", LocalDateTime.of(2024, 5, 20, 10, 0),
                LocalDateTime.of(2024, 5, 20, 11, 0), participants);

        List<MeetingScheduler.Meeting> meetings = scheduler.getMeetingsForParticipant("Bob");
        assertEquals(1, meetings.size());
    }

    @Test
    public void getMeetingsForDay_NoMeetingsForDay_EmptyList() {
        List<MeetingScheduler.Meeting> meetings = scheduler.getMeetingsForDay(LocalDate.of(2024, 5, 11));
        assertTrue(meetings.isEmpty());
    }

    @Test
    public void getMeetingsForDay_MeetingsForDay_ListOfMeetings() {
        List<String> participants = new ArrayList<>();
        participants.add("Alice");
        participants.add("Bob");

        scheduler.scheduleMeeting("Team Meeting", LocalDateTime.of(2024, 5, 10, 9, 0),
                LocalDateTime.of(2024, 5, 10, 10, 0), participants);

        List<MeetingScheduler.Meeting> meetings = scheduler.getMeetingsForDay(LocalDate.of(2024, 5, 10));
        assertEquals(2, meetings.size());
    }
}

