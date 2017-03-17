package su.dru.ignite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import su.dru.ignite.domain.ClientProfile;
import su.dru.ignite.dto.ProfileList;
import su.dru.ignite.service.ClientProfileService;

import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Andrey Vorobyov
 */
@RestController
@RequestMapping("profile")
public class ClientProfileController {
    @Autowired
    private ClientProfileService profileService;

    @RequestMapping(value = "{ctn}", method = GET)
    public ClientProfile byCtn(@PathVariable Long ctn) {
        ClientProfile profile = profileService.byCtn(ctn);

        if (profile != null) {
            return profile;
        } else {
            throw new NotFoundException();
        }
    }

    @RequestMapping(method = POST)
    public void add(@RequestBody ClientProfile profile) {
        profileService.add(profile);
    }

    @RequestMapping(value = "batch", method = POST)
    public void addAll(@RequestBody List<ClientProfile> profiles) {
        profileService.addAll(profiles);
    }

    @RequestMapping(value = "by-cell/{cellId}", method = GET)
    public ProfileList byCell(@PathVariable long cellId) {
        ProfileList profiles = profileService.byCellId(cellId);

        if (profiles.getTotal() > 0) {
            return profiles;
        } else {
            throw new NotFoundException();
        }
    }
}
