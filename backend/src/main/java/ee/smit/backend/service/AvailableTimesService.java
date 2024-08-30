package ee.smit.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.smit.backend.config.ApiConfig;
import ee.smit.backend.config.ApiEndpointConfig;
import ee.smit.backend.dto.AvailableTime;
import ee.smit.backend.model.AvailableTimeJson;
import ee.smit.backend.model.AvailableTimeXml;
import ee.smit.backend.model.TireChangeTimesResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class AvailableTimesService {

    private final ApiConfig apiConfig;
    private final WebClient webClient;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public AvailableTimesService(ApiConfig apiConfig, WebClient.Builder webClientBuilder) {
        this.apiConfig = apiConfig;
        this.webClient = webClientBuilder.build();
    }

    public List<AvailableTime> getAvailableTimes() {
        List<Mono<List<AvailableTime>>> apiRequests = apiConfig.getEndpoints().stream()
                .map(endpoint -> fetchTimes(endpoint)
                        .subscribeOn(Schedulers.boundedElastic()))
                .toList();

        return apiRequests.stream()
                .map(Mono::block)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .toList()
                .stream().sorted(Comparator.comparing(time -> LocalDateTime.parse(time.getTime(), FORMATTER)))
                .toList();
    }

    private Mono<List<AvailableTime>> fetchTimes(ApiEndpointConfig endpoint) {
        return webClient.get()
                .uri(endpoint.getUrl()+endpoint.getGetRequest())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    if ("xml".equalsIgnoreCase(endpoint.getResponseType())) {
                        return parseXmlResponse(response, endpoint.getName(), endpoint.getAddress(), endpoint.getVehicleTypes());
                    } else if ("json".equalsIgnoreCase(endpoint.getResponseType())) {
                        return parseJsonResponse(response, endpoint.getName(), endpoint.getAddress(), endpoint.getVehicleTypes());
                    }
                    return new ArrayList<>();
                });
    }

    private List<AvailableTime> parseXmlResponse(String xmlResponse, String name, String address, List<String> vehicleTypes) {
        List<AvailableTime> availableTimes = new ArrayList<>();
        try {
            JAXBContext context = JAXBContext.newInstance(TireChangeTimesResponse.class);
            TireChangeTimesResponse tireChangeTimesResponse = (TireChangeTimesResponse) context.createUnmarshaller().unmarshal(new StringReader(xmlResponse));

            for (AvailableTimeXml availableTimeXml : tireChangeTimesResponse.getAvailableTimes()) {
                availableTimes.add(new AvailableTime(
                        availableTimeXml.getUuid(),
                        availableTimeXml.getTime(),
                        name,
                        address,
                        vehicleTypes
                ));
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return availableTimes;
    }

    private List<AvailableTime> parseJsonResponse(String jsonResponse, String name, String address, List<String> vehicleTypes) {
        List<AvailableTime> availableTimes = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            AvailableTimeJson[] timeJsons = objectMapper.readValue(jsonResponse, AvailableTimeJson[].class);
            for (AvailableTimeJson availableTimeJson : timeJsons) {
                if (availableTimeJson.isAvailable()) {
                    availableTimes.add(new AvailableTime(
                            String.valueOf(availableTimeJson.getId()),
                            availableTimeJson.getTime(),
                            name,
                            address,
                            vehicleTypes
                    ));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return availableTimes;
    }

    public Mono<String> bookTime(String id, String contactInformation, String name) {
        ApiEndpointConfig endpoint = findApiEndpointByName(name);
        if (endpoint == null) {
            throw new IllegalArgumentException("No endpoint found for name: " + name);
        }

        String url = endpoint.getUrl()+endpoint.getPostOrPutRequestUrl(id);

        if ("xml".equalsIgnoreCase(endpoint.getResponseType())) {
            String xmlBody = generateXmlRequestBody(name, contactInformation);
            return webClient.put()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_XML)
                    .bodyValue(xmlBody)
                    .retrieve()
                    .bodyToMono(String.class);
        } else if ("json".equalsIgnoreCase(endpoint.getResponseType())) {
            String jsonBody = generateJsonRequestBody(contactInformation);
            return webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(jsonBody)
                    .retrieve()
                    .bodyToMono(String.class);
        } else {
            return Mono.error(new IllegalArgumentException("Unsupported response type: " + endpoint.getResponseType()));
        }
    }

    private String generateXmlRequestBody(String name, String contactInformation) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<"+name+".tireChangeBookingRequest>" +
                "<contactInformation>" + contactInformation + "</contactInformation>" +
                "</"+name+".tireChangeBookingRequest>";
    }

    private String generateJsonRequestBody(String contactInformation) {
        return "{ \"contactInformation\": \"" + contactInformation + "\" }";
    }

    private ApiEndpointConfig findApiEndpointByName(String name) {
        return apiConfig.getEndpoints().stream()
                .filter(endpoint -> name.equals(endpoint.getName()))
                .findFirst()
                .orElse(null);
    }
}
