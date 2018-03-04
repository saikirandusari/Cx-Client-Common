package cx.restclient.sast.dto;

import cx.restclient.common.BaseStatus;

/**
 * Created by Galn on 06/02/2018.
 */
public class SASTScanStatus extends BaseStatus {

    private CurrentStatus name;
    private Details details;

    public CurrentStatus getName() {
        return name;
    }

    public void setName(CurrentStatus name) {
        this.name = name;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }
}
