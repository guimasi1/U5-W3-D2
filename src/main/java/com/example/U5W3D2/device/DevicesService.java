package com.example.U5W3D2.device;

import com.example.U5W3D2.exceptions.BadRequestException;
import com.example.U5W3D2.exceptions.NotFoundException;
import com.example.U5W3D2.user.User;
import com.example.U5W3D2.user.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class DevicesService {
    @Autowired
    DevicesDAO devicesDAO;

    @Autowired
    UsersService usersService;

    public Page<Device> getDevices(int page, int size, String orderBy) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(orderBy));
        return devicesDAO.findAll(pageable);
    }
    public Page<Device> getDevices(String type, String status, int page, int size, String orderBy) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(orderBy));
        if(type == null && status == null) return devicesDAO.findAll(pageable);
        if(type == null) return devicesDAO.findByStatus(status,pageable);
        if(status == null) return devicesDAO.findByType(type,pageable);
        return devicesDAO.findByTypeAndStatus(type,status,pageable);
    }
    public Device save(NewDeviceDTO device) {
        List<String> types = List.of("smartphone", "tablet", "laptop");
        List<String> statuses = List.of("available", "assigned", "in_maintenance", "disused");
        User user = usersService.findById(device.userUUID());
        if(!types.contains(device.type())) throw new BadRequestException("Non è possibile inserire '" + device.type() + "' come tipo di dispositivo. Scegliere " +
                "uno fra questi tipi: smartphone, tablet, laptop");
        if(!statuses.contains(device.status())) throw new BadRequestException("Non è possibile inserire '" + device.status() + "' come stato del dispositivo. Scegliere " +
                "uno fra questi stati: available, assigned, in_maintenance, disused");
        Device newDevice = new Device();
        newDevice.setType(device.type());
        newDevice.setUser(user);
        newDevice.setStatus(device.status());
        return devicesDAO.save(newDevice);
    }

    public Device findById(UUID id) {
        return devicesDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Device findByIdAndUpdate(UUID id, NewDeviceDTO body) {
        Device found = this.findById(id);
        User user = usersService.findById(body.userUUID());
        found.setStatus(body.status());
        found.setType(body.type());
        found.setUser(user);

        return devicesDAO.save(found);
    }

    public void deleteById(UUID uuid) {
        Device found = this.findById(uuid);
        devicesDAO.delete(found);
    }

    public Device changeStatus(UUID uuid, String status) {
        Device found = this.findById(uuid);
        List<String> statuses = List.of("available","assigned","in_maintenance","disused");
        if(statuses.contains(status)) {
            found.setStatus(status);
            return devicesDAO.save(found);
        } else {
            throw new BadRequestException("Non è possibile inserire '" + status + "' come stato del dispositivo. Scegliere " +
                    "uno fra questi stati: available, assigned, in_maintenance, disused");
        }

    }

    public Device changeType(UUID uuid, String type) {
        Device found = this.findById(uuid);
        List<String> types = List.of("smartphone", "tablet", "laptop");
        if(types.contains(type)) {
            found.setType(type);
            return devicesDAO.save(found);
        } else {
            throw new BadRequestException("Non è possibile inserire '" + type + "' come tipo di dispositivo. Scegliere " +
                    "uno fra questi tipi: smartphone, tablet, laptop");
        }
    }


}
