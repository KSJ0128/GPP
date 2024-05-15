package Happy20.GrowingPetPlant.Status.Controller;

import Happy20.GrowingPetPlant.Status.Domain.Status;
import Happy20.GrowingPetPlant.Status.Service.StatusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

// 온도, 습도, 토양 습도
@RestController
@RequestMapping("status")
public class StatusController {
    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    // 식물 생성 api
    // createStatus

    // 식물 최근 온도 확인 api
    @GetMapping("/temp")
    public Long GetRecentTemp(@RequestParam("plantNumber") Long plantNumber) {
        return statusService.recentPlantTemp(plantNumber);
    }

    // 식물 최근 습도 확인 api
    @GetMapping("/moisture")
    public Long GetMoisture(@RequestParam("plantNumber") Long plantNumber) {
        return statusService.recentPlantMoisture(plantNumber);
    }

    // 식물 최근 토양 습도 확인 api
    @GetMapping("/humi")
    public Long GetHumidity(@RequestParam("plantNumber") Long plantNumber) {
        return statusService.recentPlantHumidity(plantNumber);
    }

    // 식물 토양 습도 확인 api
    @GetMapping("/name")
    public String GetName(@RequestParam("plantNumber") Long plantNumber) {
        return statusService.getPlantName(plantNumber);
    }

    // 물 준 날짜 리스트 리턴 api
    @GetMapping("/wateringdate")
    public List<LocalDate> getWateringDates(@RequestParam("plantNumber") Long plantNumber) {
        return statusService.getWateringDate(plantNumber);
    }
}
