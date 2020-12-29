package be.kuleuven.csa.domain;

import be.kuleuven.csa.domain.helpdomain.DataVoorZitInTableView;

import java.util.List;

public interface ZitInRepository {

    List<ZitIn> getAlleZitInByVerkoopID(int verkoop_id);
    List<DataVoorZitInTableView> getAlleZitVoorTableViewInByVerkoopIDAndWeeknr(int verkoop_id, int weeknr);
    void voegZitInToe(ZitIn zitIn);
    void verwijderZitInByVerkooptID(int verkoopt_id);
}
