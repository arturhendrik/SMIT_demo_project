package ee.smit.backend.config;

import java.util.List;

public class ApiEndpointConfig {
    private String url;
    private String getRequest;
    private String responseType;
    private String name;
    private String address;
    private List<String> vehicleTypes;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGetRequest() {
        return getRequest;
    }

    public void setGetRequest(String getRequest) {
        this.getRequest = getRequest;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
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
