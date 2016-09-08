package com.github.aeonlucid.hvaapi.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TimetableItem {

    @JsonProperty("StartDate")
    private long startDate;

    @JsonProperty("EndDate")
    private long endDate;

    @JsonProperty("StartTime")
    private String startTime;

    @JsonProperty("EndTime")
    private String endTime;

    @JsonProperty("ModuleCode")
    private String moduleCode;

    @JsonProperty("ActivityDescription")
    private String activityDescription;

    @JsonProperty("Locations")
    private TimetableLocation[] locations;

    @JsonProperty("StaffMembers")
    private String[] staffMembers;

    @JsonProperty("StudentSets")
    private String[] studentSets;

    @JsonProperty("Location")
    private String location;

    @JsonProperty("Teachers")
    private String teachers;

    @JsonProperty("Groups")
    private String groups;

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public TimetableLocation[] getLocations() {
        return locations;
    }

    public String[] getStaffMembers() {
        return staffMembers;
    }

    public String[] getStudentSets() {
        return studentSets;
    }

    public String getLocation() {
        return location;
    }

    public String getTeachers() {
        return teachers;
    }

    public String getGroups() {
        return groups;
    }

}
