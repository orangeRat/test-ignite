package su.dru.ignite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import su.dru.ignite.dao.ClientProfileDao;
import su.dru.ignite.domain.ClientProfile;
import su.dru.ignite.dto.ProfileList;

import java.util.List;

/**
 * @author Andrey Vorobyov
 */
@Service
public class ClientProfileService {
    @Autowired
    private ClientProfileDao profileDao;

    public ClientProfile byCtn(long ctn) {
        return profileDao.byCtn(ctn);
    }

    public void add(ClientProfile clientProfile) {
        profileDao.add(clientProfile);
    }

    public ProfileList byCellId(long cellId) {
        List<ClientProfile> clientProfiles = profileDao.byCellId(cellId);

        return new ProfileList(clientProfiles.size(), clientProfiles);
    }

    public void addAll(List<ClientProfile> profiles) {
        profileDao.addAll(profiles);
    }
}
