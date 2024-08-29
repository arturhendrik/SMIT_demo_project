package ee.smit.backend.dto;

import java.util.List;

public class AvailableTime {
    private String id;
    private String time;
    private String name;
    private String address;
    private List<String> vehicleTypes;

    public AvailableTime(String id, String time, String name, String address, List<String> vehicleTypes) {
        this.id = id;
        this.time = time;
        this.name = name;
        this.address = address;
        this.vehicleTypes = vehicleTypes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getVehicleTypes() {
        return vehicleTypes;
    }

    public void setVehicleTypes(List<String> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
    }
}
