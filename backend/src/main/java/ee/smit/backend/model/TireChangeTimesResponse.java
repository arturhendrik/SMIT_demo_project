package ee.smit.backend.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "tireChangeTimesResponse")
public class TireChangeTimesResponse {
    private List<AvailableTimeXml> availableTimes;

    @XmlElement(name = "availableTime")
    public List<AvailableTimeXml> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(List<AvailableTimeXml> availableTimes) {
        this.availableTimes = availableTimes;
    }
}
