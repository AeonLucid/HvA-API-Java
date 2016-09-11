# HvA-API-Java

An Java API to authenticate with and request data from the Hogeschool van Amsterdam.

## Installation

Download [the latest JAR](https://search.maven.org/remote_content?g=com.github.aeonlucid.hvaapi&a=hva-api&v=LATEST) or grab via Maven:
```xml
<dependency>
  <groupId>com.github.aeonlucid.hvaapi</groupId>
  <artifactId>hva-api</artifactId>
  <version>1.0</version>
</dependency>
```
or Gradle:
```groovy
compile group: 'com.github.aeonlucid.hvaapi', name: 'hva-api', version: '1.0'
```

## Example

```java
class Main {

    public static void main(String[] args) {
        HvAClient client = new HvAClient("username", "password");

        if(client.signIn()) {
            final Schedule schedule = client.getSchedules("IS101")[0];

            for (TimetableItem timetableItem : client.getOtherTimeTable(schedule.getValue(), DateTime.now().weekOfWeekyear().get())) {
                final DateTime beginDate = new DateTime(timetableItem.getStartDate());
                final DateTime endDate = new DateTime(timetableItem.getEndDate());

                System.out.println(beginDate.toString("dd/MM HH:mm") + " - " + endDate.toString("dd/MM HH:mm") + ": " + timetableItem.getActivityDescription());
            }
        } else {
            System.err.println("Couldn't sign in, wrong credentials specified.");
        }

        client.signOut();
    }

}
```

Output:
```
05/09 08:30 - 05/09 11:10: Project Fasten Your Seatbelts
05/09 14:30 - 05/09 16:10: FYS Coaching
06/09 08:30 - 06/09 12:40: Project Fasten Your Seatbelts
06/09 12:50 - 06/09 17:00: Project Fasten Your Seatbelts
07/09 08:30 - 07/09 11:00: Bedrijfscursus
07/09 11:10 - 07/09 13:40: Bedrijfscursus
07/09 14:30 - 07/09 17:00: Personal Skills
08/09 08:30 - 08/09 11:50: Essential Skills Wiskunde
08/09 12:00 - 08/09 13:40: Programming
08/09 13:40 - 08/09 15:20: Programming
09/09 08:30 - 09/09 11:00: User Interaction
```

## Implemented API calls

- [x] signIn
- [x] signOut
- [x] getCurrentUser
- [x] updateProfile
- [x] getDomains
- [x] getProgrammes
- [x] getAbsentees
- [ ] getAnnouncements (Response data unknown)
- [x] getNews
- [x] getLocations
- [x] getStudylocations
- [x] getStudylocationPage
- [ ] getPeople (Server throws an exception, also broken in the official 'MijnHvApp')
- [x] getMyTimeTable
- [x] getSchedules
- [x] otherSchedule
- [x] getAZUrlsForEmployees