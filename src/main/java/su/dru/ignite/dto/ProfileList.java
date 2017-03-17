package su.dru.ignite.dto;

import su.dru.ignite.domain.ClientProfile;

import java.util.List;

/**
 * @author Andrey Vorobyov
 */
public class ProfileList {
    private final int total;
    private final List<ClientProfile> results;

    public ProfileList(int total, List<ClientProfile> results) {
        this.total = total;
        this.results = results;
    }

    public int getTotal() {
        return total;
    }

    public List<ClientProfile> getResults() {
        return results;
    }
}
