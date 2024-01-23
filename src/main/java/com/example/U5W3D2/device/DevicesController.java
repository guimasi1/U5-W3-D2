package com.example.U5W3D2.device;

import com.example.U5W3D2.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/devices")
public class DevicesController {
    @Autowired
    DevicesService devicesService;

    @GetMapping
    public Page<Device> getDevices(
                                @RequestParam(required = false) String type,
                                @RequestParam(required = false) String status,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "id") String id) {
        return devicesService.getDevices(type,status,page,size,id);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DevicesResponseDTO create(@RequestBody @Validated NewDeviceDTO device, BindingResult validation) {
        if(validation.hasErrors()) {
            System.out.println(validation.getAllErrors());
            throw new BadRequestException("Errori nel payload.");
        } else {
            Device newDevice = devicesService.save(device);
            return new DevicesResponseDTO(newDevice.getId());
        }
    }

    @PutMapping("/{uuid}")
    public Device updateById(@PathVariable UUID uuid, @RequestBody NewDeviceDTO body) {
        return devicesService.findByIdAndUpdate(uuid, body);
    }

    @PatchMapping("/{id}/changeStatus")
    public Device changeStatusById(@PathVariable UUID id, @RequestBody DevicesStatusDTO statusDTO) {
        return devicesService.changeStatus(id, statusDTO.status());
    }

    @PatchMapping("/{id}/changeType")
    public Device changeTypeById(@PathVariable UUID id, @RequestBody DevicesTypeDTO devicesTypeDTO) {
        return devicesService.changeType(id, devicesTypeDTO.type());
    }

    @DeleteMapping("/{uuid}")
    public void deleteById(@PathVariable UUID uuid) {
        devicesService.deleteById(uuid);
    }

    @GetMapping("/{id}")
    public Device getDeviceById(@PathVariable UUID id) {
        return devicesService.findById(id);
    }

}
