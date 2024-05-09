package sit707_tasks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeetingScheduler {

    private Map<String, List<Meeting>> participantMeetings;

    public MeetingScheduler() {
        participantMeetings = new HashMap<>();
    }

    public void scheduleMeeting(String title, LocalDateTime startDateTime, LocalDateTime endDateTime, List<String> participants) {
        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("Start date time cannot be after end date time");
        }

        for (String participant : participants) {
            List<Meeting> meetings = participantMeetings.getOrDefault(participant, new ArrayList<>());
            for (Meeting meeting : meetings) {
                if (meeting.overlaps(startDateTime, endDateTime)) {
                    throw new IllegalStateException("There is a conflict with existing meeting for participant: " + participant);
                }
            }
        }

        Meeting newMeeting = new Meeting(title, startDateTime, endDateTime, participants);
        for (String participant : participants) {
            List<Meeting> meetings = participantMeetings.getOrDefault(participant, new ArrayList<>());
            meetings.add(newMeeting);
            participantMeetings.put(participant, meetings);
        }
    }

    public List<Meeting> getMeetingsForParticipant(String participant) {
        return participantMeetings.getOrDefault(participant, new ArrayList<>());
    }

    public List<Meeting> getMeetingsForDay(LocalDate date) {
        List<Meeting> meetingsForDay = new ArrayList<>();
        for (List<Meeting> meetings : participantMeetings.values()) {
            for (Meeting meeting : meetings) {
                if (meeting.getStartDate().toLocalDate().isEqual(date) || meeting.getEndDate().toLocalDate().isEqual(date)) {
                    meetingsForDay.add(meeting);
                }
            }
        }
        return meetingsForDay;
    }

    public static class Meeting {
        private String title;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private List<String> participants;

        public Meeting(String title, LocalDateTime startDate, LocalDateTime endDate, List<String> participants) {
            this.title = title;
            this.startDate = startDate;
            this.endDate = endDate;
            this.participants = participants;
        }

        public String getTitle() {
            return title;
        }

        public LocalDateTime getStartDate() {
            return startDate;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

        public List<String> getParticipants() {
            return participants;
        }

        public boolean overlaps(LocalDateTime startDateTime, LocalDateTime endDateTime) {
            return !startDate.isAfter(endDateTime) && !endDate.isBefore(startDateTime);
        }
    }
}

