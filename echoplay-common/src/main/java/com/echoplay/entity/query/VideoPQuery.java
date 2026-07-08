package com.echoplay.entity.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VideoPQuery extends BaseParma {
    private String pId;
    private String videoId;
    private String fileId;
}
